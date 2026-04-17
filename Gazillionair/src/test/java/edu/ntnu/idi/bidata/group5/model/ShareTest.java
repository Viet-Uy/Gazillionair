package edu.ntnu.idi.bidata.group5.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ShareTest {

  @Test
  void getQuantityReturnsExpectedValue() {
    Share share = createShare(BigDecimal.valueOf(10), BigDecimal.valueOf(150));
    assertEquals(BigDecimal.valueOf(10), share.getQuantity());
  }

  @Test
  void getPurchasePriceReturnsExpectedValue() {
    Share share = createShare(BigDecimal.valueOf(10), BigDecimal.valueOf(150));
    assertEquals(BigDecimal.valueOf(150), share.getPurchasePrice());
  }

  @Test
  void getStockReturnsExpectedStock() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(10));
    Share share = new Share(stock, BigDecimal.valueOf(10), BigDecimal.valueOf(150));
    assertEquals(stock, share.getStock());
  }

  @Test
  void constructorThrowsForNullStock() {
    assertThrows(IllegalArgumentException.class,
        () -> new Share(null, BigDecimal.valueOf(10), BigDecimal.valueOf(150)));
  }

  @Test
  void constructorThrowsForInvalidQuantity() {
    assertThrows(IllegalArgumentException.class,
        () -> new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(10)),
            BigDecimal.valueOf(-10), BigDecimal.valueOf(150)));
    assertThrows(IllegalArgumentException.class,
        () -> new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(10)),
            null, BigDecimal.valueOf(150)));
  }

  @Test
  void constructorThrowsForInvalidPurchasePrice() {
    assertThrows(IllegalArgumentException.class,
        () -> new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(10)),
            BigDecimal.valueOf(10), BigDecimal.valueOf(-150)));
    assertThrows(IllegalArgumentException.class,
        () -> new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(10)),
            BigDecimal.valueOf(10), null));
  }

  private Share createShare(BigDecimal quantity, BigDecimal purchasePrice) {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(10));
    return new Share(stock, quantity, purchasePrice);
  }
}
