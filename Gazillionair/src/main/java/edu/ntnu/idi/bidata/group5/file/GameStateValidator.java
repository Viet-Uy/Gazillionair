package edu.ntnu.idi.bidata.group5.file;

import java.math.BigDecimal;

/**
 * Validates persisted game state DTO before mapping to domain objects.
 */
public class GameStateValidator {

  /**
   * Validates full game-state payload.
   *
   * @param data payload to validate
   */
  public void validate(GameStateData data) {
    if (data == null) {
      throw new IllegalArgumentException("Game state cannot be null");
    }
    if (data.playerName == null || data.playerName.isBlank()) {
      throw new IllegalArgumentException("Player name cannot be null or blank");
    }
    if (data.startingCapital == null || data.startingCapital.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Starting capital cannot be null or negative");
    }
    if (data.cashBalance == null || data.cashBalance.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Cash balance cannot be null or negative");
    }
    if (data.currentWeek < 1) {
      throw new IllegalArgumentException("Week must be at least 1");
    }
    if (data.stocks == null || data.holdings == null || data.transactions == null
        || data.news == null) {
      throw new IllegalArgumentException("State lists cannot be null");
    }

    for (GameStateData.StockData stockData : data.stocks) {
      validateStock(stockData);
    }
    for (GameStateData.ShareData shareData : data.holdings) {
      validateShare(shareData);
    }
    for (GameStateData.TransactionData transactionData : data.transactions) {
      validateTransaction(transactionData);
    }
    for (GameStateData.NewsData newsData : data.news) {
      validateNews(newsData);
    }
  }

  private void validateStock(GameStateData.StockData stockData) {
    if (stockData == null) {
      throw new IllegalArgumentException("Stock data cannot be null");
    }
    if (stockData.symbol == null || stockData.symbol.isBlank()) {
      throw new IllegalArgumentException("Stock symbol cannot be null or blank");
    }
    if (stockData.company == null || stockData.company.isBlank()) {
      throw new IllegalArgumentException("Stock company cannot be null or blank");
    }
    if (stockData.prices == null || stockData.prices.isEmpty()) {
      throw new IllegalArgumentException("Stock prices cannot be null or empty");
    }
  }

  private void validateShare(GameStateData.ShareData shareData) {
    if (shareData == null) {
      throw new IllegalArgumentException("Share data cannot be null");
    }
    if (shareData.symbol == null || shareData.symbol.isBlank()) {
      throw new IllegalArgumentException("Share symbol cannot be null or blank");
    }
    if (shareData.quantity == null || shareData.quantity.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Share quantity must be greater than zero");
    }
    if (shareData.purchasePrice == null
        || shareData.purchasePrice.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Share purchase price must be greater than zero");
    }
  }

  private void validateTransaction(GameStateData.TransactionData transactionData) {
    if (transactionData == null) {
      throw new IllegalArgumentException("Transaction data cannot be null");
    }
    if (transactionData.type == null || transactionData.type.isBlank()) {
      throw new IllegalArgumentException("Transaction type cannot be null or blank");
    }
    if (transactionData.week < 1) {
      throw new IllegalArgumentException("Transaction week must be at least 1");
    }
    GameStateData.ShareData shareData = new GameStateData.ShareData();
    shareData.symbol = transactionData.symbol;
    shareData.quantity = transactionData.quantity;
    shareData.purchasePrice = transactionData.purchasePrice;
    validateShare(shareData);
  }

  private void validateNews(GameStateData.NewsData newsData) {
    if (newsData == null) {
      throw new IllegalArgumentException("News data cannot be null");
    }
    if (newsData.headline == null || newsData.headline.isBlank()) {
      throw new IllegalArgumentException("News headline cannot be null or blank");
    }
    if (newsData.content == null || newsData.content.isBlank()) {
      throw new IllegalArgumentException("News content cannot be null or blank");
    }
    if (newsData.affectedStocks == null || newsData.affectedStocks.isBlank()) {
      throw new IllegalArgumentException("News affected stocks cannot be null or blank");
    }
    if (newsData.week < 1) {
      throw new IllegalArgumentException("News week must be at least 1");
    }
    if (newsData.sentiment == null || newsData.sentiment.isBlank()) {
      throw new IllegalArgumentException("News sentiment cannot be null or blank");
    }
  }
}
