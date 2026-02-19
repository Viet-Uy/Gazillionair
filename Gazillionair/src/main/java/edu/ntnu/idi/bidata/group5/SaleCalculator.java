package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

/**
 * SaleCalculator class implements TransactionCalculator interface
 * to perform calculations related to stock sales.
 */
public class SaleCalculator implements TransactionCalculator {

  BigDecimal purchasePrice;
  BigDecimal salesPrice;
  BigDecimal quantity;

  /**
   * Constructor and initializing variables from Share class.
   *
   * @param share to access share class.
   */
  public SaleCalculator(Share share) {
    if (share == null) {
      throw new IllegalArgumentException("Share cannot be null");
    }

    this.purchasePrice = share.getPurchasePrice();
    this.salesPrice = share.getStock().getSalesPrice();
    this.quantity = share.getQuantity();
  }

  @Override
  public BigDecimal calculateGross() {
    return salesPrice.multiply(quantity);
  }

  @Override
  public BigDecimal calculateCommission() {
    return calculateGross().multiply(BigDecimal.valueOf(0.01));
  }

  @Override
  public BigDecimal calculateTax() {
    BigDecimal purchaseCost = purchasePrice.multiply(quantity);
    BigDecimal profit = calculateGross().subtract(calculateCommission()).subtract(purchaseCost);
    return BigDecimal.ZERO.max(profit).multiply(BigDecimal.valueOf(0.30));
  }

  @Override
  public BigDecimal calculateTotal() {
    return calculateGross().subtract(calculateCommission()).subtract(calculateTax());
  }
}
