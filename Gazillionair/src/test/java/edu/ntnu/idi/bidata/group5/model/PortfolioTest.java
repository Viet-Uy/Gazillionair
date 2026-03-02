package edu.ntnu.idi.bidata.group5.model;

import edu.ntnu.idi.bidata.group5.calculator.SaleCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioTest {

  @Test
  void addShareSuccessfully() {
    Share share = new Share(new Stock("AAPL","Apple",
            BigDecimal.valueOf(110)),
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(100)
    );
    Portfolio portfolio = new Portfolio();
    assertTrue(portfolio.addShare(share));
  }

  @Test
  void removeShareSuccessfully() {
    Share share = new Share(new Stock("AAPL","Apple",
            BigDecimal.valueOf(110)),
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(100)
    );
    Portfolio portfolio = new Portfolio();
    portfolio.addShare(share);
    assertTrue(portfolio.getShares().contains(share));
    assertTrue(portfolio.removeShare(share));
  }

  @Test
  void getSharesSuccessfully() {
    Share share1 = new Share(new Stock("AAPL1","Apple1",
            BigDecimal.valueOf(110)),
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(100)
    );
    Share share2 = new Share(new Stock("AAPL2","Apple2",
            BigDecimal.valueOf(120)),
            BigDecimal.valueOf(15),
            BigDecimal.valueOf(110)
    );
    Portfolio portfolio = new Portfolio();
    portfolio.addShare(share1);
    portfolio.addShare(share2);
    assertTrue(portfolio.getShares().contains(share1));
    assertTrue(portfolio.getShares().contains(share2));
  }

  @Test
  void getNetWorthSuccessfully() {
    Share share1 = new Share(new Stock("AAPL", "Apple",
        BigDecimal.valueOf(110)),
        BigDecimal.valueOf(10),
        BigDecimal.valueOf(100));
    Share share2 = new Share(new Stock("GOOGL", "Google",
        BigDecimal.valueOf(120)),
        BigDecimal.valueOf(15),
        BigDecimal.valueOf(110));
    Portfolio portfolio = new Portfolio();
    portfolio.addShare(share1);
    portfolio.addShare(share2);

    SaleCalculator calc1 = new SaleCalculator(share1);
    SaleCalculator calc2 = new SaleCalculator(share2);
    BigDecimal expectedNetWorth =
        calc1.calculateTotal().add(calc2.calculateTotal());

    assertEquals(0, expectedNetWorth.compareTo(portfolio.getNetWorth()));
  }


  @Test
  void getMultipleSharesBySymbolSuccessfully() {
    Share share1 = new Share(new Stock("AAPL","Apple",
            BigDecimal.valueOf(110)),
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(100));

    Share share2 = new Share(new Stock("AAPL","Apple",
            BigDecimal.valueOf(120)),
            BigDecimal.valueOf(15),
            BigDecimal.valueOf(110));

    Portfolio portfolio = new Portfolio();
    portfolio.addShare(share1);
    portfolio.addShare(share2);
    List<Share> results = portfolio.getShares("AAPL");
    assertEquals(2, results.size());
  }

  @Test
  void contains() {
    Share share = new Share(new Stock("AAPL","Apple", BigDecimal.valueOf(110)),BigDecimal.valueOf(10),
            BigDecimal.valueOf(100));

    Portfolio portfolio = new Portfolio();
    portfolio.addShare(share);
    assertTrue(portfolio.contains(share));

  }

  //Negative tests below

  @Test
  void addShareThrowsException() {
    Portfolio portfolio = new Portfolio();
    assertThrows(IllegalArgumentException.class, () -> portfolio.addShare(null));
  }

  @Test
  void removeShareThrowsException() {
    Portfolio portfolio = new Portfolio();
    assertThrows(IllegalArgumentException.class, () -> portfolio.removeShare(null));
  }

  @Test
    void getShareBySymbolSuccessfully() {
    Share share1 = new Share(new Stock("AAPL","Apple",
            BigDecimal.valueOf(110)),
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(100));
    Portfolio portfolio = new Portfolio();
    portfolio.addShare(share1);
    List<Share> results = portfolio.getShares("AAPL");
    assertEquals(1, results.size());
    assertEquals("AAPL", results.getFirst().getStock().getSymbol());
  }

  @Test
    void getSharesBySymbolThrowsException() {
    Portfolio portfolio = new Portfolio();
    assertThrows(IllegalArgumentException.class, () -> portfolio.getShares(null));
    assertThrows(IllegalArgumentException.class, () -> portfolio.getShares(""));
  }
}