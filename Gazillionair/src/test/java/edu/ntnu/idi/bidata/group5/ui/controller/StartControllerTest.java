package edu.ntnu.idi.bidata.group5.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import java.math.BigDecimal;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class StartControllerTest {

  @Test
  void startWithSampleDataCreatesSession() {
    StartController controller = new StartController();

    GameSession session = controller.startWithSampleData("Uy", new BigDecimal("100000"));

    assertNotNull(session);
    assertEquals("Uy", session.getPlayer().getName());
    assertEquals(1, session.getCurrentWeek());
  }

  @Test
  void startWithSampleDataRejectsInvalidPlayerData() {
    StartController controller = new StartController();

    assertThrows(IllegalArgumentException.class,
        () -> controller.startWithSampleData(null, new BigDecimal("1000")));
    assertThrows(IllegalArgumentException.class,
        () -> controller.startWithSampleData("Uy", null));
  }

  @Test
  void startNewGameCreatesSessionFromFile() {
    StartController controller = new StartController();
    Path filePath = Path.of("src", "test", "resources", "stocks_test.csv");

    GameSession session = controller.startNewGame("Uy", new BigDecimal("50000"), filePath);

    assertNotNull(session);
    assertEquals("Uy", session.getPlayer().getName());
    assertEquals(2, session.getMarketStocks().size());
  }

  @Test
  void startNewGameRejectsInvalidInput() {
    StartController controller = new StartController();

    assertThrows(IllegalArgumentException.class,
        () -> controller.startNewGame("Uy", new BigDecimal("1000"), null));

    Path missingFile = Path.of("src", "test", "resources", "missing.csv");
    assertThrows(IllegalStateException.class,
        () -> controller.startNewGame("Uy", new BigDecimal("1000"), missingFile));

    Path malformedFile = Path.of("src", "test", "resources", "stocks_malformed.csv");
    assertThrows(IllegalStateException.class,
        () -> controller.startNewGame("Uy", new BigDecimal("1000"), malformedFile));
  }
}

