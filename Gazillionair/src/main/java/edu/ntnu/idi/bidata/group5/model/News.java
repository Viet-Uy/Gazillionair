package edu.ntnu.idi.bidata.group5.model;

/**
 * Represents a news article that can affect stock prices and player decisions.
 */
public class News {

  private final String headline;
  private final String content;
  private final String affectedStocks;
  private final int week;
  private final Sentiment sentiment;

  /**
   * Enum representing the sentiment of a news article.
   */
  public enum Sentiment {
    /**
     * Positive sentiment.
     */
    POSITIVE("positive"),
    /**
     * Negative sentiment.
     */
    NEGATIVE("negative"),
    /**
     * Neutral sentiment.
     */
    NEUTRAL("neutral");

    private final String value;

    /**
     * Constructor for Sentiment.
     *
     * @param value the string representation of the sentiment
     */
    Sentiment(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    /**
     * Converts a string to the corresponding Sentiment enum.
     *
     * @param value the string representation
     * @return the corresponding Sentiment, or NEUTRAL if not found
     */
    public static Sentiment fromString(String value) {
      for (Sentiment s : Sentiment.values()) {
        if (s.value.equalsIgnoreCase(value)) {
          return s;
        }
      }
      return NEUTRAL;
    }
  }

  /**
   * Creates a new News article.
   *
   * @param headline the news headline
   * @param content the news content/description
   * @param affectedStocks comma-separated stock symbols or "Market-wide"
   * @param week the week this news is relevant for
   * @param sentiment the sentiment type
   */
  public News(String headline, String content, String affectedStocks, int week,
      Sentiment sentiment) {
    if (headline == null || headline.isBlank()) {
      throw new IllegalArgumentException("Headline cannot be null or blank");
    }
    if (content == null || content.isBlank()) {
      throw new IllegalArgumentException("Content cannot be null or blank");
    }
    if (affectedStocks == null || affectedStocks.isBlank()) {
      throw new IllegalArgumentException("Affected stocks cannot be null or blank");
    }
    if (week < 1) {
      throw new IllegalArgumentException("Week must be at least 1");
    }
    if (sentiment == null) {
      throw new IllegalArgumentException("Sentiment cannot be null");
    }

    this.headline = headline;
    this.content = content;
    this.affectedStocks = affectedStocks;
    this.week = week;
    this.sentiment = sentiment;
  }

  public String getHeadline() {
    return headline;
  }

  public String getContent() {
    return content;
  }

  public String getAffectedStocks() {
    return affectedStocks;
  }

  public int getWeek() {
    return week;
  }

  public Sentiment getSentiment() {
    return sentiment;
  }

  public String getSentimentAsString() {
    return sentiment.getValue();
  }
}
