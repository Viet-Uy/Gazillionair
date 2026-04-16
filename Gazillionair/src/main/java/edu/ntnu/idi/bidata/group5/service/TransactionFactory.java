package edu.ntnu.idi.bidata.group5.service;

import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Share;

/**
 * Factory for creating transaction objects.
 */
public class TransactionFactory {

  /**
   * Creates a purchase transaction.
   *
   * @param share the share to buy
   * @param week transaction week
   * @return purchase transaction
   */
  public Purchase createPurchase(Share share, int week) {
    if (share == null) {
      throw new IllegalArgumentException("Share cannot be null");
    }
    return new Purchase(share, week);
  }

  /**
   * Creates a sale transaction.
   *
   * @param share the share to sell
   * @param week transaction week
   * @return sale transaction
   */
  public Sale createSale(Share share, int week) {
    if (share == null) {
      throw new IllegalArgumentException("Share cannot be null");
    }
    return new Sale(share, week);
  }
}
