package edu.ntnu.idi.bidata.group5.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameEngineTest {

  private GameEngine gameEngine;
  private GameSession session;
  private Stock apple;
  private Stock tesla;

  @BeforeEach
  void setUp() {
    gameEngine = new GameEngine();
    apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
    tesla = new Stock("TSLA", "Tesla", new BigDecimal("200"));
    session = new GameSession("TestPlayer", new BigDecimal("1000"), List.of(apple, tesla));
  }

  // Positive tests - Happy path
  @Test
  void next_week_advances_week() {
    int initialWeek = session.getCurrentWeek();

    gameEngine.nextWeek(session);

    assertEquals(initialWeek + 1, session.getCurrentWeek(),
        "Week should be incremented by 1");
  }

  @Test
  void next_week_notifies_observers() {
    AtomicInteger notificationCount = new AtomicInteger(0);
    session.addObserver(notificationCount::incrementAndGet);

    gameEngine.nextWeek(session);

    assertEquals(1, notificationCount.get(), "Observer should be notified once");
  }

  @Test
  void next_week_updates_stock_prices() {
    BigDecimal appleInitialPrice = apple.getSalesPrice();
    BigDecimal teslaInitialPrice = tesla.getSalesPrice();

    gameEngine.nextWeek(session);

    // Prices should have changed (or stayed the same, but price history should grow)
    assertEquals(2, apple.getHistoricalPrices().size(),
        "Apple should have 2 price entries after advance");
    assertEquals(2, tesla.getHistoricalPrices().size(),
        "Tesla should have 2 price entries after advance");
  }

  @Test
  void advance_week_delegates_to_next_week() {
    int initialWeek = session.getCurrentWeek();

    gameEngine.advanceWeek(session);

    assertEquals(initialWeek + 1, session.getCurrentWeek(),
        "advanceWeek should delegate to nextWeek");
  }

  @Test
  void next_week_multiple_times() {
    final int initialWeek = session.getCurrentWeek();

    gameEngine.nextWeek(session);
    gameEngine.nextWeek(session);
    gameEngine.nextWeek(session);

    assertEquals(initialWeek + 3, session.getCurrentWeek(),
        "Multiple advances should increment week correctly");
  }

  @Test
  void next_week_multiple_times_notifies_each_time() {
    AtomicInteger notificationCount = new AtomicInteger(0);
    session.addObserver(notificationCount::incrementAndGet);

    gameEngine.nextWeek(session);
    gameEngine.nextWeek(session);
    gameEngine.nextWeek(session);

    assertEquals(3, notificationCount.get(),
        "Observer should be notified 3 times");
  }

  @Test
  void apply_market_changes_with_valid_parameters() {
    assertDoesNotThrow(
        () -> gameEngine.applyMarketChanges(List.of(apple, tesla), 1),
        "Should accept valid parameters");
  }

  @Test
  void apply_market_changes_with_empty_stock_list() {
    assertDoesNotThrow(
        () -> gameEngine.applyMarketChanges(List.of(), 1),
        "Should accept empty stock list");
  }

  @Test
  void apply_market_changes_with_week_one() {
    assertDoesNotThrow(
        () -> gameEngine.applyMarketChanges(List.of(apple), 1),
        "Should accept week 1");
  }

  @Test
  void apply_market_changes_with_large_week_number() {
    assertDoesNotThrow(
        () -> gameEngine.applyMarketChanges(List.of(apple), 100),
        "Should accept large week numbers");
  }

  // Negative tests - Error cases
  @Test
  void next_week_with_null_session_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> gameEngine.nextWeek(null),
        "nextWeek with null session should throw IllegalArgumentException");
  }

  @Test
  void advance_week_with_null_session_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> gameEngine.advanceWeek(null),
        "advanceWeek with null session should throw IllegalArgumentException");
  }

  @Test
  void apply_market_changes_with_null_stocks_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> gameEngine.applyMarketChanges(null, 1),
        "applyMarketChanges with null stocks should throw exception");
  }

  @Test
  void apply_market_changes_with_week_zero_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> gameEngine.applyMarketChanges(List.of(apple), 0),
        "applyMarketChanges with week 0 should throw exception");
  }

  @Test
  void apply_market_changes_with_negative_week_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> gameEngine.applyMarketChanges(List.of(apple), -1),
        "applyMarketChanges with negative week should throw exception");
  }

  // Boundary tests - Edge cases
  @Test
  void next_week_at_week_boundary() {
    int initialWeek = session.getCurrentWeek();
    assertEquals(1, initialWeek, "Session should start at week 1");

    gameEngine.nextWeek(session);

    assertEquals(2, session.getCurrentWeek(), "Should advance to week 2");
  }

  @Test
  void next_week_many_times() {
    for (int i = 0; i < 50; i++) {
      gameEngine.nextWeek(session);
    }

    assertEquals(51, session.getCurrentWeek(),
        "Should be at week 51 after 50 advances from week 1");
  }

  @Test
  void apply_market_changes_boundary_week_one() {
    assertDoesNotThrow(
        () -> gameEngine.applyMarketChanges(List.of(apple, tesla), 1),
        "Week 1 is minimum valid week");
  }

  @Test
  void apply_market_changes_boundary_very_large_week() {
    assertDoesNotThrow(
        () -> gameEngine.applyMarketChanges(List.of(apple), Integer.MAX_VALUE),
        "Should accept very large week numbers");
  }

  @Test
  void next_week_multiple_observers() {
    AtomicInteger observer1Count = new AtomicInteger(0);
    AtomicInteger observer2Count = new AtomicInteger(0);

    session.addObserver(observer1Count::incrementAndGet);
    session.addObserver(observer2Count::incrementAndGet);

    gameEngine.nextWeek(session);

    assertEquals(1, observer1Count.get(), "First observer should be notified");
    assertEquals(1, observer2Count.get(), "Second observer should be notified");
  }
}

