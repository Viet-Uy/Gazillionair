package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

/**
 * Share class of the stocks, has quantities and purchasePrice.
 */
public class Share {

  // Stock stock;

  BigDecimal quantity;
  BigDecimal purchasePrice;

  /**
   * Constructor.
   *
   * @param quantity quantity of how many shares.
   * @param purchasePrice price at the moment of purchase.
   */

  public Share(BigDecimal quantity, BigDecimal purchasePrice) {
    // this.stock = stock;
    this.quantity = quantity;
    this.purchasePrice = purchasePrice;

  }

  // public Stock getStock

  public BigDecimal getQuantity() {
    return quantity;
  }

  public BigDecimal getPurchasePrice() {
    return purchasePrice;
  }

}
