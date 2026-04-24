package edu.ntnu.idi.bidata.group5.service;

import edu.ntnu.idi.bidata.group5.model.News;
import edu.ntnu.idi.bidata.group5.model.News.Sentiment;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates random news articles for each game week.
 */
public class NewsGenerator {

  private static final Random random = new Random();

  private static final String[] POSITIVE_HEADLINES = {
      "Strong Earnings Beat Market Expectations",
      "Market-Wide Momentum Suggests Expansion",
      "Sector Growth Accelerates Quarter-Over-Quarter",
      "Analyst Upgrades Drive Optimism",
      "Consumer Spending Surges Beyond Forecast",
      "Tech Innovation Drives Industry Transformation",
      "Supply Chain Improvements Boost Efficiency"
  };

  private static final String[] NEGATIVE_HEADLINES = {
      "Supply Chain Volatility Escalates",
      "Energy Markets Experience Compression",
      "Tech Sector Infrastructure Under Scrutiny",
      "Geopolitical Tensions Impact Markets",
      "Economic Data Falls Short of Estimates",
      "Regulatory Changes Threaten Profitability",
      "Commodity Prices Spike on Supply Concerns"
  };

  private static final String[] NEUTRAL_HEADLINES = {
      "Consumer Sentiment Shows Mixed Signals",
      "Market Consolidation Period Continues",
      "Analyst Outlook Remains Cautious",
      "Corporate Restructuring Underway",
      "Market Volatility Remains Elevated",
      "New Compliance Requirements Proposed",
      "Industry Leadership Transitions"
  };

  private static final String[] POSITIVE_CONTENT = {
      "Latest quarterly results exceed analyst expectations, signaling robust market"
          + " conditions. Strong performance across multiple sectors suggests sustained"
          + " economic growth.",
      "Macroeconomic indicators reflect tentative optimism. Several leading indices show"
          + " early signs of upward trajectory formation.",
      "Consumer demand remains resilient with robust spending patterns observed."
          + " Discretionary sector shows unexpected strength in recent transactions.",
      "Technology sector demonstrates innovation momentum. New product launches and service"
          + " expansions drive competitive advantage.",
      "Employment data strengthens outlook. Job market resilience supports consumer"
          + " confidence and spending potential."
  };

  private static final String[] NEGATIVE_CONTENT = {
      "Industry analysts report increased logistical pressure affecting multiple commodity"
          + " suppliers. Global container rates have experienced notable fluctuation.",
      "Fuel commodity prices exhibit compressed volatility amid geopolitical uncertainty."
          + " Hedging activity accelerates across energy sector participants.",
      "New compliance requirements proposed for cloud service providers. Implementation"
          + " timeline remains uncertain pending legislative review.",
      "Market research indicates divergent spending patterns across demographics."
          + " Discretionary spending categories demonstrate uneven momentum.",
      "Trade tensions escalate, creating uncertainty in supply chains. Import-dependent"
          + " industries brace for potential margin pressure."
  };

  private static final String[] NEUTRAL_CONTENT = {
      "Market observers note consolidation patterns emerging. Recent trading volumes suggest"
          + " repositioning of investor portfolios.",
      "Regulatory agencies announce review of existing market standards. Implementation"
          + " timeline and impact remain subject to further clarity.",
      "Corporate executives announce strategic initiatives and organizational changes."
          + " Transition period expected over coming quarters.",
      "Market participants show mixed sentiment as traditional metrics prove inconclusive."
          + " Technical analysis suggests sideways movement ahead.",
      "Industry experts debate emerging market trends. Consensus remains elusive on"
          + " longer-term directional indicators."
  };

  private static final String[] STOCK_GROUPS = {
      "TECH",
      "AAPL, MSFT",
      "TSLA, F",
      "JNJ, PFE",
      "XOM, CVX",
      "WMT, TGT",
      "AMZN",
      "Market-wide"
  };

  /**
   * Generates a random news article.
   *
   * @param week the week number for this news
   * @return a randomly generated News article
   */
  public News generateNews(int week) {
    if (week < 1) {
      throw new IllegalArgumentException("Week must be at least 1");
    }

    Sentiment sentiment = getRandomSentiment();
    String headline = getHeadlineForSentiment(sentiment);
    String content = getContentForSentiment(sentiment);
    String stocks = getRandomStocks();

    return new News(headline, content, stocks, week, sentiment);
  }

  /**
   * Generates multiple news articles for a given week.
   *
   * @param week the week number
   * @param count the number of articles to generate
   * @return list of randomly generated News articles
   */
  public List<News> generateNewsForWeek(int week, int count) {
    if (week < 1) {
      throw new IllegalArgumentException("Week must be at least 1");
    }
    if (count < 1) {
      throw new IllegalArgumentException("Count must be at least 1");
    }

    List<News> newsList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      newsList.add(generateNews(week));
    }
    return newsList;
  }

  private Sentiment getRandomSentiment() {
    int rand = random.nextInt(100);
    if (rand < 30) {
      return Sentiment.POSITIVE;
    } else if (rand < 60) {
      return Sentiment.NEGATIVE;
    } else {
      return Sentiment.NEUTRAL;
    }
  }

  private String getHeadlineForSentiment(Sentiment sentiment) {
    switch (sentiment) {
      case POSITIVE:
        return POSITIVE_HEADLINES[random.nextInt(POSITIVE_HEADLINES.length)];
      case NEGATIVE:
        return NEGATIVE_HEADLINES[random.nextInt(NEGATIVE_HEADLINES.length)];
      default:
        return NEUTRAL_HEADLINES[random.nextInt(NEUTRAL_HEADLINES.length)];
    }
  }

  private String getContentForSentiment(Sentiment sentiment) {
    switch (sentiment) {
      case POSITIVE:
        return POSITIVE_CONTENT[random.nextInt(POSITIVE_CONTENT.length)];
      case NEGATIVE:
        return NEGATIVE_CONTENT[random.nextInt(NEGATIVE_CONTENT.length)];
      default:
        return NEUTRAL_CONTENT[random.nextInt(NEUTRAL_CONTENT.length)];
    }
  }

  private String getRandomStocks() {
    return STOCK_GROUPS[random.nextInt(STOCK_GROUPS.length)];
  }
}
