package edu.ntnu.idi.bidata.group5.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameSessionTest {

  private List<Stock> stocks;

  @BeforeEach
  void setUp() {
    Stock apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
    Stock tesla = new Stock("TSLA", "Tesla", new BigDecimal("200"));
    stocks = List.of(apple, tesla);
  }

  @Test
  void constructorRejectsInvalidInput() {
    assertThrows(IllegalArgumentException.class,
        () -> new GameSession(null, BigDecimal.TEN, stocks));
    assertThrows(IllegalArgumentException.class,
        () -> new GameSession(" ", BigDecimal.TEN, stocks));
    assertThrows(IllegalArgumentException.class, () -> new GameSession("Uy", null, stocks));
    assertThrows(IllegalArgumentException.class, () -> new GameSession("Uy", BigDecimal.TEN, null));
  }

  @Test
  void buyCommitsAndNotifiesObserver() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);
    AtomicInteger notifications = new AtomicInteger(0);
    session.addObserver(notifications::incrementAndGet);

    Purchase purchase = session.buy("AAPL", 2);

    assertTrue(purchase.isCommitted());
    assertEquals(1, session.getTransactions().size());
    assertEquals(1, session.getPlayer().getPortfolio().getShares().size());
    assertEquals(1, notifications.get());
  }

  @Test
  void buyRejectsInvalidInput() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);

    assertThrows(IllegalArgumentException.class, () -> session.buy(null, 1));
    assertThrows(IllegalArgumentException.class, () -> session.buy(" ", 1));
    assertThrows(IllegalArgumentException.class, () -> session.buy("AAPL", 0));
    assertThrows(IllegalArgumentException.class, () -> session.buy("AAPL", -1));
    assertThrows(IllegalArgumentException.class, () -> session.buy("UNKNOWN", 1));
  }

  @Test
  void sellCommitsAndNotifiesObserver() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);
    session.buy("AAPL", 1);
    AtomicInteger notifications = new AtomicInteger(0);
    session.addObserver(notifications::incrementAndGet);

    Sale sale = session.sell("AAPL", 1);

    assertTrue(sale.isCommitted());
    assertEquals(2, session.getTransactions().size());
    assertTrue(session.getPlayer().getPortfolio().getShares().isEmpty());
    assertEquals(1, notifications.get());
  }

  @Test
  void sellAllowsPartialQuantityFromSingleBundle() {
    GameSession session = new GameSession("Uy", new BigDecimal("10000"), stocks);
    session.buy("AAPL", 10);

    Sale sale = session.sell("AAPL", 2);

    assertTrue(sale.isCommitted());
    assertEquals(2, session.getTransactions().size());
    assertEquals(1, session.getHoldings().size());
    assertEquals(0,
        session.getHoldings().getFirst().getQuantity().compareTo(new BigDecimal("8")));
  }

  @Test
  void sellCanConsumeAcrossMultipleBundlesOfSameStock() {
    GameSession session = new GameSession("Uy", new BigDecimal("10000"), stocks);
    session.buy("AAPL", 10);
    session.buy("AAPL", 2);

    assertTrue(session.canSell("AAPL", 12));
    Sale sale = session.sell("AAPL", 12);

    assertTrue(sale.isCommitted());
    assertTrue(session.getHoldings().isEmpty());
  }

  @Test
  void sellRejectsInvalidInputAndMissingShare() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);

    assertThrows(IllegalArgumentException.class, () -> session.sell(null, 1));
    assertThrows(IllegalArgumentException.class, () -> session.sell(" ", 1));
    assertThrows(IllegalArgumentException.class, () -> session.sell("AAPL", 0));
    assertThrows(IllegalArgumentException.class, () -> session.sell("AAPL", -1));
    assertThrows(IllegalStateException.class, () -> session.sell("AAPL", 1));
  }

  @Test
  void nextWeekNotifiesObserverAndChangesWeek() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);
    AtomicInteger notifications = new AtomicInteger(0);
    session.addObserver(notifications::incrementAndGet);
    final int before = session.getCurrentWeek();

    session.nextWeek();

    assertEquals(before + 1, session.getCurrentWeek());
    assertEquals(1, notifications.get());
  }

  @Test
  void canBuyAndCanSellReturnExpectedValues() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);

    assertTrue(session.canBuy("AAPL", 1));
    assertFalse(session.canBuy("UNKNOWN", 1));
    assertFalse(session.canBuy("AAPL", 0));

    assertFalse(session.canSell("AAPL", 1));
    session.buy("AAPL", 1);
    assertTrue(session.canSell("AAPL", 1));
    assertFalse(session.canSell("AAPL", 2));
  }

  @Test
  void holdingsAndBalancesAreExposedForTabs() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);
    session.buy("AAPL", 1);

    assertTrue(session.hasHoldings());
    assertEquals(1, session.getHoldings().size());
    assertEquals(0, new BigDecimal("899.50").compareTo(session.getCashBalance()));
    assertEquals(0, new BigDecimal("99.00").compareTo(session.getPortfolioValue()));
    assertEquals(0, new BigDecimal("998.50").compareTo(session.getNetWorth()));
  }

  @Test
  void stockDetailsAndPriceHistoryWork() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);
    assertNotNull(session.getStock("AAPL"));
    assertEquals(1, session.getStockPriceHistory("AAPL").size());
    session.nextWeek();
    assertEquals(2, session.getStockPriceHistory("AAPL").size());
    assertThrows(IllegalArgumentException.class, () -> session.getStock("UNKNOWN"));
    assertThrows(IllegalArgumentException.class, () -> session.getStockPriceHistory(null));
  }

  @Test
  void transactionQueriesFilterByTypeAndWeek() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);
    session.buy("AAPL", 1);
    session.nextWeek();
    session.buy("AAPL", 1);
    session.sell("AAPL", 1);

    assertEquals(3, session.getTransactions().size());
    assertEquals(2, session.getPurchases().size());
    assertEquals(1, session.getSales().size());
    assertEquals(1, session.getTransactionsForWeek(1).size());
    assertEquals(2, session.getTransactionsForWeek(2).size());
    assertThrows(IllegalArgumentException.class, () -> session.getTransactionsForWeek(0));
  }

  @Test
  void sellAllHoldingsSellsEverythingAndNotifies() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);
    session.buy("AAPL", 1);
    session.buy("TSLA", 1);
    AtomicInteger notifications = new AtomicInteger(0);
    session.addObserver(notifications::incrementAndGet);

    List<Sale> sales = session.sellAllHoldings();

    assertEquals(2, sales.size());
    assertTrue(session.getHoldings().isEmpty());
    assertEquals(1, notifications.get());
    assertTrue(sales.stream().allMatch(Sale::isCommitted));
  }

  @Test
  void sellAllHoldingsReturnsEmptyWhenNoPositions() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);
    AtomicInteger notifications = new AtomicInteger(0);
    session.addObserver(notifications::incrementAndGet);

    List<Sale> sales = session.sellAllHoldings();

    assertTrue(sales.isEmpty());
    assertEquals(0, notifications.get());
  }

  @Test
  void observerRegistrationRejectsNull() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);

    assertThrows(IllegalArgumentException.class, () -> session.addObserver(null));
    assertThrows(IllegalArgumentException.class, () -> session.removeObserver(null));
  }

  @Test
  void explicitObserverPublishingAndExchangeAdvanceWork() {
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);
    AtomicInteger notifications = new AtomicInteger(0);
    session.addObserver(notifications::incrementAndGet);
    final int before = session.getCurrentWeek();

    session.advanceExchangeWeek();
    session.refreshDerivedState();
    session.publishModelChanged();

    assertEquals(before + 1, session.getCurrentWeek());
    assertEquals(1, notifications.get());
  }
}

