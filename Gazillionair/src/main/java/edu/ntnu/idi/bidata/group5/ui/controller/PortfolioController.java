package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Share;
import java.math.BigDecimal;
import java.util.List;

/**
 * PortfolioController class is responsible for handling the logic related,
 * to the portfolio view in the UI.
 * It will be used to handle the logic related to the portfolio view,
 * such as displaying the user's portfolio, updating the portfolio,
 * and other relevant logic related to the portfolio view.
 */
public class PortfolioController {

  private final GameSession session;

  /**
   * Creates a controller for portfolio actions.
   *
   * @param session current game session
   */
  public PortfolioController(GameSession session) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    this.session = session;
  }

  /**
   * Returns current portfolio holdings.
   *
   * @return holdings
   */
  public List<Share> getHoldings() {
    return session.getHoldings();
  }

  /**
   * Returns total portfolio market value.
   *
   * @return portfolio value
   */
  public BigDecimal getPortfolioValue() {
    return session.getPortfolioValue();
  }

  /**
   * Returns available cash balance.
   *
   * @return cash balance
   */
  public BigDecimal getCashBalance() {
    return session.getCashBalance();
  }

  /**
   * Returns whether portfolio has holdings.
   *
   * @return true if holdings exist
   */
  public boolean hasHoldings() {
    return session.hasHoldings();
  }

  /**
   * Sells a specific holding quantity by symbol.
   *
   * @param symbol stock symbol
   * @param quantity quantity to sell
   * @return committed sale
   */
  public Sale sell(String symbol, int quantity) {
    return session.sell(symbol, quantity);
  }

  /**
   * Sells a specific holding quantity by symbol.
   *
   * @param symbol stock symbol
   * @param quantity quantity to sell
   * @return committed sale
   */
  public Sale sell(String symbol, BigDecimal quantity) {
    return session.sell(symbol, quantity);
  }

  /**
   * Returns total owned quantity for a stock symbol.
   *
   * @param symbol stock symbol
   * @return total quantity owned
   */
  public BigDecimal getOwnedQuantity(String symbol) {
    if (symbol == null || symbol.isBlank()) {
      throw new IllegalArgumentException("Symbol cannot be null or blank");
    }
    return session.getHoldings().stream()
        .filter(share -> share.getStock().getSymbol().equalsIgnoreCase(symbol))
        .map(Share::getQuantity)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Sells all shares for a single stock symbol.
   *
   * @param symbol stock symbol
   * @return committed sale
   */
  public Sale sellAllForSymbol(String symbol) {
    BigDecimal ownedQuantity = getOwnedQuantity(symbol);
    if (ownedQuantity.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalStateException("No holdings for selected stock");
    }
    return session.sell(symbol, ownedQuantity);
  }

  /**
   * Sells all holdings in one operation.
   *
   * @return committed sale list
   */
  public List<Sale> sellAll() {
    return session.sellAllHoldings();
  }
}
