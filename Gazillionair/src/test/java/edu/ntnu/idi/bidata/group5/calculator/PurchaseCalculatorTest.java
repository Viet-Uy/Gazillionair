package edu.ntnu.idi.bidata.group5.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idi.bidata.group5.model.Share;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchaseCalculatorTest {

  private Stock testStock;

  @BeforeEach
  void setUp() {
    testStock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
  }

  @Test
  void calculate_gross_returns_quantity_times_price() {
    Share share = new Share(testStock, BigDecimal.valueOf(10), BigDecimal.valueOf(50));
    PurchaseCalculator calculator = new PurchaseCalculator(share);

    BigDecimal expectedGross = BigDecimal.valueOf(500);
    assertEquals(
        0,
        expectedGross.compareTo(calculator.calculateGross()),
        "Gross should be quantity times purchase price");
  }

  @Test
  void calculate_commission_is_0_5_percent_of_gross() {
    Share share = new Share(testStock, BigDecimal.valueOf(10), BigDecimal.valueOf(50));
    PurchaseCalculator calculator = new PurchaseCalculator(share);

    BigDecimal expectedCommission = BigDecimal.valueOf(2.5);
    assertEquals(
        0,
        expectedCommission.compareTo(calculator.calculateCommission()),
        "Commission should be 0.5% of gross");
  }

  @Test
  void calculate_tax_is_zero_for_purchase() {
    Share share = new Share(testStock, BigDecimal.valueOf(10), BigDecimal.valueOf(50));
    PurchaseCalculator calculator = new PurchaseCalculator(share);

    assertEquals(
        0,
        BigDecimal.ZERO.compareTo(calculator.calculateTax()),
        "Tax should be zero for purchases");
  }

  @Test
  void calculate_total_is_gross_plus_commission_plus_tax() {
    Share share = new Share(testStock, BigDecimal.valueOf(10), BigDecimal.valueOf(50));
    PurchaseCalculator calculator = new PurchaseCalculator(share);

    BigDecimal expectedTotal = BigDecimal.valueOf(502.5);
    assertEquals(
        0,
        expectedTotal.compareTo(calculator.calculateTotal()),
        "Total should be gross plus commission plus tax");
  }

  @Test
  void calculate_with_large_quantity() {
    Share share = new Share(testStock, BigDecimal.valueOf(1000), BigDecimal.valueOf(100));
    PurchaseCalculator calculator = new PurchaseCalculator(share);

    BigDecimal expectedGross = BigDecimal.valueOf(100000);
    BigDecimal expectedCommission = BigDecimal.valueOf(500);
    BigDecimal expectedTotal = BigDecimal.valueOf(100500);

    assertEquals(0, expectedGross.compareTo(calculator.calculateGross()));
    assertEquals(0, expectedCommission.compareTo(calculator.calculateCommission()));
    assertEquals(0, expectedTotal.compareTo(calculator.calculateTotal()));
  }

  @Test
  void calculate_with_decimal_values() {
    Share share = new Share(testStock, new BigDecimal("10.50"), new BigDecimal("50.25"));
    PurchaseCalculator calculator = new PurchaseCalculator(share);

    BigDecimal expectedGross = new BigDecimal("527.625");
    assertEquals(
        0,
        expectedGross.compareTo(calculator.calculateGross()),
        "Should handle decimal values correctly");
  }

  @Test
  void calculate_with_very_small_quantity() {
    Share share = new Share(testStock, new BigDecimal("0.01"), new BigDecimal("100"));
    PurchaseCalculator calculator = new PurchaseCalculator(share);

    BigDecimal expectedGross = new BigDecimal("1");
    assertEquals(0, expectedGross.compareTo(calculator.calculateGross()));
  }

  @Test
  void calculate_with_very_large_quantity() {
    Share share = new Share(testStock, new BigDecimal("999999.99"), new BigDecimal("100"));
    PurchaseCalculator calculator = new PurchaseCalculator(share);

    BigDecimal expectedGross = new BigDecimal("99999999");
    assertEquals(0, expectedGross.compareTo(calculator.calculateGross()));
  }

  @Test
  void calculate_with_expensive_stock() {
    Share share = new Share(testStock, BigDecimal.valueOf(1), new BigDecimal("100000"));
    PurchaseCalculator calculator = new PurchaseCalculator(share);

    BigDecimal expectedGross = BigDecimal.valueOf(100000);
    BigDecimal expectedCommission = BigDecimal.valueOf(500);
    BigDecimal expectedTotal = BigDecimal.valueOf(100500);

    assertEquals(0, expectedGross.compareTo(calculator.calculateGross()));
    assertEquals(0, expectedCommission.compareTo(calculator.calculateCommission()));
    assertEquals(0, expectedTotal.compareTo(calculator.calculateTotal()));
  }

  @Test
  void calculate_with_cheap_stock() {
    Share share = new Share(testStock, BigDecimal.valueOf(1000), new BigDecimal("0.01"));
    PurchaseCalculator calculator = new PurchaseCalculator(share);

    BigDecimal expectedGross = BigDecimal.valueOf(10);
    assertEquals(0, expectedGross.compareTo(calculator.calculateGross()));
  }

  @Test
  void commission_calculation_precision() {
    Share share1 = new Share(testStock, BigDecimal.valueOf(3), BigDecimal.valueOf(100));
    PurchaseCalculator calc1 = new PurchaseCalculator(share1);

    BigDecimal gross1 = BigDecimal.valueOf(300);
    BigDecimal commission1 = BigDecimal.valueOf(1.5);

    assertEquals(0, gross1.compareTo(calc1.calculateGross()));
    assertEquals(0, commission1.compareTo(calc1.calculateCommission()));
  }

  @Test
  void constructor_with_null_share_throws_exception() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new PurchaseCalculator(null),
        "Constructor should reject null share");
  }

  @Test
  void calculate_with_minimum_valid_quantity() {
    Share share = new Share(testStock, new BigDecimal("0.0001"), BigDecimal.valueOf(100));
    PurchaseCalculator calculator = new PurchaseCalculator(share);

    assertNotNull(calculator.calculateGross(), "Should handle very small quantities");
    assertNotNull(calculator.calculateTotal(), "Total should be calculable");
  }

  @Test
  void calculate_with_maximum_reasonable_quantity() {
    Share share = new Share(testStock, new BigDecimal("999999999"), BigDecimal.valueOf(999999));
    PurchaseCalculator calculator = new PurchaseCalculator(share);

    assertNotNull(calculator.calculateGross(), "Should handle very large quantities");
  }

  @Test
  void commission_rounding_consistency() {
    Share share1 = new Share(testStock, BigDecimal.valueOf(1), BigDecimal.valueOf(33));
    Share share2 = new Share(testStock, BigDecimal.valueOf(1), BigDecimal.valueOf(67));

    PurchaseCalculator calc1 = new PurchaseCalculator(share1);
    PurchaseCalculator calc2 = new PurchaseCalculator(share2);

    BigDecimal comm1 = calc1.calculateCommission();
    BigDecimal comm2 = calc2.calculateCommission();

    assertNotNull(comm1, "Commission 1 should be calculated");
    assertNotNull(comm2, "Commission 2 should be calculated");
  }
}