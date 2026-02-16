package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

/**
 * Abstract class representing a transaction, either buy or sell.
 */
public abstract class Transaction {

  Share share;
  int week;
  TransactionCalculator calculator;
  protected boolean committed;

  /**
   * This constructor initializes the transaction with the given share, week, and calculator.
   *
   * @param share the share involved in the transaction
   * @param week the week of the transaction
   * @param calculator the calculator to compute the transaction details
   */
  protected Transaction(Share share, int week, TransactionCalculator calculator) {
    this.share = share;
    this.week = week;
    this.calculator = calculator;
    this.committed = false;
  }

  public Share getShare() {
    return share;
  }

  public int getWeek() {
    return week;
  }

  public TransactionCalculator getCalculator() {
    return calculator;
  }

  public boolean isCommitted() {
    return committed;
  }

  /**
   * Commits the transaction, updating the player's portfolio and cash balance accordingly.
   *
   * @throws IllegalStateException if the transaction has already been committed
   */
  public abstract void commit(Player player);

}
