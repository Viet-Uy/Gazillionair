package edu.ntnu.idi.bidata.group5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeTest {

  private Stock apple;
  private Stock tesla;
  private Exchange exchange;

  @BeforeEach
  void setUp() {
    apple = new Stock("AAPL", "Apple", new BigDecimal(100));
    tesla = new Stock("TSLA", "Tesla", new BigDecimal(200));
    exchange = new Exchange("TechExchange", List.of(apple, tesla));
  }

  @Test
  void constructorInitializeFieldsCorrectly() {
    assertEquals ("TechExchange", exchange.getName());
    assertEquals (1,exchange.getWeek());
    assertEquals (apple, exchange.getStock("AAPL"));
    assertEquals (tesla, exchange.getStock("TSLA"));
  }

  @Test
  void getStockReturnCorrectStock() {
    Stock result = exchange.getStock("TSLA");
    assertEquals (tesla, result);
  }

  @Test
  void findStocksReturnCorrectStock() {
    List<Stock> result = exchange.findStocks("AAPL");
    assertEquals (apple, result);
  }

  @Test
  void advanceIncreaseWeekNumber() {
    exchange.advance();
    assertEquals (2,  exchange.getWeek());
  }

  @Test
  void advanceUpdateStockPrices() {
    BigDecimal oldApplePrice = apple.getSalesPrice();
    BigDecimal oldTeslaPrice = tesla.getSalesPrice();

    exchange.advance();

    assertNotEquals(oldApplePrice, apple.getSalesPrice());
    assertNotEquals(oldTeslaPrice, tesla.getSalesPrice());
  }

}