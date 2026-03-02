package edu.ntnu.idi.bidata.group5.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

  //tests for constructor
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
  void getSymbolReturnsCorrectSymbol() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    assertEquals("AAPL", stock.getSymbol());
  }

  @Test
  void getCompanyReturnsCorrectCompany() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    assertEquals("Apple", stock.getCompany());
  }

  // Negative test cases for constructor

  @Test
  void constructorThrowsExceptionSymbol() {
    assertThrows(IllegalArgumentException.class, () -> new Stock(null, "Apple", BigDecimal.valueOf(100)));
    assertThrows(IllegalArgumentException.class, () -> new Stock("", "Apple", BigDecimal.valueOf(100)));
  }

  @Test
  void constructorThrowsExceptionCompany() {
    assertThrows(IllegalArgumentException.class, () -> new Stock("AAPL", null, BigDecimal.valueOf(100)));
    assertThrows(IllegalArgumentException.class, () -> new Stock("AAPL", "", BigDecimal.valueOf(100)));
  }

  @Test
  void constructorThrowsExceptionSalesPrice() {
    assertThrows(IllegalArgumentException.class, () -> new Stock("AAPL", "Apple", null));
    assertThrows(IllegalArgumentException.class, () -> new Stock("AAPL", "Apple", BigDecimal.valueOf(-1)));
    assertThrows(IllegalArgumentException.class, () -> new Stock("AAPL", "Apple", BigDecimal.ZERO));
  }

  @Test
  void addNewSalesPriceThrowsException() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    assertThrows(IllegalArgumentException.class, () -> stock.addNewSalesPrice(null));
    assertThrows(IllegalArgumentException.class, () -> stock.addNewSalesPrice(BigDecimal.valueOf(-1)));
    assertThrows(IllegalArgumentException.class, () -> stock.addNewSalesPrice(BigDecimal.ZERO));
  }

  //tests for getHistoricalPrices
  @Test
  void getHistoricalPricesReturnsAllHistoricalPrices() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    stock.addNewSalesPrice(BigDecimal.valueOf(120));
    stock.addNewSalesPrice(BigDecimal.valueOf(110));

    var prices = stock.getHistoricalPrices();
    assertEquals(3, prices.size());
    assertEquals(BigDecimal.valueOf(100), prices.get(0));
    assertEquals(BigDecimal.valueOf(120), prices.get(1));
    assertEquals(BigDecimal.valueOf(110), prices.get(2));
  }

  @Test
  void getHistoricalPricesReturnsCopy() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    var prices = stock.getHistoricalPrices();
    prices.add(BigDecimal.valueOf(200));

    // Internal list should not be modified
    assertEquals(1, stock.getHistoricalPrices().size());
  }

  @Test
  void getHistoricalPricesWithSinglePrice() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    var prices = stock.getHistoricalPrices();
    assertEquals(1, prices.size());
    assertEquals(BigDecimal.valueOf(100), prices.get(0));
  }

  //tests for getHighestPrice
  @Test
  void getHighestPriceReturnsMaximum() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    stock.addNewSalesPrice(BigDecimal.valueOf(150));
    stock.addNewSalesPrice(BigDecimal.valueOf(120));
    stock.addNewSalesPrice(BigDecimal.valueOf(180));

    assertEquals(BigDecimal.valueOf(180), stock.getHighestPrice());
  }

  @Test
  void getHighestPriceWithSinglePrice() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    assertEquals(BigDecimal.valueOf(100), stock.getHighestPrice());
  }

  @Test
  void getHighestPriceWithDecimalValues() {
    Stock stock = new Stock("AAPL", "Apple", new BigDecimal("99.50"));
    stock.addNewSalesPrice(new BigDecimal("99.75"));
    stock.addNewSalesPrice(new BigDecimal("99.99"));

    assertEquals(new BigDecimal("99.99"), stock.getHighestPrice());
  }

  //Tests for getLowestPrice
  @Test
  void getLowestPriceReturnsMinimum() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    stock.addNewSalesPrice(BigDecimal.valueOf(150));
    stock.addNewSalesPrice(BigDecimal.valueOf(50));
    stock.addNewSalesPrice(BigDecimal.valueOf(120));

    assertEquals(BigDecimal.valueOf(50), stock.getLowestPrice());
  }

  @Test
  void getLowestPriceWithSinglePrice() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    assertEquals(BigDecimal.valueOf(100), stock.getLowestPrice());
  }

  @Test
  void getLowestPriceWithDecimalValues() {
    Stock stock = new Stock("AAPL", "Apple", new BigDecimal("99.50"));
    stock.addNewSalesPrice(new BigDecimal("99.75"));
    stock.addNewSalesPrice(new BigDecimal("99.25"));

    assertEquals(new BigDecimal("99.25"), stock.getLowestPrice());
  }

  //Tests for getLatestPriceChange
  @Test
  void getLatestPriceChangeReturnsPositiveDifference() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    stock.addNewSalesPrice(BigDecimal.valueOf(150));

    assertEquals(BigDecimal.valueOf(50), stock.getLatestPriceChange());
  }

  @Test
  void getLatestPriceChangeReturnsNegativeDifference() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    stock.addNewSalesPrice(BigDecimal.valueOf(80));

    assertEquals(BigDecimal.valueOf(-20), stock.getLatestPriceChange());
  }

  @Test
  void getLatestPriceChangeReturnsZeroWithOnePrice() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    assertEquals(BigDecimal.ZERO, stock.getLatestPriceChange());
  }

  @Test
  void getLatestPriceChangeWithMultiplePriceUpdates() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    stock.addNewSalesPrice(BigDecimal.valueOf(120));
    stock.addNewSalesPrice(BigDecimal.valueOf(115));
    stock.addNewSalesPrice(BigDecimal.valueOf(140));

    assertEquals(BigDecimal.valueOf(25), stock.getLatestPriceChange());
  }

  @Test
  void getLatestPriceChangeWithDecimalValues() {
    Stock stock = new Stock("AAPL", "Apple", new BigDecimal("99.50"));
    stock.addNewSalesPrice(new BigDecimal("100.75"));

    assertEquals(new BigDecimal("1.25"), stock.getLatestPriceChange());
  }
}