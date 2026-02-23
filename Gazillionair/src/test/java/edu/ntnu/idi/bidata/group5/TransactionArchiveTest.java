package edu.ntnu.idi.bidata.group5;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionArchiveTest {

  private TransactionArchive archive;

  @BeforeEach
  void setUp() {
    archive = new TransactionArchive();
  }

  private Share createShare() {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    return new Share(stock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));
  }


  @Test
  void add() {
    archive.add(new Purchase(createShare(), 1));
    assertEquals(1, archive.getTransactions().size());

    assertThrows(IllegalArgumentException.class, () -> archive.add(null));
  }

  @Test
  void isEmpty() {
    assertTrue(archive.isEmpty());

    archive.add(new Purchase(createShare(), 1));
    assertFalse(archive.isEmpty());
  }

  @Test
  void getTransactions() {
    archive.add(new Purchase(createShare(), 1));
    archive.add(new Sale(createShare(), 2));

    assertEquals(2, archive.getTransactions().size());
  }

  @Test
  void getPurchases() {
    archive.add(new Purchase(createShare(), 1));
    archive.add(new Sale(createShare(), 2));

    assertEquals(1, archive.getPurchases().size());
  }

  @Test
  void getSales() {
    archive.add(new Purchase(createShare(), 1));
    archive.add(new Sale(createShare(), 2));

    assertEquals(1, archive.getSales().size());
  }

  @Test
  void countDistinctWeeks() {
    archive.add(new Purchase(createShare(), 1));
    archive.add(new Sale(createShare(), 2));

    assertEquals(2, archive.countDistinctWeeks());
  }

  @Test
  void countDistinctWeeksWithZeroTransactions() {
    assertEquals(0, archive.countDistinctWeeks());
  }
}