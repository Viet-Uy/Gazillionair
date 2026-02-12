package edu.ntnu.idi.bidata.group5;

/**
 * Represents a sale transaction in the stock market game.
 */
public class Sale  extends Transaction {

  /**
   * Constructor for Sale.
   *
   * @param share the share being sold
   * @param week the week of the transaction
   */
  public Sale(Share share, int week) {
    super(share, week, new SaleCalculator(share));
  }

  /**
   * Commits the sale transaction, updating the player's portfolio and cash balance.
   *
   * @param player the player committing the sale
   */
  public void commit(Player player) {
    // implement later
  }
}
