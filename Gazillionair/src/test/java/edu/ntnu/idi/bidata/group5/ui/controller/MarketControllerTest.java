package edu.ntnu.idi.bidata.group5.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketControllerTest {

  private MarketController controller;
  private GameSession session;

  @BeforeEach
  void setUp() {
    Stock apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
    Stock tesla = new Stock("TSLA", "Tesla", new BigDecimal("200"));
    session = new GameSession("Uy", new BigDecimal("1000"), List.of(apple, tesla));
  }

  @Test
  void constructorRejectsNullSession() {
    assertThrows(IllegalArgumentException.class,
        () -> new MarketController(null, null));
  }

  @Test
  void constructorRejectsNullView() {
    assertThrows(IllegalArgumentException.class,
        () -> new MarketController(session, null));
  }

  @Test
  void searchReturnsMatchingStocks() {
    List<Stock> allStocks = session.getMarketStocks();

    assertEquals(2, allStocks.size());
    assertEquals("AAPL", allStocks.getFirst().getSymbol());
  }

  @Test
  void buyExecutesTransaction() {
    Purchase purchase = session.buy("AAPL", 1);

    assertNotNull(purchase);
    assertTrue(purchase.isCommitted());
  }

  @Test
  void buyRejectsInvalidInput() {
    assertThrows(IllegalArgumentException.class, () -> session.buy(null, 1));
    assertThrows(IllegalArgumentException.class, () -> session.buy("AAPL", 0));
  }

  @Test
  void sellExecutesTransaction() {
    session.buy("AAPL", 1);

    Sale sale = session.sell("AAPL", 1);

    assertNotNull(sale);
    assertTrue(sale.isCommitted());
  }

  @Test
  void sellRejectsWhenNoOwnedShare() {
    assertThrows(IllegalStateException.class, () -> session.sell("AAPL", 1));
    assertThrows(IllegalArgumentException.class, () -> session.sell("AAPL", 0));
  }
}

