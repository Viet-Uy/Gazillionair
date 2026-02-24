package edu.ntnu.idi.bidata.group5;

import java.util.Arrays;
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
    assertThrows(IllegalArgumentException.class, () -> new Exchange("NullStockInListExchange", Arrays.asList(apple, null)));
  }

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
}