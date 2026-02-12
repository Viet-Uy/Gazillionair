package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

/**
 * Represents a sale transaction in the stock market game.
 */
public class Sale  extends Transaction {

  /**
   * Constructor for Sale.
   *
   * @param share the share being sold
   * @param week the week of the transaction
   */
  public Sale(Share share, int week) {
    super(share, week, new SaleCalculator(share));
  }

  /**
   * Commits the sale transaction, updating the player's portfolio and cash balance.
   *
   * @param player the player committing the sale
   */
  public void commit(Player player) {
    if (isCommitted()) {
      throw new IllegalStateException("Transaction already committed.");
    }

    if (!player.getPortfolio().contains(getShare())) {
      throw new IllegalStateException("Player does not own the share.");
    }

    BigDecimal totalValue = getCalculator().calculateTotal();

    player.addMoney(totalValue);
    player.getPortfolio().removeShare(getShare());
    player.getTransactionArchive().add(this);

    committed = true;
  }
}
