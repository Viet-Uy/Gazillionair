package edu.ntnu.idi.bidata.group5.service;

import java.util.Arrays;

import edu.ntnu.idi.bidata.group5.model.Player;
import edu.ntnu.idi.bidata.group5.model.Share;
import edu.ntnu.idi.bidata.group5.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeTest {

  private Stock apple;
  private Stock tesla;
  private Exchange exchange;

  @BeforeEach
  void setUp() {
    apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
    tesla = new Stock("TSLA", "Tesla", new BigDecimal("200"));
    exchange = new Exchange("TechExchange", List.of(apple, tesla));
  }

  //tests for constructor
  @Test
  void constructorInitializeFieldsCorrectly() {
    assertEquals("TechExchange", exchange.getName());
    assertEquals(1, exchange.getWeek());
    assertEquals(apple, exchange.getStock("AAPL"));
    assertEquals(tesla, exchange.getStock("TSLA"));
  }

  @Test
  void duplicateStockSymbol() {
    assertThrows(IllegalArgumentException.class, () -> new Exchange(
            "DuplicateExchange",
            List.of(apple, tesla, new Stock("AAPL", "Apple2", new BigDecimal("150")))
    ));
  }

  @Test
  void constructorThrowsExceptionWithNullName() {
    assertThrows(IllegalArgumentException.class, () -> new Exchange(null, List.of(apple, tesla)));
  }

  @Test
  void constructorThrowsExceptionWithBlankName() {
    assertThrows(IllegalArgumentException.class, () -> new Exchange("   ", List.of(apple, tesla)));
  }

  @Test
  void constructorThrowsOnNullStocks() {
    assertThrows(IllegalArgumentException.class, () -> new Exchange("NullStocksExchange", null));
  }

  @Test
  void constructorThrowsOnNullStockInList() {
    assertThrows(IllegalArgumentException.class, () -> new Exchange("BadExchange", Arrays.asList(apple, null)));
  }

  //tests for getStock
  @Test
  void getStockReturnCorrectStock() {
    Stock result = exchange.getStock("TSLA");
    assertEquals(tesla, result);
  }

  @Test
  void getStockThrowsOnUnknownSymbol() {
    assertThrows(IllegalArgumentException.class, () -> exchange.getStock("UNKNOWN"));
  }

  @Test
  void getStockThrowsOnNullSymbol() {
    assertThrows(IllegalArgumentException.class, () -> exchange.getStock(null));
  }

  @Test
  void getStockThrowsOnBlankSymbol() {
    assertThrows(IllegalArgumentException.class, () -> exchange.getStock("  "));
  }

  @Test
  void findStocksReturnCorrectStock() {
    List<Stock> result = exchange.findStocks("AAPL");
    assertEquals(1, result.size());
    assertEquals(apple, result.getFirst());
  }

  //tests for findStocks
  @Test
  void findStocksIsCaseInsensitive() {
    List<Stock> result = exchange.findStocks("apple");
    assertEquals(1, result.size());
    assertEquals(apple, result.getFirst());
  }

  @Test
  void findStocksReturnsEmptyListForNoMatches() {
    List<Stock> result = exchange.findStocks("NONEXISTENT");
    assertTrue(result.isEmpty());
  }

  @Test
  void findStocksReturnsEmptyListForNullKeyword() {
    List<Stock> result = exchange.findStocks(null);
    assertTrue(result.isEmpty());
  }

  @Test
  void findStocksReturnsEmptyListForBlankKeyword() {
    List<Stock> result = exchange.findStocks("   ");
    assertTrue(result.isEmpty());
  }

  //test for advance
  @Test
  void advanceIncreaseWeekNumber() {
    exchange.advance();
    assertEquals(2, exchange.getWeek());
  }

  @Test
  void advanceUpdateStockPrices() {
    exchange.advance();
    assertNotNull(apple.getSalesPrice());
    assertNotNull(tesla.getSalesPrice());
    assertEquals(2, exchange.getWeek());
  }

  @Test
  void advanceKeepsPricesNonNegative() {
    exchange.advance();
    assertTrue(apple.getSalesPrice().compareTo(new BigDecimal("0.01")) >= 0);
    assertTrue(tesla.getSalesPrice().compareTo(new BigDecimal("0.01")) >= 0);
  }

  //tests for buy
  @Test
  void buyShouldReduceMoneyAddShareAndArchiveTransaction() {
    Player player = new Player("Player1", new BigDecimal("1000"));

    exchange.buy(player, "AAPL", new BigDecimal("2")); // 2 * 100 = 200

    assertEquals(0, player.getMoney().compareTo(new BigDecimal("799")));
    assertEquals(1, player.getPortfolio().getShares().size());
    assertFalse(player.getTransactionArchive().isEmpty());
    assertEquals(1, player.getTransactionArchive().getPurchases().size());
  }

  @Test
  void buyThrowsWhenNotEnoughMoney() {
    Player poorPlayer = new Player("Poor", new BigDecimal("50"));

    assertThrows(IllegalStateException.class,
            () -> exchange.buy(poorPlayer, "AAPL", new BigDecimal("1")));
  }

  @Test
  void buyThrowsOnNullPlayer() {
    assertThrows(IllegalArgumentException.class,
            () -> exchange.buy(null, "AAPL", new BigDecimal("1")));
  }

  @Test
  void buyThrowsOnNullSymbol() {
    Player player = new Player("Player1", new BigDecimal("1000"));
    assertThrows(IllegalArgumentException.class,
            () -> exchange.buy(player, null, new BigDecimal("1")));
  }

  @Test
  void buyThrowsOnBlankSymbol() {
    Player player = new Player("Player1", new BigDecimal("1000"));
    assertThrows(IllegalArgumentException.class,
            () -> exchange.buy(player, "   ", new BigDecimal("1")));
  }

  @Test
  void buyThrowsOnNullQuantity() {
    Player player = new Player("Player1", new BigDecimal("1000"));
    assertThrows(IllegalArgumentException.class,
            () -> exchange.buy(player, "AAPL", null));
  }

  @Test
  void buyThrowsOnZeroOrNegativeQuantity() {
    Player player = new Player("Player1", new BigDecimal("1000"));

    assertThrows(IllegalArgumentException.class,
            () -> exchange.buy(player, "AAPL", BigDecimal.ZERO));

    assertThrows(IllegalArgumentException.class,
            () -> exchange.buy(player, "AAPL", new BigDecimal("-1")));
  }

  //tests for sell
  @Test
  void sellShouldIncreaseMoneyRemoveShareAndArchiveTransaction() {
    Player player = new Player("Player1", new BigDecimal("1000"));

    exchange.buy(player, "AAPL", new BigDecimal("1"));
    Share ownedShare = player.getPortfolio().getShares().getFirst();

    exchange.sell(player, ownedShare);

    assertEquals(0, player.getMoney().compareTo(new BigDecimal("998.50")));
    assertTrue(player.getPortfolio().getShares().isEmpty());
    assertEquals(1, player.getTransactionArchive().getSales().size());
  }

  @Test
  void sellThrowsIfPlayerDoesNotOwnShare() {
    Player player = new Player("Player1", new BigDecimal("1000"));
    Share fakeShare = new Share(apple, new BigDecimal("1"), apple.getSalesPrice());

    assertThrows(IllegalStateException.class, () -> exchange.sell(player, fakeShare));
  }

  @Test
  void sellThrowsOnNullPlayer() {
    Share share = new Share(apple, new BigDecimal("1"), apple.getSalesPrice());
    assertThrows(IllegalArgumentException.class, () -> exchange.sell(null, share));
  }

  @Test
  void sellThrowsOnNullShare() {
    Player player = new Player("Player1", new BigDecimal("1000"));
    assertThrows(IllegalArgumentException.class, () -> exchange.sell(player, null));
  }

  //tests for getGainers

  @Test
  void getGainersReturnsSortedByPriceChangeDescending() {
    Stock stock1 = new Stock("STOCK1", "Company1", new BigDecimal("100"));
    Stock stock2 = new Stock("STOCK2", "Company2", new BigDecimal("100"));
    Stock stock3 = new Stock("STOCK3", "Company3", new BigDecimal("100"));
    Exchange ex = new Exchange("TestExchange", List.of(stock1, stock2, stock3));

    stock1.addNewSalesPrice(new BigDecimal("110")); // +10
    stock2.addNewSalesPrice(new BigDecimal("120")); // +20
    stock3.addNewSalesPrice(new BigDecimal("105")); // +5

    List<Stock> gainers = ex.getGainers(3);
    assertEquals(3, gainers.size());
    assertEquals(stock2, gainers.get(0)); // +20 first
    assertEquals(stock1, gainers.get(1)); // +10 second
    assertEquals(stock3, gainers.get(2)); // +5 third
  }

  @Test
  void getGainersRespectsLimit() {
    Stock stock1 = new Stock("STOCK1", "Company1", new BigDecimal("100"));
    Stock stock2 = new Stock("STOCK2", "Company2", new BigDecimal("100"));
    Stock stock3 = new Stock("STOCK3", "Company3", new BigDecimal("100"));
    Exchange ex = new Exchange("TestExchange", List.of(stock1, stock2, stock3));

    stock1.addNewSalesPrice(new BigDecimal("110"));
    stock2.addNewSalesPrice(new BigDecimal("120"));
    stock3.addNewSalesPrice(new BigDecimal("105"));

    List<Stock> gainers = ex.getGainers(2);
    assertEquals(2, gainers.size());
    assertEquals(stock2, gainers.get(0));
    assertEquals(stock1, gainers.get(1));
  }

  @Test
  void getGainersWithOneStock() {
    List<Stock> gainers = exchange.getGainers(1);
    assertEquals(1, gainers.size()); // Only 1 stock returned due to limit
  }

  @Test
  void getGainersThrowsOnZeroLimit() {
    assertThrows(IllegalArgumentException.class, () -> exchange.getGainers(0));
  }

  @Test
  void getGainersThrowsOnNegativeLimit() {
    assertThrows(IllegalArgumentException.class, () -> exchange.getGainers(-1));
  }

  @Test
  void getGainersIncludesZeroAndNegativeChanges() {
    Stock stock1 = new Stock("STOCK1", "Company1", new BigDecimal("100"));
    Stock stock2 = new Stock("STOCK2", "Company2", new BigDecimal("100"));
    Exchange ex = new Exchange("TestExchange", List.of(stock1, stock2));

    stock1.addNewSalesPrice(new BigDecimal("100")); // 0 change
    stock2.addNewSalesPrice(new BigDecimal("90"));  // -10 change

    List<Stock> gainers = ex.getGainers(2);
    assertEquals(2, gainers.size());
    assertEquals(stock1, gainers.get(0)); // 0 > -10
    assertEquals(stock2, gainers.get(1));
  }

  // tests for getLosers

  @Test
  void getLosersReturnsSortedByPriceChangeAscending() {
    Stock stock1 = new Stock("STOCK1", "Company1", new BigDecimal("100"));
    Stock stock2 = new Stock("STOCK2", "Company2", new BigDecimal("100"));
    Stock stock3 = new Stock("STOCK3", "Company3", new BigDecimal("100"));
    Exchange ex = new Exchange("TestExchange", List.of(stock1, stock2, stock3));

    stock1.addNewSalesPrice(new BigDecimal("90")); // -10
    stock2.addNewSalesPrice(new BigDecimal("80")); // -20
    stock3.addNewSalesPrice(new BigDecimal("95")); // -5

    List<Stock> losers = ex.getLosers(3);
    assertEquals(3, losers.size());
    assertEquals(stock2, losers.get(0)); // -20 first
    assertEquals(stock1, losers.get(1)); // -10 second
    assertEquals(stock3, losers.get(2)); // -5 third
  }

  @Test
  void getLosersRespectsLimit() {
    Stock stock1 = new Stock("STOCK1", "Company1", new BigDecimal("100"));
    Stock stock2 = new Stock("STOCK2", "Company2", new BigDecimal("100"));
    Stock stock3 = new Stock("STOCK3", "Company3", new BigDecimal("100"));
    Exchange ex = new Exchange("TestExchange", List.of(stock1, stock2, stock3));

    stock1.addNewSalesPrice(new BigDecimal("90"));
    stock2.addNewSalesPrice(new BigDecimal("80"));
    stock3.addNewSalesPrice(new BigDecimal("95"));

    List<Stock> losers = ex.getLosers(2);
    assertEquals(2, losers.size());
    assertEquals(stock2, losers.get(0));
    assertEquals(stock1, losers.get(1));
  }

  @Test
  void getLosersThrowsOnZeroLimit() {
    assertThrows(IllegalArgumentException.class, () -> exchange.getLosers(0));
  }

  @Test
  void getLosersThrowsOnNegativeLimit() {
    assertThrows(IllegalArgumentException.class, () -> exchange.getLosers(-1));
  }

  @Test
  void getLosersIncludesZeroAndPositiveChanges() {
    Stock stock1 = new Stock("STOCK1", "Company1", new BigDecimal("100"));
    Stock stock2 = new Stock("STOCK2", "Company2", new BigDecimal("100"));
    Exchange ex = new Exchange("TestExchange", List.of(stock1, stock2));

    stock1.addNewSalesPrice(new BigDecimal("100")); // 0 change
    stock2.addNewSalesPrice(new BigDecimal("110")); // +10 change

    List<Stock> losers = ex.getLosers(2);
    assertEquals(2, losers.size());
    assertEquals(stock1, losers.get(0)); // 0 > +10
    assertEquals(stock2, losers.get(1));
  }
}