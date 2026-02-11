package edu.ntnu.idi.bidata.group5;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ShareTest {
    @Test
    void testGetQuantity (){
        Stock exampleStock = new Stock ("Apple", "AAPL", BigDecimal.valueOf(10.00));
        Share share = new Share(exampleStock, BigDecimal.valueOf(10.00), BigDecimal.valueOf(150.00));
        assertEquals(BigDecimal.valueOf(10.00), share.getQuantity());
    }
}
