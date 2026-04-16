package edu.ntnu.idi.bidata.group5.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.PlayerStatus;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DashboardControllerTest {

  private DashboardController controller;
  private GameSession session;

  @BeforeEach
  void setUp() {
    Stock apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
    session = new GameSession("Uy", new BigDecimal("1000"), List.of(apple));
    controller = new DashboardController(session);
  }

  @Test
  void constructorRejectsNullSession() {
    assertThrows(IllegalArgumentException.class, () -> new DashboardController(null));
  }

  @Test
  void getNetWorthAndStatusReturnSessionValues() {
    assertEquals(session.getNetWorth(), controller.getNetWorth());
    assertEquals(PlayerStatus.NOVICE, controller.getStatus());
  }

  @Test
  void nextWeekAdvancesSession() {
    int before = session.getCurrentWeek();

    controller.nextWeek();

    assertEquals(before + 1, session.getCurrentWeek());
  }
}

