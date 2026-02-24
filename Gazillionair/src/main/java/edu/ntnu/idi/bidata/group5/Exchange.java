package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a stock exchange where stocks are traded.
 * The exchange keeps track of available stocks, the current trading week,
 * and is responsible for advancing time and updating stock prices.
 */
public class Exchange {

  /** Minimum stock price. */
  private static final BigDecimal MIN_PRICE = new BigDecimal("0.01");

  /** Maximum percentage change applied to a stock price per week. */
  private static final BigDecimal MAX_CHANGE = new BigDecimal("0.10"); // 10%

  /** The name of the exchange. */
  private final String name;

  /** Map of stocks indexed by their unique symbol. */
  private final Map<String, Stock> stockMap;

  /** Random generator used for price changes. */
  private final Random random;

  /** The current trading week. */
  private int week;

  /**
   * Constructs a new {@code Exchange} with the given name and stocks.
   * The trading week starts at week 1.
   *
   * @param name the name of the exchange
   * @param stocks the list of stocks traded on the exchange
   * @throws IllegalArgumentException if the name is {@code null} or blank,
   *     if the stock list is {@code null}, or if duplicate stock symbols are provided
   */
  public Exchange(String name, List<Stock> stocks) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Exchange name cannot be null or blank");
    }
    if (stocks == null) {
      throw new IllegalArgumentException("Stock list cannot be null");
    }

    this.stockMap = new HashMap<>();
    for (Stock stock : stocks) {
      if (stock == null) {
        throw new IllegalArgumentException("Stock in list cannot be null");
      }
      String symbol = stock.getSymbol();
      if (stockMap.containsKey(symbol)) {
        throw new IllegalArgumentException("Duplicate stock symbol: " + symbol);
      }
      stockMap.put(symbol, stock);
    }

    this.name = name;
    this.week = 1;
    this.random = new Random();
  }

  public String getName() {
    return name;
  }

  public int getWeek() {
    return week;
  }

  /**
   * Returns the stock with the given symbol.
   *
   * @param symbol the stock symbol
   * @return the stock with the specified symbol
   * @throws IllegalArgumentException if the symbol is {@code null}, blank,
   *     or no stock with the symbol exists
   */
  public Stock getStock(String symbol) {
    if (symbol == null || symbol.isBlank()) {
      throw new IllegalArgumentException("Symbol cannot be null or blank");
    }
    Stock stock = stockMap.get(symbol);
    if (stock == null) {
      throw new IllegalArgumentException("No stock found with symbol: " + symbol);
    }
    return stock;
  }

  /**
   * Searches for stocks whose symbol or company name contains the given keyword.
   * The search is case-insensitive.
   *
   * @param keyword the search keyword
   * @return a list of matching stocks, or an empty list if no matches are found
   */
  public List<Stock> findStocks(String keyword) {
    if (keyword == null || keyword.isBlank()) {
      return List.of();
    }

    String k = keyword.toLowerCase().trim();
    List<Stock> matches = new ArrayList<>();

    for (Stock stock : stockMap.values()) {
      if (stock.getSymbol().toLowerCase().contains(k)
              || stock.getCompany().toLowerCase().contains(k)) {
        matches.add(stock);
      }
    }
    return matches;
  }

  /**
   * Buys shares of a stock for the given player. The purchase is committed immediately.
   *
   * @param player the player who buys
   * @param symbol the stock symbol
   * @param quantity the quantity to buy
   * @return the completed purchase transaction
   * @throws IllegalArgumentException if player, symbol or quantity are invalid
   * @throws IllegalStateException if the player cannot afford the purchase
   */
  public Purchase buy(Player player, String symbol, BigDecimal quantity) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (symbol == null || symbol.isBlank()) {
      throw new IllegalArgumentException("Symbol cannot be null or blank");
    }
    if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than zero");
    }

    Stock stock = getStock(symbol);
    BigDecimal purchasePrice = stock.getSalesPrice();
    Share share = new Share(stock, quantity, purchasePrice);

    Purchase purchase = new Purchase(share, week);
    purchase.commit(player);
    return purchase;
  }

  /**
   * Sells an owned share for the given player. The sale is committed immediately.
   *
   * @param player the player who sells
   * @param share the share to sell
   * @return the completed sale transaction
   * @throws IllegalArgumentException if player or share are invalid
   * @throws IllegalStateException if the player does not own the share
   */
  public Sale sell(Player player, Share share) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (share == null) {
      throw new IllegalArgumentException("Share cannot be null");
    }

    Sale sale = new Sale(share, week);
    sale.commit(player);
    return sale;
  }

  /**
   * Advances the exchange by one trading week and updates all stock prices.
   */
  public void advance() {
    week++;

    for (Stock stock : stockMap.values()) {
      BigDecimal current = stock.getSalesPrice();
      BigDecimal newPrice = applyRandomChange(current);
      stock.addNewSalesPrice(newPrice);
    }
  }

  private BigDecimal applyRandomChange(BigDecimal current) {
    double sign = (random.nextDouble() * 2.0) - 1.0;
    BigDecimal changePercent = MAX_CHANGE.multiply(BigDecimal.valueOf(sign));

    BigDecimal multiplier = BigDecimal.ONE.add(changePercent);
    BigDecimal newPrice = current.multiply(multiplier);

    if (newPrice.compareTo(MIN_PRICE) < 0) {
      newPrice = MIN_PRICE;
    }

    return newPrice.setScale(2, RoundingMode.HALF_UP);
  }
}