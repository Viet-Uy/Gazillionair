package edu.ntnu.idi.bidata.group5.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idi.bidata.group5.model.Share;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PurchaseCalculatorTest {

  @Test
  void calculateGross() {
    PurchaseCalculator purchaseCalculator = createCalculator();
    BigDecimal expectedGross = BigDecimal.valueOf(500); // 10 shares * $50 buy price
    assertEquals(expectedGross, purchaseCalculator.calculateGross());
  }

  @Test
  void calculateCommission() {
    PurchaseCalculator purchaseCalculator = createCalculator();
    BigDecimal expectedCommission = BigDecimal.valueOf(2.500); // 0.5% of $500 gross
    assertEquals(0, expectedCommission.compareTo(purchaseCalculator.calculateCommission()));
  }

  @Test
  void calculateTax() {
    PurchaseCalculator purchaseCalculator = createCalculator();
    BigDecimal expectedTax = BigDecimal.ZERO; // No tax when buying
    assertEquals(expectedTax, purchaseCalculator.calculateTax());
  }

  @Test
  void calculateTotal() {
    PurchaseCalculator purchaseCalculator = createCalculator();
    BigDecimal expectedTotal = BigDecimal.valueOf(502.5);
    assertEquals(0, expectedTotal.compareTo(purchaseCalculator.calculateTotal()));
  }

  @Test
  void constructorWithNullShare() {
    assertThrows(IllegalArgumentException.class, () -> new PurchaseCalculator(null));
  }

  private PurchaseCalculator createCalculator() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    Share share = new Share(stock, BigDecimal.valueOf(10), BigDecimal.valueOf(50));
    return new PurchaseCalculator(share);
  }
}
