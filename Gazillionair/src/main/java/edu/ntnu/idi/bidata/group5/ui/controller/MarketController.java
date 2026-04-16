package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.util.List;

/**
 * MarketController class is responsible for handling the market operations,
 * such as searching for stocks, buying and selling stocks.
 * It interacts with the GameSession to perform these operations and updates the
 */
public class MarketController {

  private final GameSession session;

  /**
   * Constructor for the MarketController, initializes the GameSession.
   *
   * @param session the GameSession to be used by the controller.
   * @throws IllegalArgumentException if the session is null.
   */
  public MarketController(GameSession session) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    this.session = session;
  }

  /**
   * Searches for stocks based on the provided query string. The search is case-insensitive
   * and matches stocks whose symbol or name contains the query string.
   *
   * @param query the search query string used to find matching stocks.
   * @return a list of stocks that match the search query, or an empty list if no matches are found.
   */
  public List<Stock> search(String query) {
    return session.searchStocks(query);
  }

  /**
   * Buys a specified quantity of a stock identified by its symbol.
   * The method interacts with the GameSession to execute the purchase and,
   * returns a Purchase object containing details of the transaction.
   *
   * @param symbol the stock symbol of the stock to be purchased.
   * @param quantity the quantity of the stock to be purchased.
   * @return a Purchase object containing details of the completed purchase transaction.
   */
  public Purchase buy(String symbol, int quantity) {
    return session.buy(symbol, quantity);
  }

  /**
   * Sells a specified quantity of a stock identified by its symbol.
   * The method interacts with the GameSession to execute the sale and,
   * returns a Sale object containing details of the transaction.
   *
   * @param symbol the stock symbol of the stock to be sold.
   * @param quantity the quantity of the stock to be sold.
   * @return a Sale object containing details of the completed sale transaction.
   */
  public Sale sell(String symbol, int quantity) {
    return session.sell(symbol, quantity);
  }
}
