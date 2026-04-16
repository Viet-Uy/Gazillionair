package edu.ntnu.idi.bidata.group5.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Stock;
import edu.ntnu.idi.bidata.group5.ui.view.MarketView;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketControllerTest {

  private MarketController controller;

  @BeforeEach
  void setUp() {
    Stock apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
    Stock tesla = new Stock("TSLA", "Tesla", new BigDecimal("200"));
    GameSession session = new GameSession("Uy", new BigDecimal("1000"), List.of(apple, tesla));
    MarketView marketView = new MarketView();  // ADD THIS LINE
    controller = new MarketController(session, marketView);  // Fix: use 'session' not 'GameSession'
  }

  @Test
  void constructorRejectsNullSession() {
    MarketView marketView = new MarketView();  // ADD THIS LINE
    assertThrows(IllegalArgumentException.class, () -> new MarketController(null, marketView));  // Fix: add marketView param
  }

  @Test
  void searchReturnsMatchingStocks() {
    List<Stock> matches = controller.search("AAP");
    List<Stock> allStocks = controller.search(" ");

    assertEquals(1, matches.size());
    assertEquals("AAPL", matches.getFirst().getSymbol());
    assertEquals(2, allStocks.size());
  }

  @Test
  void buyExecutesTransaction() {
    Purchase purchase = controller.buy("AAPL", 1);

    assertNotNull(purchase);
    assertTrue(purchase.isCommitted());
  }

  @Test
  void buyRejectsInvalidInput() {
    assertThrows(IllegalArgumentException.class, () -> controller.buy(null, 1));
    assertThrows(IllegalArgumentException.class, () -> controller.buy("AAPL", 0));
  }

  @Test
  void sellExecutesTransaction() {
    controller.buy("AAPL", 1);

    Sale sale = controller.sell("AAPL", 1);

    assertNotNull(sale);
    assertTrue(sale.isCommitted());
  }

  @Test
  void sellRejectsWhenNoOwnedShare() {
    assertThrows(IllegalStateException.class, () -> controller.sell("AAPL", 1));
    assertThrows(IllegalArgumentException.class, () -> controller.sell("AAPL", 0));
  }
}

