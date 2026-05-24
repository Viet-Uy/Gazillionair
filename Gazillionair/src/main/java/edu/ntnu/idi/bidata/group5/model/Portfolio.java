package edu.ntnu.idi.bidata.group5.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The Portfolio class represents a collection of shares owned by an investor.
 * It provides methods to manage the shares, calculate the total value of the portfolio,
 * and retrieve shares based on stock symbols.
 * The class ensures that shares are added and removed correctly.
 */
public class Portfolio {

  private final List<Share> shares;

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
    if (share == null) {
      throw new IllegalArgumentException("Share cannot be null");
    }
    return shares.add(share);
  }

  /**
   * Remove a share from the portfolio.
   *
   * @param share the share to remove
   * @return true if the share was removed, false otherwise
   */
  public boolean removeShare(Share share) {
    if (share == null) {
      throw new IllegalArgumentException("Share cannot be null");
    }

    return shares.remove(share);
  }

  /**
   * Returns a list of all shares in the portfolio.
   * Get shares returns a new ArrayList containing all shares in the portfolio to ensure that
   * the internal list of shares cannot be modified directly from outside the class.
   *
   * @return a list of all shares in the portfolio.
   */
  public List<Share> getShares() {
    return new ArrayList<>(shares);
  }

  /**
   * This one returns the shares for a given stock symbol.
   *
   * @param symbol the stock symbol
   * @return returning shares matching the stock symbol
   * @throws IllegalArgumentException if the symbol is null or empty
   */
  public List<Share> getShares(String symbol) {

    if (symbol == null || symbol.isEmpty()) {
      throw new IllegalArgumentException("Stock symbol cannot be null or empty");
    }

    return shares.stream()
        .filter(share ->
                share.getStock()
                .getSymbol()
                .equals(symbol)
        )
        .toList();

  }

  /**
   * Check if the portfolio contains the given share.
   *
   * @param share the share to check
   * @return true if the portfolio contains the share, false otherwise
   */
  public boolean contains(Share share) {
    if (share == null) {
      throw new IllegalArgumentException("Share cannot be null");
    }
    return shares.contains(share);
  }

  /**
   * Calculates the current market value of the portfolio based on stock sales prices.
   *
   * @return the current market value of all owned shares
   */
  public BigDecimal getNetWorth() {
    BigDecimal total = BigDecimal.ZERO;
    for (Share share : shares) {
      total = total.add(share.getStock().getSalesPrice().multiply(share.getQuantity()));
    }
    return total;
  }

}
