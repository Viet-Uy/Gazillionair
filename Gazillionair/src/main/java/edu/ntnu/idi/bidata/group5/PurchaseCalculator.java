package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

/**
 * Calculator for when the user is purchasing.
 */
public class PurchaseCalculator implements TransactionCalculator {

  private final BigDecimal purchasePrice;
  private final BigDecimal quantity;

  /**
   * Initializing share to get access to its functions.
   *
   * @param share to get access to the share prices and quantity.
   */
  public PurchaseCalculator(Share share) {
    if (share == null) {
      throw new IllegalArgumentException("Share cannot be null");
    }
    this.purchasePrice = share.getPurchasePrice();
    this.quantity = share.getQuantity();
  }

  @Override
  public BigDecimal calculateGross() {
    return purchasePrice.multiply(quantity); // Gross
  }

  @Override
  public BigDecimal calculateCommission() {
    return calculateGross().multiply(BigDecimal.valueOf(0.005)); // 0.5% commission when buying
  }

  @Override
  public BigDecimal calculateTax() {
    return BigDecimal.ZERO; // No tax when buying
  }

  @Override
  public BigDecimal calculateTotal() {
    return calculateGross().add(calculateCommission()).add(calculateTax());
  }
}
