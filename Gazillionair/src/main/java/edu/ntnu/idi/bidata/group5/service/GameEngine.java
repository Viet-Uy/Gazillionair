package edu.ntnu.idi.bidata.group5.service;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.util.List;

/**
 * Game engine operations that mutate session state.
 */
public class GameEngine {

  /**
   * Advances the game by one week.
   *
   * @param session current game session
   */
  public void advanceWeek(GameSession session) {
    nextWeek(session);
  }

  /**
   * Advances market prices one week, recomputes derived values, then notifies observers.
   *
   * @param session current game session
   */
  public void nextWeek(GameSession session) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    session.advanceExchangeWeek();
    session.refreshDerivedState();
    session.publishModelChanged();
  }

  /**
   * Hook for custom market-change strategies.
   *
   * @param stocks stocks to update
   * @param week current week number
   */
  public void applyMarketChanges(List<Stock> stocks, int week) {
    if (stocks == null) {
      throw new IllegalArgumentException("Stocks cannot be null");
    }
    if (week < 1) {
      throw new IllegalArgumentException("Week must be at least 1");
    }
  }
}
