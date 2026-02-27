package edu.ntnu.idi.bidata.group5.model;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SaleTest {

    @Test
    void commit() {
        Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
        Share share = new Share(stock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));

        Player player = new Player("TestPlayer", BigDecimal.valueOf(10000));
        player.getPortfolio().addShare(share);
        Sale sale = new Sale(share, 1);
        sale.commit(player);

        assertTrue(sale.isCommitted());
    }

    // Negative test cases for commit method
    @Test
    void commitWhenCommited() {
        Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
        Share share = new Share(stock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));

        Player player = new Player("TestPlayer", BigDecimal.valueOf(10000));
        player.getPortfolio().addShare(share);
        Sale sale = new Sale(share, 1);
        sale.commit(player);

        assertThrows(IllegalStateException.class, () -> sale.commit(player));
    }

    @Test
    void commitWithNullPlayer() {
        Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
        Share share = new Share(stock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));

        Sale sale = new Sale(share, 1);
        assertThrows(IllegalArgumentException.class, () -> sale.commit(null));
    }

    @Test
    void commitWithNoShares() {
        Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
        Share share = new Share(stock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));

        Player player = new Player("TestPlayer", BigDecimal.valueOf(10000));
        Sale sale = new Sale(share, 1);
        assertThrows(IllegalStateException.class, () -> sale.commit(player));
    }



}