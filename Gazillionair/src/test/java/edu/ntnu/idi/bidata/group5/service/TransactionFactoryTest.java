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

  @BeforeEach
  void setUp() {
    factory = new TransactionFactory();
    Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
    share = new Share(stock, new BigDecimal("1"), new BigDecimal("100"));
  }

  @Test
  void createPurchaseCreatesValidTransaction() {
    Purchase purchase = factory.createPurchase(share, 1);

    assertEquals(share, purchase.getShare());
    assertEquals(1, purchase.getWeek());
    assertFalse(purchase.isCommitted());
  }

  @Test
  void createSaleCreatesValidTransaction() {
    Sale sale = factory.createSale(share, 1);

    assertEquals(share, sale.getShare());
    assertEquals(1, sale.getWeek());
    assertFalse(sale.isCommitted());
  }

  @Test
  void createMethodsRejectNullShare() {
    assertThrows(IllegalArgumentException.class, () -> factory.createPurchase(null, 1));
    assertThrows(IllegalArgumentException.class, () -> factory.createSale(null, 1));
  }

  @Test
  void createMethodsRejectInvalidWeekFromTransactionConstructors() {
    assertThrows(IllegalArgumentException.class, () -> factory.createPurchase(share, 0));
    assertThrows(IllegalArgumentException.class, () -> factory.createSale(share, 0));
  }
}

