package edu.ntnu.idi.bidata.group5;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an archive of completed transactions.
 * The transaction archive stores all completed {@link Transaction} objects
 * and provides methods for retrieving transactions, filtering by type,
 * and calculating statistics such as the number of distinct trading weeks.
 */

public class TransactionArchive {

  /** List of all archived transactions. */
  private final List<Transaction> transactions;

  /**
   * Constructs an empty {@code TransactionArchive}.
   */
  public TransactionArchive() {
    transactions = new ArrayList<>();
  }

  /**
   * Adds a completed transaction to the archive.
   *
   * @param transaction the transaction to add
   * @throws IllegalArgumentException if the transaction is {@code null}
   */
  public void add(Transaction transaction) {
    if (transaction == null) {
      throw new IllegalArgumentException("Transaction cannot be null");
    }
    transactions.add(transaction);
  }

  /**
   * Checks whether the archive contains any transactions.
   *
   * @return {@code true} if the archive is empty, {@code false} otherwise
   */
  public boolean isEmpty() {
    return transactions.isEmpty();
  }

  /**
   * Returns all archived transactions.
   * A copy of the internal list is returned to preserve encapsulation.
   *
   * @return a list containing all archived transactions
   */
  public List<Transaction> getTransactions() {
    return new ArrayList<>(transactions);
  }

  /**
   * Returns all purchase transactions in the archive.
   *
   * @return a list of {@link Purchase} transactions
   */
  public List<Purchase> getPurchases() {
    List<Purchase> purchases = new ArrayList<>();
    for (Transaction transaction : transactions) {
      if (transaction instanceof Purchase) {
        purchases.add((Purchase) transaction);
      }
    }
    return purchases;
  }

  /**
   * Returns all sale transactions in the archive.
   *
   * @return a list of {@link Sale} transactions
   */
  public List<Sale> getSales() {
    List<Sale> sales = new ArrayList<>();
    for (Transaction transaction : transactions) {
      if (transaction instanceof Sale) {
        sales.add((Sale) transaction);
      }
    }
    return sales;
  }

  /**
   * Counts the number of distinct trading weeks represented in the archive.
   * Only positive week numbers are considered.
   *
   * @return the number of distinct weeks with at least one transaction
   */
  public int countDistinctWeeks() {
    return (int) transactions.stream()
      .map(Transaction::getWeek)
      .filter(w -> w > 0)
      .distinct()
      .count();
  }
}