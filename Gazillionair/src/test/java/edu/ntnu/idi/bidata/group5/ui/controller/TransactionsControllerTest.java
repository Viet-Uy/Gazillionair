package edu.ntnu.idi.bidata.group5.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Transaction;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionsControllerTest {

  private TransactionsController controller;

  @BeforeEach
  void setUp() {
    Stock apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
    GameSession session = new GameSession("Uy", new BigDecimal("2000"), List.of(apple));
    session.buy("AAPL", 1);
    session.nextWeek();
    session.sell("AAPL", 1);
    controller = new TransactionsController(session);
  }

  @Test
  void constructorRejectsNullSession() {
    assertThrows(IllegalArgumentException.class, () -> new TransactionsController(null));
  }

  @Test
  void returnsTransactionViews() {
    assertEquals(2, controller.getTransactions().size());
    assertEquals(1, controller.getPurchases().size());
    assertEquals(1, controller.getSales().size());
  }

  @Test
  void filtersTransactionsByWeek() {
    assertEquals(1, controller.getTransactionsForWeek(1).size());
    assertEquals(1, controller.getTransactionsForWeek(2).size());
  }

  @Test
  void getTransactionsForWeekRejectsInvalidWeek() {
    assertThrows(IllegalArgumentException.class, () -> controller.getTransactionsForWeek(0));
    assertThrows(IllegalArgumentException.class, () -> controller.getTransactionsForWeek(-1));
  }

  @Test
  void purchasesAndSalesAreCommitted() {
    assertTrue(controller.getPurchases().stream().allMatch(purchase -> purchase.isCommitted()));
    assertTrue(controller.getSales().stream().allMatch(sale -> sale.isCommitted()));
  }

  @Test
  void filterTransactionsSupportsTypeWeekAndSearch() {
    List<Transaction> purchases = controller.filterTransactions("Purchases", "All Weeks", "");
    List<Transaction> weekTwo = controller.filterTransactions("All Types", "Week 2", "");
    List<Transaction> appleSearch = controller.filterTransactions("All Types", "All Weeks", "app");

    assertEquals(1, purchases.size());
    assertEquals(1, weekTwo.size());
    assertEquals(2, appleSearch.size());
  }

  @Test
  void getWeekFilterOptionsReturnsRecordedWeeks() {
    assertEquals(List.of("Week 1", "Week 2"), controller.getWeekFilterOptions());
  }

  @Test
  void getTransactionDetailsReturnsReadableSummary() {
    String details = controller.getTransactionDetails(controller.getTransactions().getFirst());

    assertTrue(details.contains("Type: Purchase"));
    assertTrue(details.contains("Symbol: AAPL"));
    assertTrue(details.contains("Gross: $100.00"));
  }
}

