package edu.ntnu.idi.bidata.group5.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class StockTest {

  @Test
  void constructorStoresFieldsCorrectly() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    assertEquals("AAPL", stock.getSymbol());
    assertEquals("Apple", stock.getCompany());
    assertEquals(BigDecimal.valueOf(100), stock.getSalesPrice());
  }

  @Test
  void getSalesPriceReturnsMostRecentPrice() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    stock.addNewSalesPrice(BigDecimal.valueOf(120));
    stock.addNewSalesPrice(BigDecimal.valueOf(130));
    assertEquals(BigDecimal.valueOf(130), stock.getSalesPrice());
  }

  @Test
  void addNewSalesPriceReturnsUpdatedPrice() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    BigDecimal updated = stock.addNewSalesPrice(BigDecimal.valueOf(150));
    assertEquals(BigDecimal.valueOf(150), updated);
  }

  @Test
  void constructorThrowsExceptionForInvalidSymbol() {
    assertThrows(IllegalArgumentException.class,
        () -> new Stock(null, "Apple", BigDecimal.valueOf(100)));
    assertThrows(IllegalArgumentException.class,
        () -> new Stock("", "Apple", BigDecimal.valueOf(100)));
  }

  @Test
  void constructorThrowsExceptionForInvalidCompany() {
    assertThrows(IllegalArgumentException.class,
        () -> new Stock("AAPL", null, BigDecimal.valueOf(100)));
    assertThrows(IllegalArgumentException.class,
        () -> new Stock("AAPL", "", BigDecimal.valueOf(100)));
  }

  @Test
  void constructorThrowsExceptionForInvalidSalesPrice() {
    assertThrows(IllegalArgumentException.class, () -> new Stock("AAPL", "Apple", null));
    assertThrows(IllegalArgumentException.class,
        () -> new Stock("AAPL", "Apple", BigDecimal.valueOf(-1)));
    assertThrows(IllegalArgumentException.class,
        () -> new Stock("AAPL", "Apple", BigDecimal.ZERO));
  }

  @Test
  void addNewSalesPriceThrowsExceptionForInvalidPrice() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    assertThrows(IllegalArgumentException.class, () -> stock.addNewSalesPrice(null));
    assertThrows(IllegalArgumentException.class,
        () -> stock.addNewSalesPrice(BigDecimal.valueOf(-1)));
    assertThrows(IllegalArgumentException.class,
        () -> stock.addNewSalesPrice(BigDecimal.ZERO));
  }

  @Test
  void getHistoricalPricesReturnsAllPricesAndCopy() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    stock.addNewSalesPrice(BigDecimal.valueOf(120));
    stock.addNewSalesPrice(BigDecimal.valueOf(110));

    List<BigDecimal> prices = stock.getHistoricalPrices();
    assertEquals(3, prices.size());
    assertEquals(BigDecimal.valueOf(100), prices.get(0));
    assertEquals(BigDecimal.valueOf(120), prices.get(1));
    assertEquals(BigDecimal.valueOf(110), prices.get(2));

    prices.add(BigDecimal.valueOf(200));
    assertEquals(3, stock.getHistoricalPrices().size());
  }

  @Test
  void highestAndLowestPriceWork() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    stock.addNewSalesPrice(BigDecimal.valueOf(150));
    stock.addNewSalesPrice(BigDecimal.valueOf(50));
    stock.addNewSalesPrice(BigDecimal.valueOf(120));

    assertEquals(BigDecimal.valueOf(150), stock.getHighestPrice());
    assertEquals(BigDecimal.valueOf(50), stock.getLowestPrice());
  }

  @Test
  void getLatestPriceChangeWorksForPositiveAndNegative() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    assertEquals(BigDecimal.ZERO, stock.getLatestPriceChange());

    stock.addNewSalesPrice(BigDecimal.valueOf(150));
    assertEquals(BigDecimal.valueOf(50), stock.getLatestPriceChange());

    stock.addNewSalesPrice(BigDecimal.valueOf(120));
    assertEquals(BigDecimal.valueOf(-30), stock.getLatestPriceChange());
  }
}
