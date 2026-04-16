package edu.ntnu.idi.bidata.group5.model;

import edu.ntnu.idi.bidata.group5.model.observer.ModelObserver;
import edu.ntnu.idi.bidata.group5.model.observer.ObservableModel;
import edu.ntnu.idi.bidata.group5.service.Exchange;
import edu.ntnu.idi.bidata.group5.service.GameEngine;
import edu.ntnu.idi.bidata.group5.service.TransactionFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates runtime game state and exposes use-case style operations for the UI layer.
 */
public class GameSession implements ObservableModel {

  private final Player player;
  private final Exchange exchange;
  private final List<Stock> marketStocks;
  private final List<ModelObserver> observers;
  private final TransactionFactory transactionFactory;
  private final GameEngine gameEngine;

  /**
   * Creates a new game session.
   *
   * @param playerName player display name
   * @param startingCapital initial cash amount
   * @param stocks initial market stocks
   */
  public GameSession(String playerName, BigDecimal startingCapital, List<Stock> stocks) {
    if (playerName == null || playerName.isBlank()) {
      throw new IllegalArgumentException("Player name cannot be null or blank");
    }
    if (startingCapital == null) {
      throw new IllegalArgumentException("Starting capital cannot be null");
    }
    if (stocks == null) {
      throw new IllegalArgumentException("Stocks cannot be null");
    }

    this.player = new Player(playerName, startingCapital);
    this.exchange = new Exchange("Gazillionair Exchange", stocks);
    this.marketStocks = new ArrayList<>(stocks);
    this.observers = new ArrayList<>();
    this.transactionFactory = new TransactionFactory();
    this.gameEngine = new GameEngine();
  }

  /**
   * Returns the current player.
   *
   * @return player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Returns the current week number.
   *
   * @return week number
   */
  public int getCurrentWeek() {
    return exchange.getWeek();
  }

  /**
   * Returns a snapshot of the market stocks.
   *
   * @return copied stock list
   */
  public List<Stock> getMarketStocks() {
    return new ArrayList<>(marketStocks);
  }

  /**
   * Returns all committed transactions for the session.
   *
   * @return transaction list
   */
  public List<Transaction> getTransactions() {
    return player.getTransactionArchive().getTransactions();
  }

  /**
   * Returns transactions performed in a specific week.
   *
   * @param week week number
   * @return transactions for the given week
   */
  public List<Transaction> getTransactionsForWeek(int week) {
    if (week < 1) {
      throw new IllegalArgumentException("Week must be at least 1");
    }
    return getTransactions().stream()
        .filter(transaction -> transaction.getWeek() == week)
        .toList();
  }

  /**
   * Returns all purchase transactions.
   *
   * @return purchase transactions
   */
  public List<Purchase> getPurchases() {
    return player.getTransactionArchive().getPurchases();
  }

  /**
   * Returns all sale transactions.
   *
   * @return sale transactions
   */
  public List<Sale> getSales() {
    return player.getTransactionArchive().getSales();
  }

  /**
   * Returns player's current net worth.
   *
   * @return net worth
   */
  public BigDecimal getNetWorth() {
    return player.getNetWorth();
  }

  /**
   * Returns current liquid cash.
   *
   * @return cash balance
   */
  public BigDecimal getCashBalance() {
    return player.getMoney();
  }

  /**
   * Returns current portfolio market value.
   *
   * @return portfolio value
   */
  public BigDecimal getPortfolioValue() {
    return player.getPortfolio().getNetWorth();
  }

  /**
   * Returns current holdings snapshot.
   *
   * @return shares currently held
   */
  public List<Share> getHoldings() {
    return player.getPortfolio().getShares();
  }

  /**
   * Returns true if player currently holds any shares.
   *
   * @return true when holdings exist
   */
  public boolean hasHoldings() {
    return !getHoldings().isEmpty();
  }

  /**
   * Returns player's current status.
   *
   * @return player status
   */
  public PlayerStatus getPlayerStatus() {
    return player.getStatus();
  }

  /**
   * Searches stocks by symbol or company.
   *
   * @param query search term
   * @return matching stocks
   */
  public List<Stock> searchStocks(String query) {
    if (query == null || query.isBlank()) {
      return getMarketStocks();
    }
    return exchange.findStocks(query);
  }

  /**
   * Returns top gaining stocks.
   *
   * @param limit max number of stocks
   * @return ordered gainers
   */
  public List<Stock> getTopGainers(int limit) {
    return exchange.getGainers(limit);
  }

  /**
   * Returns top losing stocks.
   *
   * @param limit max number of stocks
   * @return ordered losers
   */
  public List<Stock> getTopLosers(int limit) {
    return exchange.getLosers(limit);
  }

  /**
   * Returns stock details for a symbol.
   *
   * @param symbol stock symbol
   * @return stock details
   */
  public Stock getStock(String symbol) {
    return exchange.getStock(symbol);
  }

  /**
   * Returns stock price history for a symbol.
   *
   * @param symbol stock symbol
   * @return historical prices
   */
  public List<BigDecimal> getStockPriceHistory(String symbol) {
    return getStock(symbol).getHistoricalPrices();
  }

  /**
   * Creates and commits a buy transaction.
   *
   * @param symbol stock symbol
   * @param quantity share quantity
   * @return committed purchase
   */
  public Purchase buy(String symbol, int quantity) {
    if (symbol == null || symbol.isBlank()) {
      throw new IllegalArgumentException("Symbol cannot be null or blank");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than zero");
    }

    Stock stock = exchange.getStock(symbol);
    Share share = new Share(stock, BigDecimal.valueOf(quantity), stock.getSalesPrice());
    Purchase purchase = transactionFactory.createPurchase(share, getCurrentWeek());
    purchase.commit(player);
    publishModelChanged();
    return purchase;
  }

  /**
   * Creates and commits a sell transaction.
   *
   * @param symbol stock symbol
   * @param quantity share quantity
   * @return committed sale
   */
  public Sale sell(String symbol, int quantity) {
    if (symbol == null || symbol.isBlank()) {
      throw new IllegalArgumentException("Symbol cannot be null or blank");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than zero");
    }

    BigDecimal targetQuantity = BigDecimal.valueOf(quantity);
    Share shareToSell = getMatchingOwnedShare(symbol, targetQuantity);
    Sale sale = transactionFactory.createSale(shareToSell, getCurrentWeek());
    sale.commit(player);
    publishModelChanged();
    return sale;
  }

  /**
   * Advances the session by one week.
   */
  public void nextWeek() {
    gameEngine.nextWeek(this);
  }

  /**
   * Sells all currently held shares.
   *
   * @return list of committed sale transactions
   */
  public List<Sale> sellAllHoldings() {
    List<Share> sharesToSell = player.getPortfolio().getShares();
    if (sharesToSell.isEmpty()) {
      return List.of();
    }

    List<Sale> sales = new ArrayList<>();
    for (Share share : sharesToSell) {
      Sale sale = transactionFactory.createSale(share, getCurrentWeek());
      sale.commit(player);
      sales.add(sale);
    }
    publishModelChanged();
    return sales;
  }

  /**
   * Returns whether a buy is currently valid.
   *
   * @param symbol stock symbol
   * @param quantity share quantity
   * @return true if buy is valid
   */
  public boolean canBuy(String symbol, int quantity) {
    if (symbol == null || symbol.isBlank() || quantity <= 0) {
      return false;
    }

    Stock stock;
    try {
      stock = exchange.getStock(symbol);
    } catch (IllegalArgumentException e) {
      return false;
    }

    Share share = new Share(stock, BigDecimal.valueOf(quantity), stock.getSalesPrice());
    Purchase purchase = transactionFactory.createPurchase(share, getCurrentWeek());
    return player.getMoney().compareTo(purchase.getCalculator().calculateTotal()) >= 0;
  }

  /**
   * Returns whether a sell is currently valid.
   *
   * @param symbol stock symbol
   * @param quantity share quantity
   * @return true if sell is valid
   */
  public boolean canSell(String symbol, int quantity) {
    if (symbol == null || symbol.isBlank() || quantity <= 0) {
      return false;
    }
    BigDecimal targetQuantity = BigDecimal.valueOf(quantity);
    try {
      getMatchingOwnedShare(symbol, targetQuantity);
      return true;
    } catch (IllegalStateException e) {
      return false;
    }
  }

  /**
   * Updates exchange prices for a new week.
   */
  public void advanceExchangeWeek() {
    exchange.advance();
  }

  /**
   * Recomputes derived values used by the UI.
   */
  public void refreshDerivedState() {
    getNetWorth();
    getPlayerStatus();
  }

  /**
   * Notifies observers that model state changed.
   */
  public void publishModelChanged() {
    notifyObservers();
  }

  @Override
  public void addObserver(ModelObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException("Observer cannot be null");
    }
    observers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException("Observer cannot be null");
    }
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (ModelObserver observer : observers) {
      observer.onModelChanged();
    }
  }

  private Share getMatchingOwnedShare(String symbol, BigDecimal quantity) {
    return player.getPortfolio().getShares(symbol).stream()
        .filter(share -> share.getQuantity().compareTo(quantity) == 0)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No matching owned share found to sell"));
  }
}
