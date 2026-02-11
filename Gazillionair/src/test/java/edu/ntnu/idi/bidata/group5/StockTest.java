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
}