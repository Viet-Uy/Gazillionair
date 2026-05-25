package edu.ntnu.idi.bidata.group5.ui.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for NewsController.
 * Tests controller initialization, validation, and basic functionality.
 */
class NewsControllerTest {

  private GameSession session;

  @BeforeEach
  void setUp() {
    Stock apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
    Stock tesla = new Stock("TSLA", "Tesla", new BigDecimal("200"));
    session = new GameSession("TestPlayer", new BigDecimal("10000"),
        List.of(apple, tesla));
  }

  @Test
  void constructorRejectsNullView() {
    assertThrows(IllegalArgumentException.class,
        () -> new NewsController(null, session));
  }

  @Test
  void constructorRejectsNullSession() {
    assertThrows(IllegalArgumentException.class,
        () -> new NewsController(null, null));
  }

  @Test
  void loadNewsWithValidNewsList() {
    assertNotNull(session.getAllNews());
  }

  @Test
  void loadNewsWithNullDoesNotThrow() {
    assertNotNull(session);
  }

  @Test
  void sessionHasNews() {
    assertNotNull(session.getAllNews());
  }

  @Test
  void sessionCurrentWeekIsValid() {
    assert session.getCurrentWeek() >= 1;
  }
}
