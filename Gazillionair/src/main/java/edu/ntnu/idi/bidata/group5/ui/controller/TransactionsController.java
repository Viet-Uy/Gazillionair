package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

/**
 * TransactionsController class is responsible for handling the logic related,
 * to the transactions view in the UI.
 */
public class TransactionsController {

  private final GameSession session;

  /**
   * Creates a controller for transaction history actions.
   *
   * @param session current game session
   */
  public TransactionsController(GameSession session) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    this.session = session;
  }

  /**
   * Returns all transactions.
   *
   * @return transaction history
   */
  public List<Transaction> getTransactions() {
    return session.getTransactions();
  }

  /**
   * Returns transactions in a given week.
   *
   * @param week week number
   * @return transactions for week
   */
  public List<Transaction> getTransactionsForWeek(int week) {
    return session.getTransactionsForWeek(week);
  }

  /**
   * Returns all purchase transactions.
   *
   * @return purchase history
   */
  public List<Purchase> getPurchases() {
    return session.getPurchases();
  }

  /**
   * Returns all sale transactions.
   *
   * @return sale history
   */
  public List<Sale> getSales() {
    return session.getSales();
  }

  /**
   * Returns available week filters based on archived transactions.
   *
   * @return ordered list of week labels
   */
  public List<String> getWeekFilterOptions() {
    return getTransactions().stream()
        .map(Transaction::getWeek)
        .distinct()
        .sorted()
        .map(week -> "Week " + week)
        .toList();
  }

  /**
   * Returns transactions filtered by type, week, and symbol/company query.
   *
   * @param typeFilter selected type filter
   * @param weekFilter selected week filter
   * @param searchQuery selected symbol/company query
   * @return filtered transactions
   */
  public List<Transaction> filterTransactions(String typeFilter,
                                              String weekFilter,
                                              String searchQuery) {
    String normalizedType = typeFilter == null ? "All Types" : typeFilter;
    String normalizedWeek = weekFilter == null ? "All Weeks" : weekFilter;
    String normalizedQuery =
        searchQuery == null ? "" : searchQuery.trim().toLowerCase(Locale.ROOT);

    return getTransactions().stream()
        .filter(transaction -> matchesTypeFilter(transaction, normalizedType))
        .filter(transaction -> matchesWeekFilter(transaction, normalizedWeek))
        .filter(transaction -> matchesSearchQuery(transaction, normalizedQuery))
        .toList();
  }

  /**
   * Formats a transaction as detailed display text.
   *
   * @param transaction transaction to describe
   * @return detailed transaction summary
   */
  public String getTransactionDetails(Transaction transaction) {
    if (transaction == null) {
      return "Select a transaction to view details.";
    }

    return "Type: "
        + transaction.getClass().getSimpleName()
        + "\nSymbol: "
        + transaction.getShare().getStock().getSymbol()
        + "\nCompany: "
        + transaction.getShare().getStock().getCompany()
        + "\nWeek: "
        + transaction.getWeek()
        + "\nQuantity: "
        + transaction.getShare().getQuantity().stripTrailingZeros().toPlainString()
        + "\nPrice: "
        + formatMoney(transaction.getShare().getStock().getSalesPrice())
        + "\nGross: "
        + formatMoney(transaction.getCalculator().calculateGross())
        + "\nCommission: "
        + formatMoney(transaction.getCalculator().calculateCommission())
        + "\nTax: "
        + formatMoney(transaction.getCalculator().calculateTax())
        + "\nTotal: "
        + formatMoney(transaction.getCalculator().calculateTotal());
  }

  private boolean matchesTypeFilter(Transaction transaction, String typeFilter) {
    if ("Purchases".equals(typeFilter)) {
      return transaction instanceof Purchase;
    }
    if ("Sales".equals(typeFilter)) {
      return transaction instanceof Sale;
    }
    return true;
  }

  private boolean matchesWeekFilter(Transaction transaction, String weekFilter) {
    if ("All Weeks".equals(weekFilter)) {
      return true;
    }
    return weekFilter.equals("Week " + transaction.getWeek());
  }

  private boolean matchesSearchQuery(Transaction transaction, String searchQuery) {
    if (searchQuery.isBlank()) {
      return true;
    }
    String symbol = transaction.getShare().getStock().getSymbol().toLowerCase(Locale.ROOT);
    String company = transaction.getShare().getStock().getCompany().toLowerCase(Locale.ROOT);
    return symbol.contains(searchQuery) || company.contains(searchQuery);
  }

  private String formatMoney(BigDecimal amount) {
    return "$" + amount.setScale(2, RoundingMode.HALF_UP);
  }
}
