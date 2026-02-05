package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

/**
 * Calculator for when the user is purchasing.
 */
public class PurchaseCalculator implements TransactionCalculator {

  BigDecimal purchasePrice;
  BigDecimal quantity;

  /**
   * Initializing share to get access to its functions.
   *
   * @param share to get access to the share prices and quantity.
   */
  public PurchaseCalculator(Share share) {
    this.purchasePrice = share.getPurchasePrice();
    this.quantity = share.getQuantity();
  }

  @Override
  public BigDecimal calculateGross() {
    return purchasePrice.multiply(quantity); /*Gross*/
  }

  @Override
  public BigDecimal calculateCommission() {
    return calculateGross().multiply(BigDecimal.valueOf(0.005)); /*Gross after commission */
  }

  @Override
  public BigDecimal calculateTax() {
    return BigDecimal.ZERO; /*No tax when buying*/
  }

  @Override
  public BigDecimal calculateTotal() {
    return calculateGross().add(calculateCommission()).add(calculateTax());
  }
}
