package edu.ntnu.idi.bidata.group5;

/**
 * Purchase class representing a purchase transaction of shares.
 */
public class Purchase extends Transaction {

  /**
   * Constructor for Purchase.
   *
   * @param share the share being purchased
   * @param week the week of the transaction
   */
  public Purchase(Share share, int week) {
    super(share, week, new PurchaseCalculator(share));
  }

  @Override
  public void commit(/*Player player*/) {
    // Implement later
  }
}
