package edu.ntnu.idi.bidata.group5;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
    portfolio.addShare(share);
    assertTrue(portfolio.getShares().contains(share));
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
    portfolio.removeShare(share);
    assertFalse(portfolio.getShares().contains(share));
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
  void getTotalValueSuccessfully() {
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
    BigDecimal expected = (share1.getStock().getSalesPrice().multiply(share1.getQuantity()).add
                          (share2.getStock().getSalesPrice().multiply(share2.getQuantity())));
    assertEquals(expected, portfolio.getTotalValue());
  }

  @Test
  void contains() {
  }
}