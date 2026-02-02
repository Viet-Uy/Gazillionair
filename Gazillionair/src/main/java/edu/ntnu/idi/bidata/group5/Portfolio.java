package edu.ntnu.idi.bidata.group5;

import java.util.List;

/**
 * Portfolio class representing a collection of shares.
 */
public class Portfolio {

  List<Share> shares;

  /**
   * Constructor for Portfolio.
   */
  public Portfolio() {

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

}
