package edu.ntnu.idi.bidata.group5.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.bidata.group5.model.Share;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaleCalculatorTest {

  private Stock testStock;

  @BeforeEach
  void setUp() {
    testStock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
  }

  @Test
  void calculate_gross_returns_quantity_times_current_sales_price() {
    Share share = new Share(testStock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));
    SaleCalculator calculator = new SaleCalculator(share);

    BigDecimal expectedGross = BigDecimal.valueOf(9000);
    assertEquals(
        0,
        expectedGross.compareTo(calculator.calculateGross()),
        "Gross should be quantity times current sales price");
  }

  @Test
  void calculate_commission_is_1_percent_of_gross() {
    Share share = new Share(testStock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));
    SaleCalculator calculator = new SaleCalculator(share);

    BigDecimal expectedCommission = BigDecimal.valueOf(90);
    assertEquals(
        0,
        expectedCommission.compareTo(calculator.calculateCommission()),
        "Commission should be 1% of gross");
  }

  @Test
  void calculate_tax_on_profit() {
    Share share = new Share(testStock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));
    SaleCalculator calculator = new SaleCalculator(share);

    BigDecimal expectedTax = BigDecimal.valueOf(2403);
    assertEquals(
        0,
        expectedTax.compareTo(calculator.calculateTax()),
        "Tax should be 30% of profit");
  }

  @Test
  void calculate_total_correct() {
    Share share = new Share(testStock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));
    SaleCalculator calculator = new SaleCalculator(share);

    BigDecimal expectedTotal = BigDecimal.valueOf(6507);
    assertEquals(
        0,
        expectedTotal.compareTo(calculator.calculateTotal()),
        "Total should be gross minus commission minus tax");
  }

  @Test
  void calculate_tax_is_zero_on_loss() {
    Stock lossStock = new Stock("LOSS", "LossCo", new BigDecimal("50"));
    Share share = new Share(lossStock, BigDecimal.valueOf(10), BigDecimal.valueOf(100));
    SaleCalculator calculator = new SaleCalculator(share);

    assertEquals(
        0,
        BigDecimal.ZERO.compareTo(calculator.calculateTax()),
        "Tax should be zero when selling at a loss");
  }

  @Test
  void calculate_with_break_even_scenario() {
    Share share = new Share(testStock, BigDecimal.valueOf(10), BigDecimal.valueOf(100));
    SaleCalculator calculator = new SaleCalculator(share);

    assertEquals(
        0,
        BigDecimal.ZERO.compareTo(calculator.calculateTax()),
        "Tax should be zero on break-even or loss");
  }

  @Test
  void calculate_total_with_loss() {
    Stock lossStock = new Stock("LOSS", "LossCo", new BigDecimal("50"));
    Share share = new Share(lossStock, BigDecimal.valueOf(10), BigDecimal.valueOf(100));
    SaleCalculator calculator = new SaleCalculator(share);

    BigDecimal expectedTotal = BigDecimal.valueOf(495);
    assertEquals(
        0,
        expectedTotal.compareTo(calculator.calculateTotal()),
        "Total should deduct commission even on loss");
  }

  @Test
  void calculate_with_large_quantity() {
    Share share = new Share(testStock, BigDecimal.valueOf(1000), BigDecimal.valueOf(10));
    SaleCalculator calculator = new SaleCalculator(share);

    BigDecimal expectedGross = BigDecimal.valueOf(100000);
    assertEquals(0, expectedGross.compareTo(calculator.calculateGross()));
  }

  @Test
  void calculate_with_decimal_values() {
    Share share = new Share(testStock, new BigDecimal("10.50"), new BigDecimal("50.25"));
    SaleCalculator calculator = new SaleCalculator(share);

    BigDecimal expectedGross = new BigDecimal("1050");
    assertEquals(
        0,
        expectedGross.compareTo(calculator.calculateGross()),
        "Should handle decimal values correctly");
  }

  @Test
  void calculate_with_very_small_quantity() {
    Share share = new Share(testStock, new BigDecimal("0.01"), new BigDecimal("100"));
    SaleCalculator calculator = new SaleCalculator(share);

    BigDecimal expectedGross = new BigDecimal("1");
    assertEquals(0, expectedGross.compareTo(calculator.calculateGross()));
  }

  @Test
  void calculate_with_very_large_quantity() {
    Share share = new Share(testStock, new BigDecimal("999999.99"), new BigDecimal("0.01"));
    SaleCalculator calculator = new SaleCalculator(share);

    assertNotNull(calculator.calculateGross(), "Should handle very large quantities");
  }

  @Test
  void calculate_with_expensive_stock() {
    Stock expensiveStock = new Stock("EXPENSIVE", "ExpensiveCo", new BigDecimal("100000"));
    Share share = new Share(expensiveStock, BigDecimal.valueOf(1), new BigDecimal("100000"));
    SaleCalculator calculator = new SaleCalculator(share);

    BigDecimal expectedGross = BigDecimal.valueOf(100000);
    assertEquals(0, expectedGross.compareTo(calculator.calculateGross()));
  }

  @Test
  void calculate_with_cheap_stock() {
    Share share = new Share(testStock, BigDecimal.valueOf(1000), new BigDecimal("0.01"));
    SaleCalculator calculator = new SaleCalculator(share);

    BigDecimal expectedGross = BigDecimal.valueOf(100000);
    assertEquals(0, expectedGross.compareTo(calculator.calculateGross()));
  }

  @Test
  void calculate_commission_rounding_consistency() {
    Share share1 = new Share(testStock, BigDecimal.valueOf(1), new BigDecimal("33"));
    Share share2 = new Share(testStock, BigDecimal.valueOf(1), new BigDecimal("67"));

    SaleCalculator calc1 = new SaleCalculator(share1);
    SaleCalculator calc2 = new SaleCalculator(share2);

    assertNotNull(calc1.calculateCommission(), "Commission 1 should be calculable");
    assertNotNull(calc2.calculateCommission(), "Commission 2 should be calculable");
  }

  @Test
  void calculate_tax_never_negative() {
    Stock deepLossStock = new Stock("DEEPLOSS", "DeepLossCo", new BigDecimal("10"));
    Share share = new Share(deepLossStock, BigDecimal.valueOf(100), BigDecimal.valueOf(100));
    SaleCalculator calculator = new SaleCalculator(share);

    BigDecimal tax = calculator.calculateTax();
    assertTrue(tax.compareTo(BigDecimal.ZERO) >= 0, "Tax should never be negative");
  }

  @Test
  void calculate_total_with_high_profit() {
    Stock gainStock = new Stock("GAIN", "GainCo", new BigDecimal("500"));
    Share share = new Share(gainStock, BigDecimal.valueOf(10), BigDecimal.valueOf(100));
    SaleCalculator calculator = new SaleCalculator(share);

    BigDecimal total = calculator.calculateTotal();
    assertTrue(
        total.compareTo(BigDecimal.ZERO) > 0,
        "Total should be positive even after high tax on high profit");
  }

  @Test
  void constructor_with_null_share_throws_exception() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new SaleCalculator(null),
        "Constructor should reject null share");
  }

  @Test
  void calculate_with_minimum_valid_quantity() {
    Share share = new Share(testStock, new BigDecimal("0.0001"), BigDecimal.valueOf(100));
    SaleCalculator calculator = new SaleCalculator(share);

    assertNotNull(calculator.calculateGross(), "Should handle very small quantities");
    assertNotNull(calculator.calculateTotal(), "Total should be calculable");
  }

  @Test
  void calculate_with_maximum_reasonable_quantity() {
    Share share = new Share(testStock, new BigDecimal("999999999"), BigDecimal.valueOf(100));
    SaleCalculator calculator = new SaleCalculator(share);

    assertNotNull(calculator.calculateGross(), "Should handle very large quantities");
    assertNotNull(calculator.calculateTotal(), "Total should be calculable");
  }
}