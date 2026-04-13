package edu.ntnu.idi.bidata.group5.service;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.util.List;

public class GameEngine {

  public void advanceWeek(GameSession session) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    session.nextWeek();
  }

  public void applyMarketChanges(List<Stock> stocks, int week) {
    if (stocks == null) {
      throw new IllegalArgumentException("Stocks cannot be null");
    }
    if (week < 1) {
      throw new IllegalArgumentException("Week must be at least 1");
    }
  }
}
