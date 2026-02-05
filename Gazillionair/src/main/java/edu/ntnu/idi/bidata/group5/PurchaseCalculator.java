package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

abstract class PurchaseCalculator implements TransactionCalculator {

  private final Share share;
  BigDecimal purchasePrice;
  BigDecimal quantity;

  public PurchaseCalculator(Share share) {
    this.share = share;
  }

  @Override
  public BigDecimal calculateGross() {
    return share.getPurchasePrice().multiply(share.getQuantity()); /*Gross*/
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
