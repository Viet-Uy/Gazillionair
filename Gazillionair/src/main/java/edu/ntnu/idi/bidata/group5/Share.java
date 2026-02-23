package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

/**
 * Share class of the stocks, has quantities and purchasePrice.
 */
public class Share {

  private final Stock stock;
  private final BigDecimal quantity;
  private final BigDecimal purchasePrice;

  /**
   * Share constructor, initializes the stock, quantity and purchase price of the share.
   *
   * @param stock the stock that is being bought or sold.
   * @param quantity quantity of how many shares.
   * @param purchasePrice price at the moment of purchase.
   */
  public Share(Stock stock, BigDecimal quantity, BigDecimal purchasePrice) {
    if (stock == null) {
      throw new IllegalArgumentException("Stock cannot be null.");
    }
    if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than zero.");
    }
    if (purchasePrice == null || purchasePrice.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Purchase price must be greater than zero.");
    }

    this.stock = stock;
    this.quantity = quantity;
    this.purchasePrice = purchasePrice;
  }

  /**
   * Getters for the stock, quantity and purchase price of the share.
   *
   * @return the stock, quantity and purchase price of the share.
   */
  public Stock getStock() {
    return stock;
  }

  /**
   * Get the quantity of the share.
   *
   * @return the quantity of the share.
   */
  public BigDecimal getQuantity() {
    return quantity;
  }

  /**
   * Get the purchase price of the share.
   *
   * @return the purchase price of the share.
   */
  public BigDecimal getPurchasePrice() {
    return purchasePrice;
  }

}
