package edu.ntnu.idi.bidata.group5.model;

import edu.ntnu.idi.bidata.group5.calculator.TransactionCalculator;

/**
 * Abstract class representing a transaction, either buy or sell.
 */
public abstract class Transaction {

  private final Share share;
  private final int week;
  private final TransactionCalculator calculator;
  protected boolean committed;

  /**
   * This constructor initializes the transaction with the given share, week, and calculator.
   *
   * @param share the share involved in the transaction
   * @param week the week of the transaction
   * @param calculator the calculator to compute the transaction details
   */
  protected Transaction(Share share, int week, TransactionCalculator calculator) {
    if (share == null) {
      throw new IllegalArgumentException("Share cannot be null");
    }
    if (week < 1) {
      throw new IllegalArgumentException("Week must be at least 1");
    }
    if (calculator == null) {
      throw new IllegalArgumentException("Calculator cannot be null");
    }

    this.share = share;
    this.week = week;
    this.calculator = calculator;
    this.committed = false;
  }

  /**
   * Getters for the transaction properties.
   *
   * @return the share involved in the transaction
   */
  public Share getShare() {
    return share;
  }

  /**
   * Get the week of the transaction.
   *
   * @return the week of the transaction
   */
  public int getWeek() {
    return week;
  }

  /**
   * Get the calculator associated with the transaction.
   *
   * @return the calculator for the transaction
   */
  public TransactionCalculator getCalculator() {
    return calculator;
  }

  /**
   * Check if the transaction has been committed.
   *
   * @return true if the transaction is committed, false otherwise.
   */
  public boolean isCommitted() {
    return committed;
  }

  /**
   * Commits the transaction, updating the player's portfolio and cash balance accordingly.
   *
   * @param player the player for whom the transaction is being committed
   * @throws IllegalArgumentException if the player is {@code null}
   * @throws IllegalStateException if the transaction has already been committed
   */
  public abstract void commit(Player player);

}
