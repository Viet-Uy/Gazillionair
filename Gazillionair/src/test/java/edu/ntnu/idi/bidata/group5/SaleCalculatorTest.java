package edu.ntnu.idi.bidata.group5;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SaleCalculatorTest {

    @Test
    void calculateGross() {
        SaleCalculator saleCalculator = new SaleCalculator(new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(100)), BigDecimal.valueOf(90), BigDecimal.valueOf(10)));
        BigDecimal expectedGross = BigDecimal.valueOf(9000); // 90 shares * $100 sales price
        assertEquals(expectedGross, saleCalculator.calculateGross());
    }

    @Test
    void calculateCommission() {
        SaleCalculator saleCalculator = new SaleCalculator(new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(100)), BigDecimal.valueOf(90), BigDecimal.valueOf(10)));
        BigDecimal expectedCommission = BigDecimal.valueOf(90); // 1% of $9000 gross
        assertEquals(0,expectedCommission.compareTo(saleCalculator.calculateCommission()));
    }

    @Test
    void calculateTax() {
        SaleCalculator saleCalculator = new SaleCalculator(new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(100)), BigDecimal.valueOf(90), BigDecimal.valueOf(10)));
        BigDecimal expectedTax = BigDecimal.valueOf(2403); // 30% of ($9000 gross - $90 commission - $900 purchase cost)
        assertEquals(0, expectedTax.compareTo(saleCalculator.calculateTax()) );
    }

    @Test
    void calculateTotal() {
        SaleCalculator saleCalculator = new SaleCalculator(new Share(new Stock("AAPL", "Apple", BigDecimal.valueOf(100)), BigDecimal.valueOf(90), BigDecimal.valueOf(10)));
        BigDecimal expectedTotal = BigDecimal.valueOf(6507); // $9000 gross - $90 commission - $2403 tax
        assertEquals(0,expectedTotal.compareTo(saleCalculator.calculateTotal()) );
    }
}