package edu.ntnu.idi.bidata.group5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
  private Player player;

  @BeforeEach
  void setUp() {
    player = new Player("Player1", new BigDecimal(1000));
  }

  @Test
  void constructorInitializesFieldsCorrectly() {
    assertEquals("Player1", player.getName());
    assertEquals(new BigDecimal(1000), player.getStartingMoney());
    assertEquals(new BigDecimal(1000), player.getMoney());
    assertNotNull(player.getPortfolio());
    assertNotNull(player.getTransactionArchive());
  }

  @Test
  void addMoneyIncreaseBalance() {
    player.addMoney(new BigDecimal(500));
    assertEquals(new BigDecimal(1500), player.getMoney());
  }

  @Test
  void withdrawMoneyDecreaseBalance() {
    player.withdrawMoney(new BigDecimal(250));
    assertEquals(new BigDecimal(750), player.getMoney());
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