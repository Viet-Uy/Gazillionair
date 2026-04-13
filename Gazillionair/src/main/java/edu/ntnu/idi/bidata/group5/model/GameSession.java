package edu.ntnu.idi.bidata.group5.model;

import edu.ntnu.idi.bidata.group5.model.observer.ModelObserver;
import edu.ntnu.idi.bidata.group5.model.observer.ObservableModel;
import edu.ntnu.idi.bidata.group5.service.Exchange;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GameSession implements ObservableModel {

  private final Player player;
  private final Exchange exchange;
  private final List<Stock> marketStocks;
  private final List<ModelObserver> observers;

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
  }

  public Player getPlayer() {
    return player;
  }

  public int getCurrentWeek() {
    return exchange.getWeek();
  }

  public List<Stock> getMarketStocks() {
    return new ArrayList<>(marketStocks);
  }

  public List<Transaction> getTransactions() {
    return player.getTransactionArchive().getTransactions();
  }

  public BigDecimal getNetWorth() {
    return player.getNetWorth();
  }

  public PlayerStatus getPlayerStatus() {
    return player.getStatus();
  }

  public List<Stock> searchStocks(String query) {
    if (query == null || query.isBlank()) {
      return getMarketStocks();
    }
    return exchange.findStocks(query);
  }

  public List<Stock> getTopGainers(int limit) {
    return exchange.getGainers(limit);
  }

  public List<Stock> getTopLosers(int limit) {
    return exchange.getLosers(limit);
  }

  public Purchase buy(String symbol, int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than zero");
    }
    Purchase purchase = exchange.buy(player, symbol, BigDecimal.valueOf(quantity));
    notifyObservers();
    return purchase;
  }

  public Sale sell(String symbol, int quantity) {
    if (symbol == null || symbol.isBlank()) {
      throw new IllegalArgumentException("Symbol cannot be null or blank");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than zero");
    }

    BigDecimal targetQuantity = BigDecimal.valueOf(quantity);
    Share shareToSell = player.getPortfolio().getShares(symbol).stream()
        .filter(share -> share.getQuantity().compareTo(targetQuantity) == 0)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No matching owned share found to sell"));

    Sale sale = exchange.sell(player, shareToSell);
    notifyObservers();
    return sale;
  }

  public void nextWeek() {
    exchange.advance();
    notifyObservers();
  }

  public boolean canBuy(String symbol, int quantity) {
    try {
      if (quantity <= 0) {
        return false;
      }
      Stock stock = exchange.getStock(symbol);
      Share share = new Share(stock, BigDecimal.valueOf(quantity), stock.getSalesPrice());
      Purchase purchase = new Purchase(share, getCurrentWeek());
      return player.getMoney().compareTo(purchase.getCalculator().calculateTotal()) >= 0;
    } catch (RuntimeException e) {
      return false;
    }
  }

  public boolean canSell(String symbol, int quantity) {
    if (symbol == null || symbol.isBlank() || quantity <= 0) {
      return false;
    }
    BigDecimal targetQuantity = BigDecimal.valueOf(quantity);
    return player.getPortfolio().getShares(symbol).stream()
        .anyMatch(share -> share.getQuantity().compareTo(targetQuantity) == 0);
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
}
