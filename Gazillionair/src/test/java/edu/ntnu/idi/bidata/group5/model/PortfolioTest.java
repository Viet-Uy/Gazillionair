package edu.ntnu.idi.bidata.group5.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.bidata.group5.calculator.SaleCalculator;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PortfolioTest {

  private Portfolio portfolio;
  private Stock apple;
  private Stock tesla;

  @BeforeEach
  void setUp() {
    portfolio = new Portfolio();
    apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
    tesla = new Stock("TSLA", "Tesla", new BigDecimal("200"));
  }

  @Test
  void add_share_successfully() {
    Share share = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));

    assertTrue(portfolio.addShare(share), "addShare should return true");
    assertEquals(1, portfolio.getShares().size(), "Portfolio should contain one share");
  }

  @Test
  void add_multiple_shares_successfully() {
    Share share1 = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    Share share2 = new Share(tesla, new BigDecimal("5"), new BigDecimal("200"));

    portfolio.addShare(share1);
    portfolio.addShare(share2);

    assertEquals(2, portfolio.getShares().size(), "Portfolio should contain two shares");
    assertTrue(portfolio.contains(share1), "Portfolio should contain first share");
    assertTrue(portfolio.contains(share2), "Portfolio should contain second share");
  }

  @Test
  void add_multiple_shares_of_same_stock() {
    Share share1 = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    Share share2 = new Share(apple, new BigDecimal("5"), new BigDecimal("110"));

    portfolio.addShare(share1);
    portfolio.addShare(share2);

    assertEquals(2, portfolio.getShares().size(), "Portfolio should contain two share instances");
    List<Share> appleShares = portfolio.getShares("AAPL");
    assertEquals(2, appleShares.size(), "Should retrieve both AAPL shares");
  }

  @Test
  void remove_share_successfully() {
    Share share = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    portfolio.addShare(share);

    assertTrue(portfolio.getShares().contains(share));
    assertTrue(portfolio.removeShare(share), "removeShare should return true");
    assertEquals(0, portfolio.getShares().size(), "Portfolio should be empty");
    assertFalse(portfolio.contains(share), "Portfolio should not contain removed share");
  }

  @Test
  void get_shares_returns_all_shares() {
    Share share1 = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    Share share2 = new Share(tesla, new BigDecimal("5"), new BigDecimal("200"));

    portfolio.addShare(share1);
    portfolio.addShare(share2);

    List<Share> shares = portfolio.getShares();
    assertEquals(2, shares.size(), "Should return all shares");
    assertTrue(shares.contains(share1), "Should contain first share");
    assertTrue(shares.contains(share2), "Should contain second share");
  }

  @Test
  void get_shares_returns_copy() {
    Share share = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    portfolio.addShare(share);

    List<Share> shares = portfolio.getShares();
    shares.add(new Share(tesla, new BigDecimal("5"), new BigDecimal("200")));

    assertEquals(1, portfolio.getShares().size(), "Adding to returned list should not modify portfolio");
  }

  @Test
  void get_shares_by_symbol_returns_matching_shares() {
    Share share1 = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    Share share2 = new Share(apple, new BigDecimal("5"), new BigDecimal("110"));
    Share share3 = new Share(tesla, new BigDecimal("3"), new BigDecimal("200"));

    portfolio.addShare(share1);
    portfolio.addShare(share2);
    portfolio.addShare(share3);

    List<Share> appleShares = portfolio.getShares("AAPL");
    assertEquals(2, appleShares.size(), "Should return only AAPL shares");
    assertTrue(appleShares.contains(share1), "Should contain first AAPL share");
    assertTrue(appleShares.contains(share2), "Should contain second AAPL share");
  }

  @Test
  void get_shares_by_symbol_case_sensitive() {
    Share share = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    portfolio.addShare(share);

    List<Share> shares = portfolio.getShares("AAPL");
    assertEquals(1, shares.size(), "Should match exact case");
  }

  @Test
  void get_shares_by_symbol_returns_empty_for_no_matches() {
    Share share = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    portfolio.addShare(share);

    List<Share> shares = portfolio.getShares("TSLA");
    assertTrue(shares.isEmpty(), "Should return empty list for non-existent symbol");
  }

  @Test
  void contains_share_returns_true_for_owned_share() {
    Share share = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    portfolio.addShare(share);

    assertTrue(portfolio.contains(share), "Should contain the added share");
  }

  @Test
  void contains_share_returns_false_for_non_owned_share() {
    Share share1 = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    Share share2 = new Share(tesla, new BigDecimal("5"), new BigDecimal("200"));

    portfolio.addShare(share1);

    assertFalse(portfolio.contains(share2), "Should not contain share that was not added");
  }

  @Test
  void get_net_worth_with_empty_portfolio() {
    assertEquals(0, BigDecimal.ZERO.compareTo(portfolio.getNetWorth()),
        "Empty portfolio should have zero net worth");
  }

  @Test
  void get_net_worth_with_single_share() {
    Share share = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    portfolio.addShare(share);

    SaleCalculator calc = new SaleCalculator(share);
    BigDecimal expectedNetWorth = calc.calculateTotal();

    assertEquals(0, expectedNetWorth.compareTo(portfolio.getNetWorth()),
        "Net worth should match calculated value for single share");
  }

  @Test
  void get_net_worth_with_multiple_shares() {
    Share share1 = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    Share share2 = new Share(tesla, new BigDecimal("5"), new BigDecimal("200"));

    portfolio.addShare(share1);
    portfolio.addShare(share2);

    SaleCalculator calc1 = new SaleCalculator(share1);
    SaleCalculator calc2 = new SaleCalculator(share2);
    BigDecimal expectedNetWorth = calc1.calculateTotal().add(calc2.calculateTotal());

    assertEquals(0, expectedNetWorth.compareTo(portfolio.getNetWorth()),
        "Net worth should sum all shares");
  }

  @Test
  void get_net_worth_with_price_increase() {
    Share share = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    portfolio.addShare(share);

    BigDecimal initialNetWorth = portfolio.getNetWorth();
    apple.addNewSalesPrice(new BigDecimal("150"));

    BigDecimal updatedNetWorth = portfolio.getNetWorth();
    assertTrue(updatedNetWorth.compareTo(initialNetWorth) > 0,
        "Net worth should increase when stock price increases");
  }

  @Test
  void add_null_share_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> portfolio.addShare(null),
        "Adding null share should throw IllegalArgumentException");
  }

  @Test
  void remove_null_share_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> portfolio.removeShare(null),
        "Removing null share should throw IllegalArgumentException");
  }

  @Test
  void remove_non_existent_share_returns_false() {
    Share share1 = new Share(apple, new BigDecimal("10"), new BigDecimal("100"));
    Share share2 = new Share(tesla, new BigDecimal("5"), new BigDecimal("200"));

    portfolio.addShare(share1);

    assertFalse(portfolio.removeShare(share2), "Removing non-existent share should return false");
  }

  @Test
  void get_shares_with_null_symbol_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> portfolio.getShares(null),
        "Getting shares with null symbol should throw exception");
  }

  @Test
  void get_shares_with_empty_symbol_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> portfolio.getShares(""),
        "Getting shares with empty symbol should throw exception");
  }

  @Test
  void contains_null_share_throws_exception() {
    assertThrows(IllegalArgumentException.class,
        () -> portfolio.contains(null),
        "Checking contains with null share should throw exception");
  }

  @Test
  void empty_portfolio_operations() {
    assertTrue(portfolio.getShares().isEmpty(), "Empty portfolio should have empty shares list");
    assertEquals(0, BigDecimal.ZERO.compareTo(portfolio.getNetWorth()),
        "Empty portfolio net worth should be zero");
    List<Share> shares = portfolio.getShares("AAPL");
    assertTrue(shares.isEmpty(), "Querying empty portfolio should return empty list");
  }

  @Test
  void portfolio_with_many_shares() {
    for (int i = 0; i < 100; i++) {
      Share share = new Share(apple, new BigDecimal("1"), new BigDecimal("100"));
      portfolio.addShare(share);
    }

    assertEquals(100, portfolio.getShares().size(), "Portfolio should contain 100 shares");
    assertEquals(100, portfolio.getShares("AAPL").size(), "Should retrieve all 100 AAPL shares");
  }

  @Test
  void add_share_with_large_quantity() {
    Share share = new Share(apple, new BigDecimal("999999"), new BigDecimal("100"));
    portfolio.addShare(share);

    assertTrue(portfolio.contains(share), "Large quantity share should be added");
  }

  @Test
  void add_share_with_decimal_quantity() {
    Share share = new Share(apple, new BigDecimal("10.50"), new BigDecimal("100.25"));
    portfolio.addShare(share);

    assertTrue(portfolio.contains(share), "Decimal quantity share should be added");
  }
}