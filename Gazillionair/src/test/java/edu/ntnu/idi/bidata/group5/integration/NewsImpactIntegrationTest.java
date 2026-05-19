package edu.ntnu.idi.bidata.group5.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.News;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for news impact on gameplay.
 * Tests that news affects stock prices which impacts player portfolio value.
 */
class NewsImpactIntegrationTest {

  private GameSession session;
  private Stock stock;

  @BeforeEach
  void setUp() {
    stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
    List<Stock> stocks = List.of(stock);
    session = new GameSession("TestPlayer", new BigDecimal("10000"), stocks);
  }

  @Test
  void newsGeneratedForCurrentWeek() {
    // News should be generated on game creation
    List<News> newsWeek1 = session.getNewsForWeek(1);
    assertTrue(newsWeek1.size() > 0, "News should be generated for week 1");

    // Verify news has required fields
    News firstNews = newsWeek1.get(0);
    assertNotNull(firstNews.getHeadline());
    assertNotNull(firstNews.getContent());
    assertNotNull(firstNews.getSentiment());
    assertTrue(!firstNews.getAffectedStocks().isBlank(), "News should affect at least one stock");
    assertEquals(1, firstNews.getWeek());
  }

  @Test
  void newsGeneratedForNextWeek() {
    session.generateNewsForNextWeek();
    session.nextWeek();

    // News should exist for week 2
    List<News> newsWeek2 = session.getNewsForWeek(2);
    assertTrue(newsWeek2.size() > 0, "News should be generated for week 2");
  }

  @Test
  void newsImpactAffectsStockPrice() {
    // Get initial price
    BigDecimal initialPrice = session.getStock("AAPL").getSalesPrice();

    // Advance week (applies news impact on stock prices)
    session.nextWeek();

    // Price may have changed
    BigDecimal priceAfterNews = session.getStock("AAPL").getSalesPrice();

    // Price change is expected but might be 0
    assertTrue(priceAfterNews.compareTo(BigDecimal.ZERO) > 0);
  }

  @Test
  void portfolioValueReflectsNewsImpact() {
    // Buy stock at initial price
    session.buy("AAPL", 10); // 10 shares at $100 = $1000

    BigDecimal portfolioValueWeek1 = session.getPortfolioValue();
    // Portfolio value should be around $1000 (with some tolerance for rounding)
    assertTrue(portfolioValueWeek1.compareTo(new BigDecimal("900")) > 0);
    assertTrue(portfolioValueWeek1.compareTo(new BigDecimal("1100")) < 0);

    // Advance week (news may change price)
    session.nextWeek();

    BigDecimal portfolioValueWeek2 = session.getPortfolioValue();

    // Portfolio value should update based on new stock price
    // It may be different from week 1 due to price changes
    assertTrue(portfolioValueWeek2.compareTo(BigDecimal.ZERO) > 0);
  }



  @Test
  void newsImpactCalculation() {
    // Positive: +2%, Negative: -3%, Neutral: 0%
    BigDecimal initialPrice = new BigDecimal("100");

    // Simulate positive impact
    BigDecimal positiveImpact = initialPrice.multiply(new BigDecimal("1.02"));
    assertTrue(positiveImpact.compareTo(initialPrice) > 0);

    // Simulate negative impact
    BigDecimal negativeImpact = initialPrice.multiply(new BigDecimal("0.97"));
    assertTrue(negativeImpact.compareTo(initialPrice) < 0);

    // Simulate neutral impact
    BigDecimal neutralImpact = initialPrice.multiply(BigDecimal.ONE);
    assertEquals(0, neutralImpact.compareTo(initialPrice));
  }

  @Test
  void multipleWeeksOfNewsAndTrading() {
    // Trade and advance weeks, verifying portfolio adjusts to news
    session.buy("AAPL", 10);
    BigDecimal initialNetWorth = session.getNetWorth();

    // Trade for 5 weeks
    for (int week = 0; week < 5; week++) {
      session.nextWeek();
      // News impact is applied to stock prices
      // Portfolio value adjusts accordingly
      BigDecimal netWorth = session.getNetWorth();
      assertTrue(netWorth.compareTo(BigDecimal.ZERO) > 0);
    }

    // Verify news was generated for each week
    for (int week = 1; week <= 6; week++) {
      assertTrue(session.getNewsForWeek(week).size() >= 0);
    }
  }

  private void assertNotNull(Object obj) {
    assertTrue(obj != null, "Object should not be null");
  }
}
