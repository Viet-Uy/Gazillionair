package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

/**
 * Purchase class representing a purchase transaction of shares.
 */
public class Purchase extends Transaction {

  /**
   * Constructor for Purchase.
   *
   * @param share the share being purchased
   * @param week the week of the transaction
   */
  public Purchase(Share share, int week) {
    super(share, week, new PurchaseCalculator(share));
  }

  @Override
  public void commit(Player player) {

    if (isCommitted()) {
      throw new IllegalStateException("Transaction already committed.");
    }

    BigDecimal totalCost = getCalculator().calculateTotal();

    if (player.getMoney().compareTo(totalCost) < 0) {
      throw new IllegalStateException("Not enough money to complete purchase.");
    }

    player.withdrawMoney(totalCost);
    player.getPortfolio().addShare(getShare());
    player.getTransactionArchive().add(this);

    committed = true;
  }
}
