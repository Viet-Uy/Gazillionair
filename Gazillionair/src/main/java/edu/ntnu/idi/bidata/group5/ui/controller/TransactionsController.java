package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Transaction;
import java.util.List;

/**
 * TransactionsController class is responsible for handling the logic related,
 * to the transactions view in the UI.
 */
public class TransactionsController {

  private final GameSession session;

  /**
   * Creates a controller for transaction history actions.
   *
   * @param session current game session
   */
  public TransactionsController(GameSession session) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    this.session = session;
  }

  /**
   * Returns all transactions.
   *
   * @return transaction history
   */
  public List<Transaction> getTransactions() {
    return session.getTransactions();
  }

  /**
   * Returns transactions in a given week.
   *
   * @param week week number
   * @return transactions for week
   */
  public List<Transaction> getTransactionsForWeek(int week) {
    return session.getTransactionsForWeek(week);
  }

  /**
   * Returns all purchase transactions.
   *
   * @return purchase history
   */
  public List<Purchase> getPurchases() {
    return session.getPurchases();
  }

  /**
   * Returns all sale transactions.
   *
   * @return sale history
   */
  public List<Sale> getSales() {
    return session.getSales();
  }
}
