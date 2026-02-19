package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

/**
 * Share class of the stocks, has quantities and purchasePrice.
 */
public class Share {

  Stock stock;
  BigDecimal quantity;
  BigDecimal purchasePrice;

  /**
   * Constructor.
   *
   * @param quantity quantity of how many shares.
   * @param purchasePrice price at the moment of purchase.
   */

  public Share(Stock stock, BigDecimal quantity, BigDecimal purchasePrice) {
    this.stock = stock;
    this.quantity = quantity;
    this.purchasePrice = purchasePrice;

    if (stock == null) {
      throw new IllegalArgumentException("Stock cannot be null.");
    }
    if (quantity == null ||quantity.compareTo(BigDecimal.ZERO) <= 0 ) {
      throw new IllegalArgumentException("Quantity must be greater than zero.");
    }
    if ( purchasePrice == null || purchasePrice.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Purchase price must be greater than zero.");
    }

  }

  public Stock getStock() {
    return stock;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public BigDecimal getPurchasePrice() {
    return purchasePrice;
  }

}
