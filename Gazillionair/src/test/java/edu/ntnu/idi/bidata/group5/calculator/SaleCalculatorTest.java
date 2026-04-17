package edu.ntnu.idi.bidata.group5.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idi.bidata.group5.model.Share;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class SaleCalculatorTest {

  @Test
  void calculateGross() {
    SaleCalculator saleCalculator = createCalculator();
    BigDecimal expectedGross = BigDecimal.valueOf(9000); // 90 shares * $100 sales price
    assertEquals(expectedGross, saleCalculator.calculateGross());
  }

  @Test
  void calculateCommission() {
    SaleCalculator saleCalculator = createCalculator();
    BigDecimal expectedCommission = BigDecimal.valueOf(90); // 1% of $9000 gross
    assertEquals(0, expectedCommission.compareTo(saleCalculator.calculateCommission()));
  }

  @Test
  void calculateTax() {
    SaleCalculator saleCalculator = createCalculator();
    BigDecimal expectedTax = BigDecimal.valueOf(2403); // 30% of (9000 - 90 - 900)
    assertEquals(0, expectedTax.compareTo(saleCalculator.calculateTax()));
  }

  @Test
  void calculateTotal() {
    SaleCalculator saleCalculator = createCalculator();
    BigDecimal expectedTotal = BigDecimal.valueOf(6507); // 9000 - 90 - 2403
    assertEquals(0, expectedTotal.compareTo(saleCalculator.calculateTotal()));
  }

  @Test
  void calculateTaxIsZeroWhenSaleIsLoss() {
    Stock stock = new Stock("LOSS", "LossCo", BigDecimal.valueOf(50));
    Share share = new Share(stock, BigDecimal.valueOf(10), BigDecimal.valueOf(100));
    SaleCalculator saleCalculator = new SaleCalculator(share);
    assertEquals(0, BigDecimal.ZERO.compareTo(saleCalculator.calculateTax()));
  }

  private SaleCalculator createCalculator() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    Share share = new Share(stock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));
    return new SaleCalculator(share);
  }
}
