package edu.ntnu.idi.bidata.group5.file;

import edu.ntnu.idi.bidata.group5.model.Stock;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StockFileHandlerTest {

  private final StockFileHandler handler = new StockFileHandler();
  private final String testFile = "src/test/resources/stocks_test.csv";

  @Test
  void readFromFileReturnsCorrectNumberOfStocks() throws IOException {
    List<Stock> stocks = handler.readFromFile(testFile);
    assertEquals(2, stocks.size());
  }

  @Test
  void readFromFileReturnsCorrectStockData() throws IOException {
    List<Stock> stocks = handler.readFromFile(testFile);
    Stock stock1 = stocks.getFirst();
    assertEquals("AAPL", stock1.getSymbol());
    assertEquals("Apple Inc.", stock1.getCompany());
    assertEquals(0, stock1.getSalesPrice().compareTo(new java.math.BigDecimal("276.43")));
  }

  @Test
  void readFromFileThrowsOnInvalidPath() {
    assertThrows(IOException.class, () -> handler.readFromFile("invalid_path.csv"));
  }

}
