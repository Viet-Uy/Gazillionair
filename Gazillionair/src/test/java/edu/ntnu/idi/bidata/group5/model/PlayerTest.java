package edu.ntnu.idi.bidata.group5.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {

  private Player player;

  @BeforeEach
  void setUp() {
    player = new Player("Player1", new BigDecimal("1000"));
  }

  @Test
  void constructorInitializesFieldsCorrectly() {
    assertEquals("Player1", player.getName());
    assertEquals(0, player.getStartingMoney().compareTo(new BigDecimal("1000")));
    assertEquals(0, player.getMoney().compareTo(new BigDecimal("1000")));
    assertNotNull(player.getPortfolio());
    assertNotNull(player.getTransactionArchive());
  }

  @Test
  void constructorThrowsOnNullName() {
    assertThrows(IllegalArgumentException.class,
        () -> new Player(null, new BigDecimal("1000")));
  }

  @Test
  void constructorThrowsOnBlankName() {
    assertThrows(IllegalArgumentException.class,
        () -> new Player("   ", new BigDecimal("1000")));
  }

  @Test
  void constructorThrowsOnNullStartingMoney() {
    assertThrows(IllegalArgumentException.class,
        () -> new Player("Test", null));
  }

  @Test
  void constructorThrowsOnNegativeStartingMoney() {
    assertThrows(IllegalArgumentException.class,
        () -> new Player("Test", new BigDecimal("-100")));
  }

  @Test
  void constructorThrowsOnZeroStartingMoney() {
    assertThrows(IllegalArgumentException.class,
        () -> new Player("Test", BigDecimal.ZERO));
  }

  @Test
  void getNetWorthReturnsCurrentBalance() {
    assertEquals(0, player.getNetWorth().compareTo(new BigDecimal("1000")));
  }

  @Test
  void addMoneyIncreaseBalance() {
    BigDecimal result = player.addMoney(new BigDecimal("500"));
    assertEquals(0, result.compareTo(new BigDecimal("1500")));
  }

  @Test
  void addMoneyWithNegativeAmount() {
    assertThrows(IllegalArgumentException.class,
        () -> player.addMoney(new BigDecimal("-100")));
  }

  @Test
  void getStatusReturnsNovice() {
    assertEquals(PlayerStatus.NOVICE, player.getStatus());
  }

  @Test
  void getStatusReturnsInvestor() {
    Player richPlayer = new Player("Investor", new BigDecimal("10000"));
    Stock stock = new Stock("AAPL", "Apple", new BigDecimal("1"));
    for (int i = 0; i < 10; i++) {
      Share share = new Share(stock, new BigDecimal("1"), stock.getSalesPrice());
      new Purchase(share, i + 1).commit(richPlayer);
    }
    richPlayer.addMoney(new BigDecimal("2001")); // pushes net worth above 1.2x starting
    assertEquals(PlayerStatus.INVESTOR, richPlayer.getStatus());
  }

  @Test
  void getStatusReturnsSpeculator() {
    Player richPlayer = new Player("Speculator", new BigDecimal("10000"));
    Stock stock = new Stock("AAPL", "Apple", new BigDecimal("1"));
    for (int i = 0; i < 20; i++) {
      Share share = new Share(stock, new BigDecimal("1"), stock.getSalesPrice());
      new Purchase(share, i + 1).commit(richPlayer);
    }
    richPlayer.addMoney(new BigDecimal("10001")); // pushes net worth above 2x starting
    assertEquals(PlayerStatus.SPECULATOR, richPlayer.getStatus());
  }

  @Test
  void getTradingWeeksReturnsDistinctTradingWeeks() {
    Stock stock = new Stock("AAPL", "Apple", new BigDecimal("1"));
    Share shareOne = new Share(stock, new BigDecimal("1"), stock.getSalesPrice());
    Share shareTwo = new Share(stock, new BigDecimal("1"), stock.getSalesPrice());
    Share shareThree = new Share(stock, new BigDecimal("1"), stock.getSalesPrice());

    new Purchase(shareOne, 1).commit(player);
    new Purchase(shareTwo, 1).commit(player);
    new Purchase(shareThree, 2).commit(player);

    assertEquals(2, player.getTradingWeeks());
  }

  @Test
  void getStatusProgressTextShowsInvestorRequirementsForNovice() {
    String progressText = player.getStatusProgressText();

    assertEquals(
        "Status Progress: 0/10 trading weeks for INVESTOR | Growth: +0.00% / +20.00%",
        progressText);
  }

  @Test
  void addMoneyWithNullAmount() {
    assertThrows(IllegalArgumentException.class,
        () -> player.addMoney(null));
  }

  @Test
  void withdrawMoneyDecreaseBalance() {
    BigDecimal result = player.withdrawMoney(new BigDecimal("250"));
    assertEquals(0, result.compareTo(new BigDecimal("750")));
  }

  @Test
  void withdrawMoneyWithInsufficientFunds() {
    assertThrows(IllegalStateException.class,
        () -> player.withdrawMoney(new BigDecimal("1500")));
  }

  @Test
  void withdrawMoneyWithNegativeAmount() {
    assertThrows(IllegalArgumentException.class,
        () -> player.withdrawMoney(new BigDecimal("-100")));
  }

  @Test
  void withdrawMoneyWithNullAmount() {
    assertThrows(IllegalArgumentException.class,
        () -> player.withdrawMoney(null));
  }

  @Test
  void portfolioInitializedEmpty() {
    assertTrue(player.getPortfolio().getShares().isEmpty());
  }

  @Test
  void transactionArchiveInitializedEmpty() {
    assertTrue(player.getTransactionArchive().isEmpty());
  }
}
