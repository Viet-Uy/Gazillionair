package edu.ntnu.idi.bidata.group5.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Share;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionArchiveTest {

  private TransactionArchive archive;

  @BeforeEach
  void setUp() {
    archive = new TransactionArchive();
  }

  @Test
  void addAddsTransactionAndRejectsNull() {
    archive.add(new Purchase(createShare(), 1));
    assertEquals(1, archive.getTransactions().size());
    assertThrows(IllegalArgumentException.class, () -> archive.add(null));
  }

  @Test
  void isEmptyReflectsArchiveState() {
    assertTrue(archive.isEmpty());
    archive.add(new Purchase(createShare(), 1));
    assertFalse(archive.isEmpty());
  }

  @Test
  void getTransactionsReturnsAllTransactions() {
    archive.add(new Purchase(createShare(), 1));
    archive.add(new Sale(createShare(), 2));
    assertEquals(2, archive.getTransactions().size());
  }

  @Test
  void getPurchasesReturnsOnlyPurchases() {
    archive.add(new Purchase(createShare(), 1));
    archive.add(new Sale(createShare(), 2));
    assertEquals(1, archive.getPurchases().size());
  }

  @Test
  void getSalesReturnsOnlySales() {
    archive.add(new Purchase(createShare(), 1));
    archive.add(new Sale(createShare(), 2));
    assertEquals(1, archive.getSales().size());
  }

  @Test
  void countDistinctWeeksCountsUniqueWeeks() {
    archive.add(new Purchase(createShare(), 1));
    archive.add(new Sale(createShare(), 2));
    assertEquals(2, archive.countDistinctWeeks());
  }

  @Test
  void countDistinctWeeksWithZeroTransactionsReturnsZero() {
    assertEquals(0, archive.countDistinctWeeks());
  }

  private Share createShare() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    return new Share(stock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));
  }
}
