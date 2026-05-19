package edu.ntnu.idi.bidata.group5.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for complete game session workflows.
 * Tests real-world scenarios: buying, advancing weeks, selling, and verifying final state.
 */
class GameSessionIntegrationTest {

  private GameSession session;
  private Stock stock1;
  private Stock stock2;

  @BeforeEach
  void setUp() {
    stock1 = new Stock("AAPL", "Apple", new BigDecimal("100"));
    stock2 = new Stock("MSFT", "Microsoft", new BigDecimal("200"));
    List<Stock> stocks = List.of(stock1, stock2);
    session = new GameSession("TestPlayer", new BigDecimal("10000"), stocks);
  }

  @Test
  void completeGameSessionFlow() {
    // Buy AAPL at week 1
    BigDecimal initialCash = session.getCashBalance();
    Purchase purchase1 = session.buy("AAPL", 10);
    assertTrue(purchase1.isCommitted());

    // Verify cash decreased with commission (0.5%)
    BigDecimal expectedCost = new BigDecimal("1000").multiply(new BigDecimal("1.005"));
    BigDecimal expectedCash = initialCash.subtract(expectedCost);
    assertEquals(0, expectedCash.compareTo(session.getCashBalance()));
    assertEquals(1, session.getHoldings().size());

    // Advance 3 weeks
    for (int i = 0; i < 3; i++) {
      session.nextWeek();
    }
    assertEquals(4, session.getCurrentWeek());

    // Verify holdings still present
    assertEquals(1, session.getHoldings().size());
    assertEquals(0, new BigDecimal("10").compareTo(session.getHoldings().get(0).getQuantity()));

    // Sell AAPL at week 4 (potentially different price)
    BigDecimal cashBeforeSale = session.getCashBalance();
    Sale sale = session.sell("AAPL", 10);
    assertTrue(sale.isCommitted());

    // Verify holdings removed
    assertTrue(session.getHoldings().isEmpty());

    // Verify cash increased (minus 1% commission and capital gains tax if profit)
    BigDecimal cashAfterSale = session.getCashBalance();
    assertTrue(cashAfterSale.compareTo(cashBeforeSale) >= 0);

    // Verify transactions recorded
    assertEquals(2, session.getTransactions().size());
    assertEquals(1, session.getPurchases().size());
    assertEquals(1, session.getSales().size());
  }

  @Test
  void multiStockPortfolioManagement() {
    // Buy multiple stocks in week 1
    Purchase p1 = session.buy("AAPL", 5);
    Purchase p2 = session.buy("MSFT", 2);
    assertTrue(p1.isCommitted());
    assertTrue(p2.isCommitted());
    assertEquals(2, session.getHoldings().size());

    BigDecimal portfolioValueWeek1 = session.getPortfolioValue();
    assertNotNull(portfolioValueWeek1);

    // Advance week
    session.nextWeek();
    BigDecimal portfolioValueWeek2 = session.getPortfolioValue();

    // Portfolio value should exist (may increase or decrease)
    assertNotNull(portfolioValueWeek2);
    assertTrue(portfolioValueWeek2.compareTo(BigDecimal.ZERO) > 0);

    // Sell one stock
    Sale saleAapl = session.sell("AAPL", 5);
    assertTrue(saleAapl.isCommitted());

    // Verify one holding remains
    assertEquals(1, session.getHoldings().size());
    assertEquals(0, new BigDecimal("2")
        .compareTo(session.getHoldings().get(0).getQuantity()));

    // Sell remaining
    Sale saleMsft = session.sell("MSFT", 2);
    assertTrue(saleMsft.isCommitted());
    assertTrue(session.getHoldings().isEmpty());
  }

  @Test
  void longTermGameProgression() {
    // Week 1: Buy stock
    session.buy("AAPL", 20);

    // Simulate 10 weeks of trading
    for (int week = 0; week < 10; week++) {
      session.nextWeek();
      if (week % 3 == 0) {
        session.buy("MSFT", 1);
      }
    }

    // Verify multiple trades
    assertTrue(session.getTransactions().size() > 3);

    // Sell all holdings at week 11
    List<Sale> finalSales = session.sellAllHoldings();
    assertTrue(finalSales.size() > 0);
    assertTrue(session.getHoldings().isEmpty());

    // Verify final state
    BigDecimal finalNetWorth = session.getNetWorth();
    assertNotNull(finalNetWorth);
    assertTrue(finalNetWorth.compareTo(BigDecimal.ZERO) > 0);
  }

  @Test
  void verifyTransactionAccuracy() {
    // Buy AAPL at $100 per share, quantity 10
    // Gross: 1000, Commission: 5 (0.5%), Total cost: 1005
    BigDecimal cashBefore = session.getCashBalance();
    Purchase purchase = session.buy("AAPL", 10);

    BigDecimal expectedCost = new BigDecimal("1005"); // 10 * 100 * 1.005
    BigDecimal cashAfter = session.getCashBalance();
    assertEquals(0, cashBefore.subtract(cashAfter).compareTo(expectedCost));

    // Verify share is in portfolio at purchase price
    assertEquals(1, session.getHoldings().size());
    BigDecimal purchasePrice = session.getHoldings().get(0).getPurchasePrice();
    assertEquals(0, new BigDecimal("100").compareTo(purchasePrice));
  }

  @Test
  void verifyNetWorthTracking() {
    BigDecimal initialNetWorth = session.getNetWorth();
    assertEquals(0, initialNetWorth.compareTo(new BigDecimal("10000")));

    // Buy stock
    session.buy("AAPL", 10); // Costs 1005

    // After purchase: net worth should be less than initial (due to commission)
    BigDecimal netWorthAfterBuy = session.getNetWorth();
    assertTrue(netWorthAfterBuy.compareTo(new BigDecimal("9900")) > 0);
    assertTrue(netWorthAfterBuy.compareTo(new BigDecimal("10000")) < 0);

    // Advance week (price might change)
    session.nextWeek();
    BigDecimal netWorthAfterWeek = session.getNetWorth();
    assertNotNull(netWorthAfterWeek);
    assertTrue(netWorthAfterWeek.compareTo(BigDecimal.ZERO) > 0);
  }
}
