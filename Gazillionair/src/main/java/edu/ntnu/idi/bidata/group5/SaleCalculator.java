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
    return calculateGross().subtract(calculateCommission().subtract(purchaseCost));
  }

  @Override
  public BigDecimal calculateTotal() {
    return calculateGross().subtract(calculateCommission()).subtract(calculateTax());
  }
}
