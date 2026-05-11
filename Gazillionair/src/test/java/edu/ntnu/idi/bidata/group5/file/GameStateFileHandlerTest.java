package edu.ntnu.idi.bidata.group5.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameStateFileHandlerTest {

  @Test
  void writeAndReadRoundTripPreservesSessionState() throws IOException {
    GameStateFileHandler handler = new GameStateFileHandler();
    List<Stock> stocks = List.of(
        new Stock("AAPL", "Apple", new BigDecimal("100")),
        new Stock("TSLA", "Tesla", new BigDecimal("200")));
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), stocks);
    session.buy("AAPL", 2);
    session.nextWeek();
    session.sell("AAPL", 1);

    Path tempFile = Files.createTempFile("gazillionair-save-", ".json");
    try {
      handler.writeToFile(session, tempFile.toString());
      GameSession restored = handler.readFromFile(tempFile.toString());

      assertEquals(session.getPlayer().getName(), restored.getPlayer().getName());
      assertEquals(0, session.getPlayer().getStartingMoney().compareTo(
          restored.getPlayer().getStartingMoney()));
      assertEquals(0, session.getCashBalance().compareTo(restored.getCashBalance()));
      assertEquals(session.getCurrentWeek(), restored.getCurrentWeek());
      assertEquals(session.getHoldings().size(), restored.getHoldings().size());
      assertEquals(session.getTransactions().size(), restored.getTransactions().size());
      assertEquals(session.getAllNews().size(), restored.getAllNews().size());
      assertEquals(session.getStockPriceHistory("AAPL"), restored.getStockPriceHistory("AAPL"));
      assertEquals(session.getStockPriceHistory("TSLA"), restored.getStockPriceHistory("TSLA"));
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  @Test
  void readRejectsMissingFile() {
    GameStateFileHandler handler = new GameStateFileHandler();
    Path missingPath = Path.of("src", "test", "resources", "missing-save.json");

    assertThrows(IOException.class, () -> handler.readFromFile(missingPath.toString()));
  }
}
