package edu.ntnu.idi.bidata.group5;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SaleCalculatorTest {

    @Test
    void calculateGross() {
        SaleCalculator saleCalculator = new SaleCalculator(new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(100)), BigDecimal.valueOf(90), BigDecimal.valueOf(10)));
        BigDecimal expectedGross = BigDecimal.valueOf(1000); // 10 shares * $100 sales price
        assertEquals(expectedGross, saleCalculator.calculateGross());
    }

    @Test
    void calculateCommission() {
        SaleCalculator saleCalculator = new SaleCalculator(new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(100)), BigDecimal.valueOf(90), BigDecimal.valueOf(10)));
        BigDecimal expectedCommission = BigDecimal.valueOf(10); // 1% of $1000 gross
        assertEquals(expectedCommission, saleCalculator.calculateCommission());
    }

    @Test
    void calculateTax() {
        SaleCalculator saleCalculator = new SaleCalculator(new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(100)), BigDecimal.valueOf(90), BigDecimal.valueOf(10)));
        BigDecimal expectedTax = BigDecimal.valueOf(100); // ($1000 gross - $10 commission) - ($900 purchase cost)
        assertEquals(expectedTax, saleCalculator.calculateTax());
    }

    @Test
    void calculateTotal() {
        SaleCalculator saleCalculator = new SaleCalculator(new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(100)), BigDecimal.valueOf(90), BigDecimal.valueOf(10)));
        BigDecimal expectedTotal = BigDecimal.valueOf(890); // $1000 gross - $10 commission - $100 tax
        assertEquals(expectedTotal, saleCalculator.calculateTotal());
    }
}