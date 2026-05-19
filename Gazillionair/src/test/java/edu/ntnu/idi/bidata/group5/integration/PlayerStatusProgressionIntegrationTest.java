package edu.ntnu.idi.bidata.group5.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.PlayerStatus;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for player status progression.
 * Tests that player status correctly transitions from NOVICE to INVESTOR to SPECULATOR
 * based on weeks with trades and net worth growth.
 */
class PlayerStatusProgressionIntegrationTest {

  private GameSession session;
  private Stock stock;

  @BeforeEach
  void setUp() {
    stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
    List<Stock> stocks = List.of(stock);
    session = new GameSession("TestPlayer", new BigDecimal("10000"), stocks);
  }

  @Test
  void playerStartsAsNovice() {
    assertEquals(PlayerStatus.NOVICE, session.getPlayer().getStatus());
  }

  @Test
  void playerHasMultipleWeeksOfTradingRecorded() {
    // Trade in multiple weeks to verify countDistinctWeeks works
    session.buy("AAPL", 1); // Week 1
    assertEquals(1, session.getPlayer().getTransactionArchive().countDistinctWeeks());

    session.nextWeek(); // Week 2
    session.buy("AAPL", 1); // Week 2
    assertEquals(2, session.getPlayer().getTransactionArchive().countDistinctWeeks());

    session.nextWeek(); // Week 3
    session.buy("AAPL", 1); // Week 3
    assertEquals(3, session.getPlayer().getTransactionArchive().countDistinctWeeks());
  }

  @Test
  void statusBaseOnTradesNotCurrentWeek() {
    // Advance 20 weeks WITHOUT trading -> should be NOVICE
    for (int week = 0; week < 20; week++) {
      session.nextWeek();
    }

    // Even though we're at week 20, no trades means countDistinctWeeks = 0
    assertEquals(0, session.getPlayer().getTransactionArchive().countDistinctWeeks());
    assertEquals(PlayerStatus.NOVICE, session.getPlayer().getStatus());

    // Now make 1 trade
    session.buy("AAPL", 1);

    // Still at week 20, but only 1 trading week, so need more
    assertEquals(1, session.getPlayer().getTransactionArchive().countDistinctWeeks());
    assertEquals(PlayerStatus.NOVICE, session.getPlayer().getStatus());
  }

  @Test
  void netWorthGrowthAffectsStatus() {
    // No growth = NOVICE
    assertEquals(PlayerStatus.NOVICE, session.getPlayer().getStatus());

    // Add money to simulate growth
    session.getPlayer().addMoney(new BigDecimal("2000")); // 20% growth
    // Still NOVICE because no trading weeks
    assertEquals(PlayerStatus.NOVICE, session.getPlayer().getStatus());

    // Trade for 1 week with 20% growth
    session.buy("AAPL", 1);
    // Still NOVICE because only 1 week (needs 10 for INVESTOR)
    assertEquals(PlayerStatus.NOVICE, session.getPlayer().getStatus());
  }

  @Test
  void verifyInvestorRequirements() {
    // Just verify that if we have 10 trading weeks and adequate growth, we reach INVESTOR
    // The exact growth amount depends on stock price changes which are random

    // Trade in 10 different weeks
    for (int week = 0; week < 10; week++) {
      session.nextWeek();
      session.buy("AAPL", 1);
    }

    // Verify: 10 weeks of trading
    assertEquals(10, session.getPlayer().getTransactionArchive().countDistinctWeeks());

    // Add money to ensure growth threshold
    session.getPlayer().addMoney(new BigDecimal("3000")); // Add significant growth

    // Status should be INVESTOR (10 weeks + growth >= 1.2x)
    assertEquals(PlayerStatus.INVESTOR, session.getPlayer().getStatus());
  }

  @Test
  void verifySpeculatorRequirements() {
    // Trade in 20 weeks with high growth
    for (int week = 0; week < 20; week++) {
      session.nextWeek();
      session.buy("AAPL", 1);
    }

    // Verify: 20 weeks of trading
    assertEquals(20, session.getPlayer().getTransactionArchive().countDistinctWeeks());

    // Add substantial money for > 2x growth (need more than 10000 added)
    session.getPlayer().addMoney(new BigDecimal("12000")); // More than double

    // Status should be SPECULATOR (20 weeks + growth >= 2.0x)
    assertEquals(PlayerStatus.SPECULATOR, session.getPlayer().getStatus());
  }
}
