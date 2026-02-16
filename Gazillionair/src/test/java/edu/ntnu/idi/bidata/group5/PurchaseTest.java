package edu.ntnu.idi.bidata.group5;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseTest {

    @Test
    void commit() {
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
}