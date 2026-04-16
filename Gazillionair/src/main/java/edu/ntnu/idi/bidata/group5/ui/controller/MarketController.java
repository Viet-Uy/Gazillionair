package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Stock;
import edu.ntnu.idi.bidata.group5.ui.view.MarketView;
import java.util.List;
import java.util.function.Consumer;

/**
 * MarketController manages the stock market interaction.
 * Handles search queries and buy/sell transactions via the MarketView.
 * Bridges UI input to the GameSession backend.
 */
public class MarketController {

  private final GameSession session;
  private final MarketView view;
  private Consumer<Stock> onStockSelected;

  /**
   * Constructs a MarketController with the given GameSession and MarketView.
   *
   * @param session the GameSession containing market data
   * @param view the MarketView for display
   * @throws IllegalArgumentException if session or view is null
   */
  public MarketController(GameSession session, MarketView view) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.session = session;
    this.view = view;
    initializeBindings();
  }

  /**
   * Initializes event bindings between view and controller.
   */
  private void initializeBindings() {
    view.getSearchInput()
        .textProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              List<Stock> results = search(newVal);
              view.updateStocks(results);
            });

    view.setOnRowSelected(
        stock -> {
          if (onStockSelected != null) {
            onStockSelected.accept(stock);
          }
        });

    List<Stock> allStocks = session.getMarketStocks();
    view.updateStocks(allStocks);
  }

  /**
   * Searches for stocks by symbol or company name.
   *
   * @param query the search query
   * @return list of matching stocks
   */
  public List<Stock> search(String query) {
    return session.searchStocks(query);
  }

  /**
   * Executes a buy transaction.
   *
   * @param symbol the stock symbol
   * @param quantity the number of shares to buy
   * @return Purchase containing transaction details
   */
  public Purchase buy(String symbol, int quantity) {
    return session.buy(symbol, quantity);
  }

  /**
   * Executes a sell transaction.
   *
   * @param symbol the stock symbol
   * @param quantity the number of shares to sell
   * @return Sale containing transaction details
   */
  public Sale sell(String symbol, int quantity) {
    return session.sell(symbol, quantity);
  }

  /**
   * Sets the callback when a stock row is selected.
   *
   * @param callback the callback function to invoke with selected stock
   */
  public void setOnStockSelected(Consumer<Stock> callback) {
    this.onStockSelected = callback;
  }

  /**
   * Refreshes the stock table with current market data.
   * Called when the GameSession notifies of changes (e.g., after nextWeek).
   */
  public void refreshStockTable() {
    List<Stock> currentStocks = session.getMarketStocks();
    view.updateStocks(currentStocks);
  }

  /**
   * Gets the associated MarketView.
   *
   * @return the MarketView
   */
  public MarketView getView() {
    return view;
  }
}
