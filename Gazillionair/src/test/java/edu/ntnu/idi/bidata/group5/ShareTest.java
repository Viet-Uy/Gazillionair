package edu.ntnu.idi.bidata.group5;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ShareTest {
    @Test
    void testGetQuantity (){
        Stock exampleStock = new Stock ("AAPL", "Apple", BigDecimal.valueOf(10.00));
        Share share = new Share(exampleStock, BigDecimal.valueOf(10.00), BigDecimal.valueOf(150.00));
        assertEquals(BigDecimal.valueOf(10.00), share.getQuantity());
    }

    @Test
    void testGetPurchasePrice (){
        Stock exampleStock = new Stock ("AAPL", "Apple", BigDecimal.valueOf(10.00));
        Share share = new Share(exampleStock, BigDecimal.valueOf(10.00), BigDecimal.valueOf(150.00));
        assertEquals(BigDecimal.valueOf(150.00), share.getPurchasePrice());
    }

    @Test
    void testGetStock (){
        Stock exampleStock = new Stock ("AAPL", "Apple", BigDecimal.valueOf(10.00));
        Share share = new Share(exampleStock, BigDecimal.valueOf(10.00), BigDecimal.valueOf(150.00));
        assertEquals(exampleStock, share.getStock());
    }

    //Negative test cases for constructor

    @Test
    void stockThrowsException () {
        assertThrows(IllegalArgumentException.class, () -> new Share(null, BigDecimal.valueOf(10.00), BigDecimal.valueOf(150.00)));
    }

    @Test
    void quantityThrowsException () {
        assertThrows(IllegalArgumentException.class, () -> new Share(new Stock ("AAPL", "Apple", BigDecimal.valueOf(10.00)), BigDecimal.valueOf(-10.00), BigDecimal.valueOf(150.00)));
        assertThrows(IllegalArgumentException.class, () -> new Share(new Stock ("AAPL", "Apple", BigDecimal.valueOf(10.00)), null, BigDecimal.valueOf(150.00)));
    }

    @Test
    void purchasePriceThrowsException () {
        assertThrows(IllegalArgumentException.class, () -> new Share(new Stock ("AAPL", "Apple", BigDecimal.valueOf(10.00)), BigDecimal.valueOf(10.00), BigDecimal.valueOf(-150.00)));
        assertThrows(IllegalArgumentException.class, () -> new Share(new Stock ("AAPL", "Apple", BigDecimal.valueOf(10.00)), BigDecimal.valueOf(10.00), null));
    }


}

