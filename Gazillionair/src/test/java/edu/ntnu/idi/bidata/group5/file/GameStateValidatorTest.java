package edu.ntnu.idi.bidata.group5.file;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for GameStateValidator.
 * Tests validation of game state data including player info, stocks, shares,
 * transactions, and news data.
 */
class GameStateValidatorTest {

  private GameStateValidator validator;
  private GameStateData validData;

  @BeforeEach
  void setUp() {
    validator = new GameStateValidator();
    validData = createValidGameStateData();
  }

  @Test
  void validateRejectsNullData() {
    assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
  }

  @Test
  void validateRejectsNullPlayerName() {
    GameStateData data = createValidGameStateData();
    data.playerName = null;
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsBlankPlayerName() {
    GameStateData data = createValidGameStateData();
    data.playerName = "   ";
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullStartingCapital() {
    GameStateData data = createValidGameStateData();
    data.startingCapital = null;
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsZeroStartingCapital() {
    GameStateData data = createValidGameStateData();
    data.startingCapital = BigDecimal.ZERO;
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNegativeStartingCapital() {
    GameStateData data = createValidGameStateData();
    data.startingCapital = new BigDecimal("-100");
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullCashBalance() {
    GameStateData data = createValidGameStateData();
    data.cashBalance = null;
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNegativeCashBalance() {
    GameStateData data = createValidGameStateData();
    data.cashBalance = new BigDecimal("-100");
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateAcceptsZeroCashBalance() {
    GameStateData data = createValidGameStateData();
    data.cashBalance = BigDecimal.ZERO;
    validator.validate(data);
  }

  @Test
  void validateRejectsZeroWeek() {
    GameStateData data = createValidGameStateData();
    data.currentWeek = 0;
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNegativeWeek() {
    GameStateData data = createValidGameStateData();
    data.currentWeek = -5;
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullStocks() {
    GameStateData data = createValidGameStateData();
    data.stocks = null;
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullHoldings() {
    GameStateData data = createValidGameStateData();
    data.holdings = null;
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullTransactions() {
    GameStateData data = createValidGameStateData();
    data.transactions = null;
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullNews() {
    GameStateData data = createValidGameStateData();
    data.news = null;
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullStockData() {
    GameStateData data = createValidGameStateData();
    data.stocks.add(null);
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullStockSymbol() {
    GameStateData data = createValidGameStateData();
    data.stocks.add(createStockWithNullSymbol());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsBlankStockSymbol() {
    GameStateData data = createValidGameStateData();
    data.stocks.add(createStockWithBlankSymbol());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullStockCompany() {
    GameStateData data = createValidGameStateData();
    data.stocks.add(createStockWithNullCompany());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullStockPrices() {
    GameStateData data = createValidGameStateData();
    data.stocks.add(createStockWithNullPrices());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsEmptyStockPrices() {
    GameStateData data = createValidGameStateData();
    data.stocks.add(createStockWithEmptyPrices());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullShareData() {
    GameStateData data = createValidGameStateData();
    data.holdings.add(null);
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullShareSymbol() {
    GameStateData data = createValidGameStateData();
    data.holdings.add(createShareWithNullSymbol());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullShareQuantity() {
    GameStateData data = createValidGameStateData();
    data.holdings.add(createShareWithNullQuantity());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsZeroShareQuantity() {
    GameStateData data = createValidGameStateData();
    data.holdings.add(createShareWithZeroQuantity());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullSharePurchasePrice() {
    GameStateData data = createValidGameStateData();
    data.holdings.add(createShareWithNullPrice());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsZeroSharePurchasePrice() {
    GameStateData data = createValidGameStateData();
    data.holdings.add(createShareWithZeroPrice());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullTransactionData() {
    GameStateData data = createValidGameStateData();
    data.transactions.add(null);
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullTransactionType() {
    GameStateData data = createValidGameStateData();
    data.transactions.add(createTransactionWithNullType());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsZeroTransactionWeek() {
    GameStateData data = createValidGameStateData();
    data.transactions.add(createTransactionWithZeroWeek());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullNewsData() {
    GameStateData data = createValidGameStateData();
    data.news.add(null);
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullNewsHeadline() {
    GameStateData data = createValidGameStateData();
    data.news.add(createNewsWithNullHeadline());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullNewsContent() {
    GameStateData data = createValidGameStateData();
    data.news.add(createNewsWithNullContent());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullNewsAffectedStocks() {
    GameStateData data = createValidGameStateData();
    data.news.add(createNewsWithNullStocks());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsZeroNewsWeek() {
    GameStateData data = createValidGameStateData();
    data.news.add(createNewsWithZeroWeek());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateRejectsNullNewsSentiment() {
    GameStateData data = createValidGameStateData();
    data.news.add(createNewsWithNullSentiment());
    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }

  @Test
  void validateAcceptsValidData() {
    validator.validate(validData);
  }

  private GameStateData createValidGameStateData() {
    GameStateData data = new GameStateData();
    data.playerName = "TestPlayer";
    data.startingCapital = new BigDecimal("10000");
    data.cashBalance = new BigDecimal("5000");
    data.currentWeek = 1;

    GameStateData.StockData stock = new GameStateData.StockData();
    stock.symbol = "TEST";
    stock.company = "Test Company";
    stock.prices = new ArrayList<>();
    stock.prices.add(new BigDecimal("100"));
    data.stocks = new ArrayList<>();
    data.stocks.add(stock);

    data.holdings = new ArrayList<>();
    data.transactions = new ArrayList<>();
    data.news = new ArrayList<>();

    return data;
  }

  private GameStateData.StockData createStockWithNullSymbol() {
    GameStateData.StockData stock = new GameStateData.StockData();
    stock.symbol = null;
    stock.company = "Test";
    stock.prices = new ArrayList<>();
    stock.prices.add(BigDecimal.TEN);
    return stock;
  }

  private GameStateData.StockData createStockWithBlankSymbol() {
    GameStateData.StockData stock = new GameStateData.StockData();
    stock.symbol = "   ";
    stock.company = "Test";
    stock.prices = new ArrayList<>();
    stock.prices.add(BigDecimal.TEN);
    return stock;
  }

  private GameStateData.StockData createStockWithNullCompany() {
    GameStateData.StockData stock = new GameStateData.StockData();
    stock.symbol = "TEST";
    stock.company = null;
    stock.prices = new ArrayList<>();
    stock.prices.add(BigDecimal.TEN);
    return stock;
  }

  private GameStateData.StockData createStockWithNullPrices() {
    GameStateData.StockData stock = new GameStateData.StockData();
    stock.symbol = "TEST";
    stock.company = "Test";
    stock.prices = null;
    return stock;
  }

  private GameStateData.StockData createStockWithEmptyPrices() {
    GameStateData.StockData stock = new GameStateData.StockData();
    stock.symbol = "TEST";
    stock.company = "Test";
    stock.prices = new ArrayList<>();
    return stock;
  }

  private GameStateData.ShareData createShareWithNullSymbol() {
    GameStateData.ShareData share = new GameStateData.ShareData();
    share.symbol = null;
    share.quantity = BigDecimal.ONE;
    share.purchasePrice = BigDecimal.TEN;
    return share;
  }

  private GameStateData.ShareData createShareWithNullQuantity() {
    GameStateData.ShareData share = new GameStateData.ShareData();
    share.symbol = "TEST";
    share.quantity = null;
    share.purchasePrice = BigDecimal.TEN;
    return share;
  }

  private GameStateData.ShareData createShareWithZeroQuantity() {
    GameStateData.ShareData share = new GameStateData.ShareData();
    share.symbol = "TEST";
    share.quantity = BigDecimal.ZERO;
    share.purchasePrice = BigDecimal.TEN;
    return share;
  }

  private GameStateData.ShareData createShareWithNullPrice() {
    GameStateData.ShareData share = new GameStateData.ShareData();
    share.symbol = "TEST";
    share.quantity = BigDecimal.ONE;
    share.purchasePrice = null;
    return share;
  }

  private GameStateData.ShareData createShareWithZeroPrice() {
    GameStateData.ShareData share = new GameStateData.ShareData();
    share.symbol = "TEST";
    share.quantity = BigDecimal.ONE;
    share.purchasePrice = BigDecimal.ZERO;
    return share;
  }

  private GameStateData.TransactionData createTransactionWithNullType() {
    GameStateData.TransactionData transaction = new GameStateData.TransactionData();
    transaction.type = null;
    transaction.week = 1;
    transaction.symbol = "TEST";
    transaction.quantity = BigDecimal.ONE;
    transaction.purchasePrice = BigDecimal.TEN;
    return transaction;
  }

  private GameStateData.TransactionData createTransactionWithZeroWeek() {
    GameStateData.TransactionData transaction = new GameStateData.TransactionData();
    transaction.type = "PURCHASE";
    transaction.week = 0;
    transaction.symbol = "TEST";
    transaction.quantity = BigDecimal.ONE;
    transaction.purchasePrice = BigDecimal.TEN;
    return transaction;
  }

  private GameStateData.NewsData createNewsWithNullHeadline() {
    GameStateData.NewsData news = new GameStateData.NewsData();
    news.headline = null;
    news.content = "Test";
    news.affectedStocks = "TEST";
    news.week = 1;
    news.sentiment = "POSITIVE";
    return news;
  }

  private GameStateData.NewsData createNewsWithNullContent() {
    GameStateData.NewsData news = new GameStateData.NewsData();
    news.headline = "Test";
    news.content = null;
    news.affectedStocks = "TEST";
    news.week = 1;
    news.sentiment = "POSITIVE";
    return news;
  }

  private GameStateData.NewsData createNewsWithNullStocks() {
    GameStateData.NewsData news = new GameStateData.NewsData();
    news.headline = "Test";
    news.content = "Test";
    news.affectedStocks = null;
    news.week = 1;
    news.sentiment = "POSITIVE";
    return news;
  }

  private GameStateData.NewsData createNewsWithZeroWeek() {
    GameStateData.NewsData news = new GameStateData.NewsData();
    news.headline = "Test";
    news.content = "Test";
    news.affectedStocks = "TEST";
    news.week = 0;
    news.sentiment = "POSITIVE";
    return news;
  }

  private GameStateData.NewsData createNewsWithNullSentiment() {
    GameStateData.NewsData news = new GameStateData.NewsData();
    news.headline = "Test";
    news.content = "Test";
    news.affectedStocks = "TEST";
    news.week = 1;
    news.sentiment = null;
    return news;
  }
}
