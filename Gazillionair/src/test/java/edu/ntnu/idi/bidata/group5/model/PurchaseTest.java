package edu.ntnu.idi.bidata.group5.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PurchaseTest {

  @Test
  void commitPurchase() {
    Purchase purchase = new Purchase(createShare(), 1);
    Player player = new Player("TestPlayer", BigDecimal.valueOf(10000));
    purchase.commit(player);
    assertTrue(purchase.isCommitted());
  }

  @Test
  void commitPurchaseWithNullPlayer() {
    Purchase purchase = new Purchase(createShare(), 1);
    assertThrows(IllegalArgumentException.class, () -> purchase.commit(null));
  }

  @Test
  void commitWhenCommitted() {
    Purchase purchase = new Purchase(createShare(), 1);
    Player player = new Player("TestPlayer", BigDecimal.valueOf(10000));
    purchase.commit(player);
    assertThrows(IllegalStateException.class, () -> purchase.commit(player));
  }

  @Test
  void commitWithInsufficientFunds() {
    Purchase purchase = new Purchase(createShare(), 1);
    Player player = new Player("TestPlayer", BigDecimal.valueOf(500));
    assertThrows(IllegalStateException.class, () -> purchase.commit(player));
  }

  private Share createShare() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    return new Share(stock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));
  }
}
