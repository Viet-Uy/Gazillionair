package edu.ntnu.idi.bidata.group5.model;

import edu.ntnu.idi.bidata.group5.calculator.SaleCalculator;
import java.math.BigDecimal;

/**
 * Represents a sale transaction in the stock market game.
 */
public class Sale extends Transaction {

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
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null.");
    }

    if (isCommitted()) {
      throw new IllegalStateException("Transaction already committed.");
    }

    BigDecimal remainingToSell = getShare().getQuantity();
    java.util.List<Share> matchingShares =
        player.getPortfolio().getShares(getShare().getStock().getSymbol());
    BigDecimal availableQuantity = matchingShares.stream()
        .map(Share::getQuantity)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    if (availableQuantity.compareTo(remainingToSell) < 0) {
      throw new IllegalStateException("Player does not own the share.");
    }

    for (Share ownedShare : matchingShares) {
      if (remainingToSell.compareTo(BigDecimal.ZERO) <= 0) {
        break;
      }
      player.getPortfolio().removeShare(ownedShare);
      if (ownedShare.getQuantity().compareTo(remainingToSell) > 0) {
        BigDecimal newQuantity = ownedShare.getQuantity().subtract(remainingToSell);
        Share remainderShare = new Share(
            ownedShare.getStock(), newQuantity, ownedShare.getPurchasePrice());
        player.getPortfolio().addShare(remainderShare);
        remainingToSell = BigDecimal.ZERO;
      } else {
        remainingToSell = remainingToSell.subtract(ownedShare.getQuantity());
      }
    }

    BigDecimal totalValue = getCalculator().calculateTotal();

    player.addMoney(totalValue);
    player.getTransactionArchive().add(this);

    committed = true;
  }
}
