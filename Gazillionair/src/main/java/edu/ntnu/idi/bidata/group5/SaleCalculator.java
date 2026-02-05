package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

abstract class SaleCalculator implements TransactionCalculator {

  private final Share share;
  BigDecimal salesPrice;
  BigDecimal quantity;

  public SaleCalculator(Share share) {
    this.share = share;
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
    BigDecimal purchaseCost = share.purchasePrice.multiply(quantity);
    return calculateGross().subtract(calculateCommission().subtract(purchaseCost));
  }

  @Override
  public BigDecimal calculateTotal() {
    return calculateGross().subtract(calculateCommission()).subtract(calculateTax());
  }
}
