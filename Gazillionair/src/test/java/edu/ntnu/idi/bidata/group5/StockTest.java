package edu.ntnu.idi.bidata.group5;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

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


}