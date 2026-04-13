package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.util.List;

public class MarketController {

  private final GameSession session;

  public MarketController(GameSession session) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    this.session = session;
  }

  public List<Stock> search(String query) {
    return session.searchStocks(query);
  }

  public Purchase buy(String symbol, int quantity) {
    return session.buy(symbol, quantity);
  }

  public Sale sell(String symbol, int quantity) {
    return session.sell(symbol, quantity);
  }
}
