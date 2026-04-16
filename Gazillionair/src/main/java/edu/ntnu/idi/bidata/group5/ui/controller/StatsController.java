package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import java.util.List;

/**
 * StatsController class is responsible for handling the logic related to the stats view in the UI.
 * It will be used to handle the logic related to the stats view, such as displaying the
 * user's stats, updating the stats, and other relevant logic related to the stats view.
 */
public class StatsController {

  private final GameSession session;

  /**
   * Creates a controller for stats view operations.
   *
   * @param session current game session
   */
  public StatsController(GameSession session) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    this.session = session;
  }

  /**
   * Returns top gaining stocks.
   *
   * @param limit number of stocks
   * @return gainer list
   */
  public List<Stock> getTopGainers(int limit) {
    return session.getTopGainers(limit);
  }

  /**
   * Returns top losing stocks.
   *
   * @param limit number of stocks
   * @return loser list
   */
  public List<Stock> getTopLosers(int limit) {
    return session.getTopLosers(limit);
  }

  /**
   * Returns stock data for a symbol.
   *
   * @param symbol stock symbol
   * @return stock
   */
  public Stock getStock(String symbol) {
    return session.getStock(symbol);
  }

  /**
   * Returns historical prices for a symbol.
   *
   * @param symbol stock symbol
   * @return price history
   */
  public List<BigDecimal> getStockPriceHistory(String symbol) {
    return session.getStockPriceHistory(symbol);
  }

  /**
   * Returns whether at least one weekly market update has happened.
   *
   * @return true when week-based price changes exist
   */
  public boolean hasWeeklyPriceChanges() {
    return session.getCurrentWeek() > 1;
  }
}
