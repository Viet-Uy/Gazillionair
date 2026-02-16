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

  /** Minimum stock price.*/
  private static final BigDecimal MIN_PRICE = new BigDecimal("0.01");

  /** Maximum percentage change applied to a stock price per week. */
  private static final BigDecimal MAX_CHANGE = new BigDecimal("0.10"); // 10%

  /** The name of the exchange. */
  private final String name;

  /** Map of stocks indexed by their unique symbol. */
  private final Map<String, Stock> stocksBySymbol;

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
   * if the stock list is {@code null}, or if duplicate stock symbols are provided
   */
  public Exchange(String name, List<Stock> stocks) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Exchange name cannot be null or blank");
    }
    if (stocks == null) {
      throw new IllegalArgumentException("Stocks list cannot be null");
    }

    this.name = name;
    this.week = 1;
    this.random = new Random();
    this.stocksBySymbol = new HashMap<>();

    for (Stock stock : stocks) {
      if (stock == null) {
        throw new IllegalArgumentException("Stock in list cannot be null");
      }
      String symbol = stock.getSymbol();
      if (stocksBySymbol.containsKey(symbol)) {
        throw new IllegalArgumentException("Duplicate stock symbol: " + symbol);
      }
      stocksBySymbol.put(symbol, stock);
    }
  }

  /**
   * Returns the name of the exchange.
   *
   * @return the exchange name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the current trading week.
   *
   * @return the current week number
   */
  public int getWeek() {
    return week;
  }
  /**
   * Returns the stock with the given symbol.
   *
   * @param symbol the stock symbol
   * @return the stock with the specified symbol
   *@throws IllegalArgumentException if the symbol is {@code null}, blank,
   * or no stock with the symbol exists
   */

  public Stock getStock(String symbol) {
    if (symbol == null || symbol.isBlank()) {
      throw new IllegalArgumentException("Symbol cannot be null or blank");
    }
    Stock stock = stocksBySymbol.get(symbol);
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

    for (Stock stock : stocksBySymbol.values()) {
      if (stock.getSymbol().toLowerCase().contains(k)
              || stock.getCompany().toLowerCase().contains(k)) {
        matches.add(stock);
      }
    }
    return matches;
  }

  /**
   * Advances the exchange by one trading week.
   * The current week number is incremented and the sales price of each stock
   * is updated by applying a random percentage change.
   */
  public void advance() {
    week++;

    for (Stock stock : stocksBySymbol.values()) {
      BigDecimal current = stock.getSalesPrice();
      BigDecimal newPrice = applyRandomChange(current);
      stock.addNewSalesPrice(newPrice);
    }
  }

  /**
   * Applies a random percentage change to a stock price.
   *
   * @param current the current sales price
   * @return the updated sales price
   */
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
