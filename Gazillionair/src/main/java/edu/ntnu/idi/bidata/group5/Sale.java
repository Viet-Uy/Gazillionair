package edu.ntnu.idi.bidata.group5;

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

  @Override
  public void commit(/*Player player*/) {
    // Implement later
  }
}
