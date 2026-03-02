package edu.ntnu.idi.bidata.group5.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

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
    assertEquals(PlayerStatus.NOVICE, player.getStatus(0));
  }

  @Test
  void getStatusReturnsInvestor() {
    player.addMoney(new BigDecimal("200"));
    assertEquals(PlayerStatus.INVESTOR, player.getStatus(11));
  }

  @Test
  void getStatusReturnsSpeculator() {
    player.addMoney(new BigDecimal("1000"));
    assertEquals(PlayerStatus.SPECULATOR, player.getStatus(21));
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