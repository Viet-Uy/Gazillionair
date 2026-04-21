package edu.ntnu.idi.bidata.group5.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShareTest {

  private Stock testStock;

  @BeforeEach
  void setUp() {
    testStock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
  }

  @Test
  void constructor_with_valid_data_stores_fields_correctly() {
    Share share = new Share(testStock, BigDecimal.valueOf(10), BigDecimal.valueOf(150));

    assertEquals(testStock, share.getStock(), "Stock should be stored correctly");
    assertEquals(
        0,
        BigDecimal.valueOf(10).compareTo(share.getQuantity()),
        "Quantity should be stored correctly");
    assertEquals(
        0,
        BigDecimal.valueOf(150).compareTo(share.getPurchasePrice()),
        "Purchase price should be stored correctly");
  }

  @Test
  void getQuantity_returns_correct_value() {
    Share share = new Share(testStock, BigDecimal.valueOf(10), BigDecimal.valueOf(150));
    assertEquals(0, BigDecimal.valueOf(10).compareTo(share.getQuantity()));
  }

  @Test
  void getPurchasePrice_returns_correct_value() {
    Share share = new Share(testStock, BigDecimal.valueOf(10), BigDecimal.valueOf(150));
    assertEquals(0, BigDecimal.valueOf(150).compareTo(share.getPurchasePrice()));
  }

  @Test
  void getStock_returns_correct_stock() {
    Share share = new Share(testStock, BigDecimal.valueOf(10), BigDecimal.valueOf(150));
    assertEquals(testStock, share.getStock());
  }

  @Test
  void constructor_with_decimal_values_works_correctly() {
    Share share = new Share(testStock, new BigDecimal("10.50"), new BigDecimal("150.75"));
    assertEquals(0, new BigDecimal("10.50").compareTo(share.getQuantity()));
    assertEquals(0, new BigDecimal("150.75").compareTo(share.getPurchasePrice()));
  }

  @Test
  void constructor_with_large_values_works_correctly() {
    Share share = new Share(testStock, BigDecimal.valueOf(1000000), BigDecimal.valueOf(999999));
    assertEquals(0, BigDecimal.valueOf(1000000).compareTo(share.getQuantity()));
    assertEquals(0, BigDecimal.valueOf(999999).compareTo(share.getPurchasePrice()));
  }

  @Test
  void constructor_with_very_small_positive_values_works() {
    Share share = new Share(testStock, new BigDecimal("0.01"), new BigDecimal("0.01"));
    assertEquals(0, new BigDecimal("0.01").compareTo(share.getQuantity()));
    assertEquals(0, new BigDecimal("0.01").compareTo(share.getPurchasePrice()));
  }

  @Test
  void constructor_rejects_null_stock() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Share(null, BigDecimal.valueOf(10), BigDecimal.valueOf(150)),
        "Constructor should reject null stock");
  }

  @Test
  void constructor_rejects_null_quantity() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Share(testStock, null, BigDecimal.valueOf(150)),
        "Constructor should reject null quantity");
  }

  @Test
  void constructor_rejects_zero_quantity() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Share(testStock, BigDecimal.ZERO, BigDecimal.valueOf(150)),
        "Constructor should reject zero quantity");
  }

  @Test
  void constructor_rejects_negative_quantity() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Share(testStock, BigDecimal.valueOf(-10), BigDecimal.valueOf(150)),
        "Constructor should reject negative quantity");
  }

  @Test
  void constructor_rejects_null_purchase_price() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Share(testStock, BigDecimal.valueOf(10), null),
        "Constructor should reject null purchase price");
  }

  @Test
  void constructor_rejects_zero_purchase_price() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Share(testStock, BigDecimal.valueOf(10), BigDecimal.ZERO),
        "Constructor should reject zero purchase price");
  }

  @Test
  void constructor_rejects_negative_purchase_price() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Share(testStock, BigDecimal.valueOf(10), BigDecimal.valueOf(-150)),
        "Constructor should reject negative purchase price");
  }

  @Test
  void constructor_with_very_large_quantity() {
    BigDecimal largeQuantity = new BigDecimal("999999999999.99");
    Share share = new Share(testStock, largeQuantity, BigDecimal.valueOf(100));
    assertEquals(0, largeQuantity.compareTo(share.getQuantity()));
  }

  @Test
  void constructor_with_very_large_purchase_price() {
    BigDecimal largePrice = new BigDecimal("999999999999.99");
    Share share = new Share(testStock, BigDecimal.valueOf(10), largePrice);
    assertEquals(0, largePrice.compareTo(share.getPurchasePrice()));
  }

  @Test
  void constructor_with_quantity_just_above_zero() {
    BigDecimal tinyQuantity = new BigDecimal("0.0001");
    Share share = new Share(testStock, tinyQuantity, BigDecimal.valueOf(100));
    assertEquals(0, tinyQuantity.compareTo(share.getQuantity()));
  }

  @Test
  void constructor_with_price_just_above_zero() {
    BigDecimal tinyPrice = new BigDecimal("0.0001");
    Share share = new Share(testStock, BigDecimal.valueOf(10), tinyPrice);
    assertEquals(0, tinyPrice.compareTo(share.getPurchasePrice()));
  }
}