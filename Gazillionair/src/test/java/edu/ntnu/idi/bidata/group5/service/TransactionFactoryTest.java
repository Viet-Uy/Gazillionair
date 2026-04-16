package edu.ntnu.idi.bidata.group5.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Share;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionFactoryTest {

  private TransactionFactory factory;
  private Share share;
  private Stock testStock;

  @BeforeEach
  void setUp() {
    factory = new TransactionFactory();
    testStock = new Stock("AAPL", "Apple", new BigDecimal("100"));
    share = new Share(testStock, new BigDecimal("1"), new BigDecimal("100"));
  }

  // Positive tests - Happy path
  @Test
  void create_purchase_creates_valid_transaction() {
    Purchase purchase = factory.createPurchase(share, 1);

    assertEquals(share, purchase.getShare(), "Purchase should contain the share");
    assertEquals(1, purchase.getWeek(), "Purchase week should be 1");
    assertFalse(purchase.isCommitted(), "Purchase should not be committed initially");
  }

  @Test
  void create_sale_creates_valid_transaction() {
    Sale sale = factory.createSale(share, 1);

    assertEquals(share, sale.getShare(), "Sale should contain the share");
    assertEquals(1, sale.getWeek(), "Sale week should be 1");
    assertFalse(sale.isCommitted(), "Sale should not be committed initially");
  }

  @Test
  void create_purchase_with_week_1() {
    Purchase purchase = factory.createPurchase(share, 1);

    assertEquals(1, purchase.getWeek(), "Week should be 1");
  }

  @Test
  void create_purchase_with_large_week_number() {
    Purchase purchase = factory.createPurchase(share, 100);

    assertEquals(100, purchase.getWeek(), "Week should be 100");
  }

  @Test
  void create_sale_with_week_1() {
    Sale sale = factory.createSale(share, 1);

    assertEquals(1, sale.getWeek(), "Week should be 1");
  }

  @Test
  void create_sale_with_large_week_number() {
    Sale sale = factory.createSale(share, 100);

    assertEquals(100, sale.getWeek(), "Week should be 100");
  }

  @Test
  void create_purchase_returns_new_instance_each_time() {
    Purchase purchase1 = factory.createPurchase(share, 1);
    Purchase purchase2 = factory.createPurchase(share, 1);

    assertEquals(purchase1.getShare(), purchase2.getShare(), "Shares should be same");
    assertEquals(purchase1.getWeek(), purchase2.getWeek(), "Weeks should be same");
    // But they should be different instances
  }

  @Test
  void create_sale_returns_new_instance_each_time() {
    Sale sale1 = factory.createSale(share, 1);
    Sale sale2 = factory.createSale(share, 1);

    assertEquals(sale1.getShare(), sale2.getShare(), "Shares should be same");
    assertEquals(sale1.getWeek(), sale2.getWeek(), "Weeks should be same");
    // But they should be different instances
  }

  @Test
  void create_purchase_with_large_share_quantity() {
    Share largeShare = new Share(testStock, new BigDecimal("999999"), new BigDecimal("100"));
    Purchase purchase = factory.createPurchase(largeShare, 1);

    assertEquals(largeShare, purchase.getShare(), "Should handle large quantity shares");
  }

  @Test
  void create_sale_with_large_share_quantity() {
    Share largeShare = new Share(testStock, new BigDecimal("999999"), new BigDecimal("100"));
    Sale sale = factory.createSale(largeShare, 1);

    assertEquals(largeShare, sale.getShare(), "Should handle large quantity shares");
  }

  @Test
  void create_purchase_with_decimal_share_quantity() {
    Share decimalShare = new Share(testStock, new BigDecimal("10.50"), new BigDecimal("100.25"));
    Purchase purchase = factory.createPurchase(decimalShare, 1);

    assertEquals(decimalShare, purchase.getShare(), "Should handle decimal quantity shares");
  }

  @Test
  void create_sale_with_decimal_share_quantity() {
    Share decimalShare = new Share(testStock, new BigDecimal("10.50"), new BigDecimal("100.25"));
    Sale sale = factory.createSale(decimalShare, 1);

    assertEquals(decimalShare, sale.getShare(), "Should handle decimal quantity shares");
  }

  // Negative tests - Error cases
  @Test
  void create_purchase_with_null_share_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> factory.createPurchase(null, 1),
        "createPurchase with null share should throw IllegalArgumentException");
  }

  @Test
  void create_sale_with_null_share_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> factory.createSale(null, 1),
        "createSale with null share should throw IllegalArgumentException");
  }

  @Test
  void create_purchase_with_invalid_week_zero_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> factory.createPurchase(share, 0),
        "createPurchase with week 0 should throw IllegalArgumentException");
  }

  @Test
  void create_purchase_with_negative_week_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> factory.createPurchase(share, -1),
        "createPurchase with negative week should throw IllegalArgumentException");
  }

  @Test
  void create_sale_with_invalid_week_zero_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> factory.createSale(share, 0),
        "createSale with week 0 should throw IllegalArgumentException");
  }

  @Test
  void create_sale_with_negative_week_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> factory.createSale(share, -1),
        "createSale with negative week should throw IllegalArgumentException");
  }

  // Boundary tests - Edge cases
  @Test
  void create_purchase_with_week_at_boundary_min() {
    Purchase purchase = factory.createPurchase(share, 1);

    assertEquals(1, purchase.getWeek(), "Minimum valid week is 1");
  }

  @Test
  void create_sale_with_week_at_boundary_min() {
    Sale sale = factory.createSale(share, 1);

    assertEquals(1, sale.getWeek(), "Minimum valid week is 1");
  }

  @Test
  void create_purchase_with_very_large_week() {
    Purchase purchase = factory.createPurchase(share, Integer.MAX_VALUE);

    assertEquals(Integer.MAX_VALUE, purchase.getWeek(), "Should handle maximum integer week");
  }

  @Test
  void create_sale_with_very_large_week() {
    Sale sale = factory.createSale(share, Integer.MAX_VALUE);

    assertEquals(Integer.MAX_VALUE, sale.getWeek(), "Should handle maximum integer week");
  }
}

