package edu.ntnu.idi.bidata.group5.file;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO structure for persisted game state JSON.
 */
public class GameStateData {

  public String playerName;
  public BigDecimal startingCapital;
  public BigDecimal cashBalance;
  public int currentWeek;
  public List<StockData> stocks;
  public List<ShareData> holdings;
  public List<TransactionData> transactions;
  public List<NewsData> news;

  /**
   * Persisted stock record.
   */
  public static class StockData {
    public String symbol;
    public String company;
    public List<BigDecimal> prices;
  }

  /**
   * Persisted share record.
   */
  public static class ShareData {
    public String symbol;
    public BigDecimal quantity;
    public BigDecimal purchasePrice;
  }

  /**
   * Persisted transaction record.
   */
  public static class TransactionData {
    public String type;
    public String symbol;
    public BigDecimal quantity;
    public BigDecimal purchasePrice;
    public int week;
  }

  /**
   * Persisted news record.
   */
  public static class NewsData {
    public String headline;
    public String content;
    public String affectedStocks;
    public int week;
    public String sentiment;
  }
}
