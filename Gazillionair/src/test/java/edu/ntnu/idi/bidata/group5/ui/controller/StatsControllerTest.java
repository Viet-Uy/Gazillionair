package edu.ntnu.idi.bidata.group5.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatsControllerTest {

  private StatsController controller;
  private GameSession session;

  @BeforeEach
  void setUp() {
    Stock apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
    Stock tesla = new Stock("TSLA", "Tesla", new BigDecimal("200"));
    Stock nvidia = new Stock("NVDA", "Nvidia", new BigDecimal("300"));
    session = new GameSession("Uy", new BigDecimal("1000"), List.of(apple, tesla, nvidia));
    controller = new StatsController(session);
  }

  @Test
  void constructorRejectsNullSession() {
    assertThrows(IllegalArgumentException.class, () -> new StatsController(null));
  }

  @Test
  void returnsGainersAndLosers() {
    session.nextWeek();
    assertEquals(2, controller.getTopGainers(2).size());
    assertEquals(2, controller.getTopLosers(2).size());
  }

  @Test
  void gainersAndLosersRejectInvalidLimit() {
    assertThrows(IllegalArgumentException.class, () -> controller.getTopGainers(0));
    assertThrows(IllegalArgumentException.class, () -> controller.getTopLosers(0));
  }

  @Test
  void returnsStockAndHistory() {
    session.nextWeek();
    assertNotNull(controller.getStock("AAPL"));
    assertEquals(2, controller.getStockPriceHistory("AAPL").size());
  }

  @Test
  void stockAndHistoryRejectInvalidSymbol() {
    assertThrows(IllegalArgumentException.class, () -> controller.getStock("UNKNOWN"));
    assertThrows(IllegalArgumentException.class, () -> controller.getStockPriceHistory(null));
  }

  @Test
  void reportsWhetherWeeklyChangesExist() {
    assertFalse(controller.hasWeeklyPriceChanges());
    session.nextWeek();
    assertTrue(controller.hasWeeklyPriceChanges());
  }
}

