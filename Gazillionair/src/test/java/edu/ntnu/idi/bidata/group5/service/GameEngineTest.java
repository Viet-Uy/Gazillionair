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

  @BeforeEach
  void setUp() {
    gameEngine = new GameEngine();
    Stock apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
    Stock tesla = new Stock("TSLA", "Tesla", new BigDecimal("200"));
    session = new GameSession("Uy", new BigDecimal("1000"), List.of(apple, tesla));
  }

  @Test
  void nextWeekAdvancesAndNotifies() {
    int before = session.getCurrentWeek();
    AtomicInteger notifications = new AtomicInteger(0);
    session.addObserver(notifications::incrementAndGet);

    gameEngine.nextWeek(session);

    assertEquals(before + 1, session.getCurrentWeek());
    assertEquals(1, notifications.get());
  }

  @Test
  void advanceWeekDelegatesToNextWeek() {
    int before = session.getCurrentWeek();

    gameEngine.advanceWeek(session);

    assertEquals(before + 1, session.getCurrentWeek());
  }

  @Test
  void nextWeekRejectsNullSession() {
    assertThrows(IllegalArgumentException.class, () -> gameEngine.nextWeek(null));
    assertThrows(IllegalArgumentException.class, () -> gameEngine.advanceWeek(null));
  }

  @Test
  void applyMarketChangesValidation() {
    assertThrows(IllegalArgumentException.class, () -> gameEngine.applyMarketChanges(null, 1));
    assertThrows(IllegalArgumentException.class,
        () -> gameEngine.applyMarketChanges(List.of(), 0));
    assertThrows(IllegalArgumentException.class,
        () -> gameEngine.applyMarketChanges(List.of(), -1));
    assertDoesNotThrow(() -> gameEngine.applyMarketChanges(List.of(), 1));
  }
}

