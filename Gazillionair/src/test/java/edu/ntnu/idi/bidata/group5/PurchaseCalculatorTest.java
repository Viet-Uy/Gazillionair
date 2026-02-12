package edu.ntnu.idi.bidata.group5;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseCalculatorTest {

    @Test
    void calculateGross() {
        PurchaseCalculator purchaseCalculator = new PurchaseCalculator(new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(100)), BigDecimal.valueOf(10), BigDecimal.valueOf(50)));
        BigDecimal expectedGross = BigDecimal.valueOf(500); // 0 shares * $
        assertEquals(expectedGross, purchaseCalculator.calculateGross());
    }

    @Test
    void calculateCommission() {
        PurchaseCalculator purchaseCalculator = new PurchaseCalculator(new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(100)), BigDecimal.valueOf(10), BigDecimal.valueOf(50)));
        BigDecimal expectedCommission = BigDecimal.valueOf(2.500); // 0.5% of $500 gross
        assertEquals(0, expectedCommission.compareTo(purchaseCalculator.calculateCommission()));
    }

    @Test
    void calculateTax() {
        PurchaseCalculator purchaseCalculator = new PurchaseCalculator(new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(100)), BigDecimal.valueOf(10), BigDecimal.valueOf(50)));
        BigDecimal expectedTax = BigDecimal.ZERO; // No tax when buying
        assertEquals(expectedTax, purchaseCalculator.calculateTax());
    }

    @Test
    void calculateTotal() {
        PurchaseCalculator purchaseCalculator = new PurchaseCalculator(new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(100)), BigDecimal.valueOf(10), BigDecimal.valueOf(50)));
        BigDecimal expectedTotal = BigDecimal.valueOf(502.5); // $500 gross + $2.5 commission + $0 taxassertEquals(0,
        assertEquals(0,expectedTotal.compareTo(purchaseCalculator.calculateTotal()));
    }
}