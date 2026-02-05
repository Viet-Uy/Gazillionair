package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

/**
 * Defining methods that are common in the buy and sell area of the program.
 */
public interface TransactionCalculator {

  /**
   * Calculates the gross value of the transaction before fees and taxes.
   *
   * @return the gross value of the transaction before fees and taxes.
   */
  BigDecimal calculateGross();

  /**
   * Calculates the commission amount.
   *
   * @return the commission amount
   */
  BigDecimal calculateCommission();

  /**
   * Calculates the tax amount applied to the transaction.
   *
   * @return the tax amount
   */
  BigDecimal calculateTax();

  /**
   * Calculates the total cost of the transaction including
   * gross amount, commission, and tax.
   *
   * @return the total transaction cost
   */
  BigDecimal calculateTotal();
}


