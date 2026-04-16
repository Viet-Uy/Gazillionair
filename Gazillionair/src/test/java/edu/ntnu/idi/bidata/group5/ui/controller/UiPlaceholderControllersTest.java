package edu.ntnu.idi.bidata.group5.ui.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class UiPlaceholderControllersTest {

  @Test
  void portfolioControllerCanBeCreated() {
    assertNotNull(new PortfolioController());
  }

  @Test
  void transactionsControllerCanBeCreated() {
    assertNotNull(new TransactionsController());
  }

  @Test
  void statsControllerCanBeCreated() {
    assertNotNull(new StatsController());
  }
}

