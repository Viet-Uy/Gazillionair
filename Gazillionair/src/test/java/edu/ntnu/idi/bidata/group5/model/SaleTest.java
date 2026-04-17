package edu.ntnu.idi.bidata.group5.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class SaleTest {

  @Test
  void commitMarksSaleAsCommitted() {
    Share share = createShare(BigDecimal.valueOf(90), BigDecimal.valueOf(10));
    Player player = new Player("TestPlayer", BigDecimal.valueOf(10000));
    player.getPortfolio().addShare(share);
    Sale sale = new Sale(share, 1);
    sale.commit(player);
    assertTrue(sale.isCommitted());
  }

  @Test
  void commitWhenCommittedThrows() {
    Share share = createShare(BigDecimal.valueOf(90), BigDecimal.valueOf(10));
    Player player = new Player("TestPlayer", BigDecimal.valueOf(10000));
    player.getPortfolio().addShare(share);
    Sale sale = new Sale(share, 1);
    sale.commit(player);
    assertThrows(IllegalStateException.class, () -> sale.commit(player));
  }

  @Test
  void commitWithNullPlayerThrows() {
    Share share = createShare(BigDecimal.valueOf(90), BigDecimal.valueOf(10));
    Sale sale = new Sale(share, 1);
    assertThrows(IllegalArgumentException.class, () -> sale.commit(null));
  }

  @Test
  void commitWithNoSharesThrows() {
    Share share = createShare(BigDecimal.valueOf(90), BigDecimal.valueOf(10));
    Player player = new Player("TestPlayer", BigDecimal.valueOf(10000));
    Sale sale = new Sale(share, 1);
    assertThrows(IllegalStateException.class, () -> sale.commit(player));
  }

  @Test
  void commitWithPartialQuantityReducesOwnedBundle() {
    Share ownedShare = createShare(BigDecimal.valueOf(10), BigDecimal.valueOf(10));
    Share soldShare = createShare(BigDecimal.valueOf(2), BigDecimal.valueOf(10));
    Player player = new Player("TestPlayer", BigDecimal.valueOf(10000));
    player.getPortfolio().addShare(ownedShare);
    Sale sale = new Sale(soldShare, 1);
    sale.commit(player);

    assertTrue(sale.isCommitted());
    assertEquals(1, player.getPortfolio().getShares().size());
    assertEquals(
        0,
        player.getPortfolio().getShares().getFirst().getQuantity()
            .compareTo(BigDecimal.valueOf(8)));
  }

  @Test
  void commitCanConsumeQuantityAcrossMultipleOwnedBundles() {
    Share firstOwnedShare = createShare(BigDecimal.valueOf(10), BigDecimal.valueOf(8));
    Share secondOwnedShare = createShare(BigDecimal.valueOf(2), BigDecimal.valueOf(12));
    Share soldShare = createShare(BigDecimal.valueOf(12), BigDecimal.valueOf(8.666667));

    Player player = new Player("TestPlayer", BigDecimal.valueOf(10000));
    player.getPortfolio().addShare(firstOwnedShare);
    player.getPortfolio().addShare(secondOwnedShare);
    Sale sale = new Sale(soldShare, 1);
    sale.commit(player);

    assertTrue(sale.isCommitted());
    assertTrue(player.getPortfolio().getShares().isEmpty());
  }

  private Share createShare(BigDecimal quantity, BigDecimal purchasePrice) {
    Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
    return new Share(stock, quantity, purchasePrice);
  }
}
