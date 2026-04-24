package edu.ntnu.idi.bidata.group5.service;

import edu.ntnu.idi.bidata.group5.model.Player;
import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Share;
import edu.ntnu.idi.bidata.group5.model.Stock;
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
  private static final BigDecimal NORMAL_MAX_CHANGE = new BigDecimal("0.04"); // 4%
  private static final BigDecimal BULL_SPIKE_MIN = new BigDecimal("0.08"); // 8%
  private static final BigDecimal BULL_SPIKE_MAX = new BigDecimal("0.18"); // 18%
  private static final BigDecimal BEAR_DROP_MIN = new BigDecimal("0.08"); // 8%
  private static final BigDecimal BEAR_DROP_MAX = new BigDecimal("0.14"); // 14%
  private static final double BULL_SPIKE_CHANCE = 0.06; // 6%
  private static final double BEAR_DROP_CHANCE = 0.03; // 3%

  /** The name of the exchange. */
  private final String name;

  /** Map of stocks indexed by their unique symbol. */
  private final Map<String, Stock> stockMap;

  /** Random generator used for price changes. */
  private final Random random;

  /** The current trading week. */
  private int week;

  /** Map storing pending news impact for each stock. */
  private final Map<String, BigDecimal> newsImpactMap;

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
    this.newsImpactMap = new HashMap<>();
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
      BigDecimal newsImpact = newsImpactMap.getOrDefault(stock.getSymbol(), BigDecimal.ZERO);
      BigDecimal newPrice = applyRandomChange(current, newsImpact);
      stock.addNewSalesPrice(newPrice);
    }
    newsImpactMap.clear();
  }

  /**
   * Returns the stocks with the largest positive price changes since last week,
   * sorted by price change from highest to lowest.
   *
   * @param limit the maximum number of stocks to return
   * @return a list of top gaining stocks, sorted by price change (descending)
   * @throws IllegalArgumentException if limit is less than or equal to zero
   */
  public List<Stock> getGainers(int limit) {
    if (limit <= 0) {
      throw new IllegalArgumentException("Limit must be more than zero");
    }

    return stockMap.values().stream()
        .sorted((a, b) -> b.getLatestPriceChange().compareTo(a.getLatestPriceChange()))
        .limit(limit)
        .toList();
  }

  /**
   * Returns the stocks with the largest negative price changes since last week,
   * sorted by price change from lowest to highest (biggest losses first).
   *
   * @param limit the maximum number of stocks to return
   * @return a list of top losing stocks, sorted by price change (ascending)
   * @throws IllegalArgumentException if limit is less than or equal to zero
   */
  public List<Stock> getLosers(int limit) {
    if (limit <= 0) {
      throw new IllegalArgumentException("Limit must be more than zero");
    }

    return stockMap.values().stream()
        .sorted((a, b) -> a.getLatestPriceChange().compareTo(b.getLatestPriceChange()))
        .limit(limit)
        .toList();
  }

  private BigDecimal applyRandomChange(BigDecimal current, BigDecimal newsImpact) {
    BigDecimal changePercent = randomChangePercent();
    changePercent = changePercent.add(newsImpact);

    BigDecimal multiplier = BigDecimal.ONE.add(changePercent);
    BigDecimal newPrice = current.multiply(multiplier);

    if (newPrice.compareTo(MIN_PRICE) < 0) {
      newPrice = MIN_PRICE;
    }

    return newPrice.setScale(2, RoundingMode.HALF_UP);
  }

  private BigDecimal randomChangePercent() {
    double roll = random.nextDouble();
    if (roll < BULL_SPIKE_CHANCE) {
      return randomBetween(BULL_SPIKE_MIN, BULL_SPIKE_MAX);
    }
    if (roll < BULL_SPIKE_CHANCE + BEAR_DROP_CHANCE) {
      return randomBetween(BEAR_DROP_MIN, BEAR_DROP_MAX).negate();
    }
    double sign = (random.nextDouble() * 2.0) - 1.0;
    return NORMAL_MAX_CHANGE.multiply(BigDecimal.valueOf(sign));
  }

  private BigDecimal randomBetween(BigDecimal min, BigDecimal max) {
    return min.add(max.subtract(min).multiply(BigDecimal.valueOf(random.nextDouble())));
  }

  /**
   * Applies news sentiment impact to affected stocks.
   * Impact is accumulated and applied during the next advance() call.
   *
   * @param symbol the stock symbol or "Market-wide" for all stocks
   * @param sentiment the news sentiment: "positive", "negative", or "neutral"
   */
  public void applyNewsImpact(String symbol, String sentiment) {
    if (symbol == null || symbol.isBlank() || sentiment == null || sentiment.isBlank()) {
      return;
    }

    BigDecimal impactPercent = getNewsImpactPercent(sentiment);
    if (impactPercent.compareTo(BigDecimal.ZERO) == 0) {
      return;
    }

    if ("Market-wide".equalsIgnoreCase(symbol)) {
      for (Stock stock : stockMap.values()) {
        accumImpactForStock(stock.getSymbol(), impactPercent);
      }
    } else {
      String[] symbols = symbol.split(",\\s*");
      for (String sym : symbols) {
        try {
          Stock stock = getStock(sym.toUpperCase());
          accumImpactForStock(stock.getSymbol(), impactPercent);
        } catch (IllegalArgumentException e) {
          // Symbol not found, skip it
        }
      }
    }
  }

  private void accumImpactForStock(String symbol, BigDecimal impactPercent) {
    newsImpactMap.put(symbol, newsImpactMap.getOrDefault(symbol, BigDecimal.ZERO)
        .add(impactPercent));
  }

  private BigDecimal getNewsImpactPercent(String sentiment) {
    switch (sentiment.toLowerCase()) {
      case "positive":
        return new BigDecimal("0.02"); // 2% increase
      case "negative":
        return new BigDecimal("-0.03"); // 3% decrease
      case "neutral":
      default:
        return BigDecimal.ZERO; // No impact
    }
  }
}
