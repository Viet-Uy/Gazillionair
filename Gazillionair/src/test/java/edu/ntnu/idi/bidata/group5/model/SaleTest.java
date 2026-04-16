package edu.ntnu.idi.bidata.group5.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SaleTest {

  private Stock testStock;
  private Player testPlayer;

  @BeforeEach
  void setUp() {
    testStock = new Stock("AAPL", "Apple", new BigDecimal("100"));
    testPlayer = new Player("TestPlayer", new BigDecimal("10000"));
  }

  // Positive tests - Happy path
  @Test
  void commit_sale_updates_committed_flag() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share);

    Sale sale = new Sale(share, 1);

    assertFalse(sale.isCommitted(), "Sale should not be committed initially");

    sale.commit(testPlayer);

    assertTrue(sale.isCommitted(), "Sale should be marked as committed after commit");
  }

  @Test
  void commit_sale_increases_player_money() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share);

    BigDecimal initialMoney = testPlayer.getMoney();

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertTrue(testPlayer.getMoney().compareTo(initialMoney) > 0,
        "Player money should increase after sale");
  }

  @Test
  void commit_sale_removes_share_from_portfolio() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share);

    assertEquals(1, testPlayer.getPortfolio().getShares().size(), "Portfolio should have one share");

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertEquals(0, testPlayer.getPortfolio().getShares().size(), "Portfolio should be empty after sale");
    assertFalse(testPlayer.getPortfolio().contains(share), "Portfolio should not contain sold share");
  }

  @Test
  void commit_sale_adds_transaction_to_archive() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share);

    assertTrue(testPlayer.getTransactionArchive().isEmpty(), "Archive should be empty initially");

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertFalse(testPlayer.getTransactionArchive().isEmpty(), "Archive should not be empty after commit");
    assertEquals(1, testPlayer.getTransactionArchive().getSales().size(),
        "Archive should contain one sale");
  }

  @Test
  void commit_sale_with_profit() {
    testStock.addNewSalesPrice(new BigDecimal("150")); // Price increased from 100 to 150

    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share);

    BigDecimal initialMoney = testPlayer.getMoney();

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertTrue(testPlayer.getMoney().compareTo(initialMoney) > 0,
        "Player money should increase significantly with profit");
    assertTrue(sale.isCommitted(), "Sale with profit should be committed");
  }

  @Test
  void commit_sale_with_loss() {
    testStock.addNewSalesPrice(new BigDecimal("50")); // Price decreased from 100 to 50

    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share);

    BigDecimal initialMoney = testPlayer.getMoney();

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertTrue(testPlayer.getMoney().compareTo(initialMoney) > 0,
        "Player should still receive some money even with loss (after commission deduction)");
    assertTrue(sale.isCommitted(), "Sale with loss should still be committed");
  }

  @Test
  void commit_sale_with_break_even_price() {
    // Stock price remains at purchase price
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share);

    BigDecimal initialMoney = testPlayer.getMoney();

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertTrue(testPlayer.getMoney().compareTo(initialMoney) > 0,
        "Player should get money back (even if break-even due to commission)");
    assertTrue(sale.isCommitted(), "Break-even sale should be committed");
  }

  @Test
  void commit_sale_with_large_quantity() {
    Share share = new Share(testStock, new BigDecimal("1000"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share);

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertTrue(sale.isCommitted(), "Large quantity sale should be committed");
    assertTrue(testPlayer.getPortfolio().getShares().isEmpty(), "Portfolio should be empty after large sale");
  }

  @Test
  void commit_sale_with_decimal_quantity() {
    Share share = new Share(testStock, new BigDecimal("10.50"), new BigDecimal("100.25"));
    testPlayer.getPortfolio().addShare(share);

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertTrue(sale.isCommitted(), "Decimal quantity sale should be committed");
    assertTrue(testPlayer.getPortfolio().getShares().isEmpty(), "Portfolio should be empty after sale");
  }

  @Test
  void commit_sale_with_week_one() {
    Share share = new Share(testStock, new BigDecimal("5"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share);

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertEquals(1, sale.getWeek(), "Sale week should be 1");
    assertTrue(sale.isCommitted(), "Week 1 sale should be committed");
  }

  @Test
  void commit_sale_with_large_week_number() {
    Share share = new Share(testStock, new BigDecimal("5"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share);

    Sale sale = new Sale(share, 100);
    sale.commit(testPlayer);

    assertEquals(100, sale.getWeek(), "Sale week should be 100");
    assertTrue(sale.isCommitted(), "Large week number sale should be committed");
  }

  // Negative tests - Error cases
  @Test
  void commit_sale_with_null_player_throws_exception() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));

    Sale sale = new Sale(share, 1);

    assertThrows(IllegalArgumentException.class,
        () -> sale.commit(null),
        "Commit with null player should throw IllegalArgumentException");
  }

  @Test
  void commit_sale_when_already_committed_throws_exception() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share);

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertThrows(IllegalStateException.class,
        () -> sale.commit(testPlayer),
        "Committing already committed sale should throw IllegalStateException");
  }

  @Test
  void commit_sale_when_player_does_not_own_share_throws_exception() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));

    Sale sale = new Sale(share, 1);

    assertThrows(IllegalStateException.class,
        () -> sale.commit(testPlayer),
        "Commit sale without owning share should throw IllegalStateException");
  }

  @Test
  void commit_sale_with_non_existent_share() {
    Share ownedShare = new Share(testStock, new BigDecimal("5"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(ownedShare);

    Share differentShare = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    Sale sale = new Sale(differentShare, 1);

    assertThrows(IllegalStateException.class,
        () -> sale.commit(testPlayer),
        "Selling different share instance should throw exception");
  }

  // Boundary tests - Edge cases
  @Test
  void commit_sale_with_very_small_quantity() {
    Share share = new Share(testStock, new BigDecimal("0.01"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share);

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertTrue(sale.isCommitted(), "Very small quantity sale should be committed");
  }

  @Test
  void commit_sale_with_very_small_stock_price() {
    Stock cheapStock = new Stock("CHEAP", "CheapCo", new BigDecimal("0.01"));
    Share share = new Share(cheapStock, new BigDecimal("1000"), new BigDecimal("0.01"));
    testPlayer.getPortfolio().addShare(share);

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertTrue(sale.isCommitted(), "Very small stock price sale should be committed");
  }

  @Test
  void commit_sale_with_very_expensive_stock() {
    Stock expensiveStock = new Stock("EXPENSIVE", "ExpensiveCo", new BigDecimal("100000"));
    Share share = new Share(expensiveStock, new BigDecimal("1"), new BigDecimal("100000"));
    testPlayer.getPortfolio().addShare(share);

    Sale sale = new Sale(share, 1);
    sale.commit(testPlayer);

    assertTrue(sale.isCommitted(), "Expensive stock sale should be committed");
    assertTrue(testPlayer.getMoney().compareTo(new BigDecimal("10000")) > 0,
        "Player money should increase significantly from expensive stock sale");
  }

  @Test
  void commit_multiple_sales_sequentially() {
    Share share1 = new Share(testStock, new BigDecimal("5"), new BigDecimal("100"));
    Share share2 = new Share(testStock, new BigDecimal("5"), new BigDecimal("100"));
    testPlayer.getPortfolio().addShare(share1);
    testPlayer.getPortfolio().addShare(share2);

    Sale sale1 = new Sale(share1, 1);
    sale1.commit(testPlayer);

    Sale sale2 = new Sale(share2, 2);
    sale2.commit(testPlayer);

    assertTrue(sale1.isCommitted(), "First sale should be committed");
    assertTrue(sale2.isCommitted(), "Second sale should be committed");
    assertEquals(2, testPlayer.getTransactionArchive().getSales().size(),
        "Archive should contain both sales");
    assertTrue(testPlayer.getPortfolio().getShares().isEmpty(),
        "Portfolio should be empty after both sales");
  }
}