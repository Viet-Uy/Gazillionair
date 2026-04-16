package edu.ntnu.idi.bidata.group5.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseTest {

  private Stock testStock;
  private Player testPlayer;

  @BeforeEach
  void setUp() {
    testStock = new Stock("AAPL", "Apple", new BigDecimal("100"));
    testPlayer = new Player("TestPlayer", new BigDecimal("10000"));
  }

  // Positive tests - Happy path
  @Test
  void commit_purchase_updates_committed_flag() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 1);

    assertFalse(purchase.isCommitted(), "Purchase should not be committed initially");

    purchase.commit(testPlayer);

    assertTrue(purchase.isCommitted(), "Purchase should be marked as committed after commit");
  }

  @Test
  void commit_purchase_deducts_money_from_player() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 1);

    BigDecimal initialMoney = testPlayer.getMoney();
    purchase.commit(testPlayer);

    assertTrue(testPlayer.getMoney().compareTo(initialMoney) < 0,
        "Player money should be deducted after purchase");
  }

  @Test
  void commit_purchase_adds_share_to_portfolio() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 1);

    assertEquals(0, testPlayer.getPortfolio().getShares().size(), "Portfolio should be empty initially");

    purchase.commit(testPlayer);

    assertEquals(1, testPlayer.getPortfolio().getShares().size(), "Share should be added to portfolio");
    assertTrue(testPlayer.getPortfolio().contains(share), "Portfolio should contain the purchased share");
  }

  @Test
  void commit_purchase_adds_transaction_to_archive() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 1);

    assertTrue(testPlayer.getTransactionArchive().isEmpty(), "Archive should be empty initially");

    purchase.commit(testPlayer);

    assertFalse(testPlayer.getTransactionArchive().isEmpty(), "Archive should not be empty after commit");
    assertEquals(1, testPlayer.getTransactionArchive().getPurchases().size(),
        "Archive should contain one purchase");
  }

  @Test
  void commit_purchase_with_large_quantity() {
    Share share = new Share(testStock, new BigDecimal("1000"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 1);

    Player richPlayer = new Player("RichPlayer", new BigDecimal("500000"));
    purchase.commit(richPlayer);

    assertTrue(purchase.isCommitted(), "Large quantity purchase should be committed");
    assertTrue(richPlayer.getPortfolio().contains(share), "Portfolio should contain large quantity share");
  }

  @Test
  void commit_purchase_with_decimal_quantity() {
    Share share = new Share(testStock, new BigDecimal("10.50"), new BigDecimal("100.25"));
    Purchase purchase = new Purchase(share, 1);

    purchase.commit(testPlayer);

    assertTrue(purchase.isCommitted(), "Decimal quantity purchase should be committed");
    assertTrue(testPlayer.getPortfolio().contains(share), "Portfolio should contain decimal quantity share");
  }

  @Test
  void commit_purchase_with_week_one() {
    Share share = new Share(testStock, new BigDecimal("5"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 1);

    purchase.commit(testPlayer);

    assertEquals(1, purchase.getWeek(), "Purchase week should be 1");
    assertTrue(purchase.isCommitted(), "Week 1 purchase should be committed");
  }

  @Test
  void commit_purchase_with_large_week_number() {
    Share share = new Share(testStock, new BigDecimal("5"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 100);

    purchase.commit(testPlayer);

    assertEquals(100, purchase.getWeek(), "Purchase week should be 100");
    assertTrue(purchase.isCommitted(), "Large week number purchase should be committed");
  }

  // Negative tests - Error cases
  @Test
  void commit_purchase_with_null_player_throws_exception() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 1);

    assertThrows(IllegalArgumentException.class,
        () -> purchase.commit(null),
        "Commit with null player should throw IllegalArgumentException");
  }

  @Test
  void commit_purchase_when_already_committed_throws_exception() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 1);

    purchase.commit(testPlayer);

    assertThrows(IllegalStateException.class,
        () -> purchase.commit(testPlayer),
        "Committing already committed purchase should throw IllegalStateException");
  }

  @Test
  void commit_purchase_with_insufficient_funds_throws_exception() {
    Share share = new Share(testStock, new BigDecimal("500"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 1);

    Player poorPlayer = new Player("PoorPlayer", new BigDecimal("100"));

    assertThrows(IllegalStateException.class,
        () -> purchase.commit(poorPlayer),
        "Commit with insufficient funds should throw IllegalStateException");
  }

  @Test
  void commit_purchase_with_exact_available_funds() {
    // Calculate exact cost including commission
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 1);

    BigDecimal expectedCost = new BigDecimal("1005"); // (10 * 100) + 0.5% commission = 1005
    Player exactPlayer = new Player("ExactPlayer", expectedCost);

    purchase.commit(exactPlayer);

    assertTrue(purchase.isCommitted(), "Purchase with exact available funds should be committed");
    assertEquals(0, exactPlayer.getMoney().compareTo(BigDecimal.ZERO),
        "Player money should be exactly zero");
  }

  @Test
  void commit_purchase_with_just_slightly_insufficient_funds() {
    Share share = new Share(testStock, new BigDecimal("10"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 1);

    BigDecimal expectedCost = new BigDecimal("1005");
    Player slightlyPoorPlayer = new Player("SlightlyPoor", expectedCost.subtract(BigDecimal.valueOf(0.01)));

    assertThrows(IllegalStateException.class,
        () -> purchase.commit(slightlyPoorPlayer),
        "Commit with just insufficient funds should throw exception");
  }

  // Boundary tests - Edge cases
  @Test
  void purchase_with_very_small_quantity() {
    Share share = new Share(testStock, new BigDecimal("0.01"), new BigDecimal("100"));
    Purchase purchase = new Purchase(share, 1);

    purchase.commit(testPlayer);

    assertTrue(purchase.isCommitted(), "Very small quantity purchase should be committed");
  }

  @Test
  void purchase_with_very_expensive_stock() {
    Stock expensiveStock = new Stock("EXPENSIVE", "ExpensiveCo", new BigDecimal("100000"));
    Share share = new Share(expensiveStock, new BigDecimal("1"), new BigDecimal("100000"));
    Purchase purchase = new Purchase(share, 1);

    Player richPlayer = new Player("VeryRichPlayer", new BigDecimal("1000000"));
    purchase.commit(richPlayer);

    assertTrue(purchase.isCommitted(), "Expensive stock purchase should be committed");
  }

  @Test
  void purchase_with_very_cheap_stock() {
    Stock cheapStock = new Stock("CHEAP", "CheapCo", new BigDecimal("0.01"));
    Share share = new Share(cheapStock, new BigDecimal("1000"), new BigDecimal("0.01"));
    Purchase purchase = new Purchase(share, 1);

    purchase.commit(testPlayer);

    assertTrue(purchase.isCommitted(), "Cheap stock purchase should be committed");
  }
}