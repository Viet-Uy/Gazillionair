package edu.ntnu.idi.bidata.group5.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PortfolioControllerTest {

  private PortfolioController controller;
  private GameSession session;

  @BeforeEach
  void setUp() {
    Stock apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
    Stock tesla = new Stock("TSLA", "Tesla", new BigDecimal("200"));
    session = new GameSession("Uy", new BigDecimal("2000"), List.of(apple, tesla));
    session.buy("AAPL", 1);
    session.buy("TSLA", 1);
    controller = new PortfolioController(session);
  }

  @Test
  void constructorRejectsNullSession() {
    assertThrows(IllegalArgumentException.class, () -> new PortfolioController(null));
  }

  @Test
  void returnsPortfolioData() {
    assertTrue(controller.hasHoldings());
    assertEquals(2, controller.getHoldings().size());
    assertTrue(controller.getPortfolioValue().compareTo(BigDecimal.ZERO) > 0);
    assertTrue(controller.getCashBalance().compareTo(BigDecimal.ZERO) > 0);
  }

  @Test
  void sellDelegatesToSession() {
    Sale sale = controller.sell("AAPL", 1);
    assertTrue(sale.isCommitted());
    assertEquals(1, controller.getHoldings().size());
  }

  @Test
  void sellRejectsInvalidInput() {
    assertThrows(IllegalArgumentException.class, () -> controller.sell(null, 1));
    assertThrows(IllegalArgumentException.class, () -> controller.sell("AAPL", 0));
    assertThrows(IllegalStateException.class, () -> controller.sell("AAPL", 5));
  }

  @Test
  void sellAllSellsEverything() {
    List<Sale> sales = controller.sellAll();
    assertEquals(2, sales.size());
    assertFalse(controller.hasHoldings());
  }

  @Test
  void sellAllForSymbolSellsOnlyChosenStock() {
    session.buy("AAPL", 2);

    Sale sale = controller.sellAllForSymbol("AAPL");

    assertTrue(sale.isCommitted());
    assertEquals(0, controller.getOwnedQuantity("AAPL").compareTo(BigDecimal.ZERO));
    assertTrue(controller.getOwnedQuantity("TSLA").compareTo(BigDecimal.ZERO) > 0);
  }
}

