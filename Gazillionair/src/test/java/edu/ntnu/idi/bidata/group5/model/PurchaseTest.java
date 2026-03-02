package edu.ntnu.idi.bidata.group5.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseTest {

    @Test
    void commitPurchase() {
        // Create a stock and a share
        Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
        Share share = new Share(stock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));

        // Create a purchase and commit it
        Purchase purchase = new Purchase(share,1);

        Player player = new Player("TestPlayer", BigDecimal.valueOf(10000));
        purchase.commit(player);

        // Assert that the purchase is marked as committed
        assertTrue(purchase.isCommitted());
    }

    // Negative tests for commit method

    @Test
    void commitPurchaseWithNullPlayer() {
        // Create a stock and a share
        Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
        Share share = new Share(stock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));

        // Create a purchase and attempt to commit it with a null player
        Purchase purchase = new Purchase(share,1);
        assertThrows(IllegalArgumentException.class, () -> purchase.commit(null));
    }

    @Test
    void commitWhenCommitted() {
        // Create a stock and a share
        Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
        Share share = new Share(stock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));

        // Create a purchase and commit it
        Purchase purchase = new Purchase(share,1);
        Player player = new Player("TestPlayer", BigDecimal.valueOf(10000));
        purchase.commit(player);

        // Attempt to commit the same purchase again
        assertThrows(IllegalStateException.class, () -> purchase.commit(player));
    }

    @Test
    void commitWithInsufficientFunds() {
        // Create a stock and a share
        Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
        Share share = new Share(stock, BigDecimal.valueOf(90), BigDecimal.valueOf(10));

        // Create a purchase and attempt to commit it with insufficient funds
        Purchase purchase = new Purchase(share,1);
        Player player = new Player("TestPlayer", BigDecimal.valueOf(500)); // Not enough money
        assertThrows(IllegalStateException.class, () -> purchase.commit(player));
    }

}