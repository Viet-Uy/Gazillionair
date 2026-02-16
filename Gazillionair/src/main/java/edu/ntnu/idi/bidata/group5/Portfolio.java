package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Portfolio class representing a collection of shares.
 */
public class Portfolio {

  List<Share> shares;

  /**
   * Constructor for Portfolio.
   * Contains Arraylist of shares.
   */
  public Portfolio() {
    this.shares = new ArrayList<>();
  }

  /**
   * Add a share to the portfolio.
   *
   * @param share the share to add
   * @return true if the share was added, false otherwise
   */
  public boolean addShare(Share share) {
    return shares.add(share);
  }

  /**
   * Remove a share from the portfolio.
   *
   * @param share the share to remove
   * @return true if the share was removed, false otherwise
   */
  public boolean removeShare(Share share) {
    return shares.remove(share);
  }

  public List<Share> getShares() {
    return shares;
  }

  /**
   * TBD, this one returns the shares for a given stock symbol.
   *
   * @param symbol the stock symbol
   * @return returning shares, TBD
   */
  public List<Share> getShares(String symbol) {
    return shares;
  }

  /**
   * Check if the portfolio contains the given share.
   *
   * @param share the share to check
   * @return true if the portfolio contains the share, false otherwise
   */
  public boolean contains(Share share) {
    return shares.contains(share);
  }

  /**
   * Calculates the total value of all shares in the portfolio.
   * The total value is computed by iterating through each {@link Share}
   * in the portfolio and multiplying the share's quantity by the stock's
   * current sales price. All individual values are then summed using
   * {@link java.math.BigDecimal} arithmetic to ensure precision.
   *
   * @return the total value of the portfolio as a {@link BigDecimal}
   */
  public BigDecimal getTotalValue() {
    BigDecimal totalValue = BigDecimal.ZERO;
    for (Share share : shares) {
      BigDecimal value = share.getStock().getSalesPrice().multiply(share.getQuantity());
      totalValue = totalValue.add(value);
    }
    return totalValue;
  }
}
