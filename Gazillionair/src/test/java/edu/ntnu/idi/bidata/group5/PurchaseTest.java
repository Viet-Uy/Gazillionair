package edu.ntnu.idi.bidata.group5;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseTest {

    @Test
    void commit() {
        // Create a stock and a share
        Stock stock = new Stock("AAPL", "Apple", null);
        Share share = new Share(stock, null, BigDecimal.valueOf(10));

        // Create a purchase and commit it
        Purchase purchase = new Purchase(share,1);

        Player player = new Player("TestPlayer", BigDecimal.valueOf(1000));
        purchase.commit(player);

        // Assert that the purchase is marked as committed
        assertTrue(purchase.isCommitted());
    }
}