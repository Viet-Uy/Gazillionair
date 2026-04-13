package edu.ntnu.idi.bidata.group5.service;

import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Share;

public class TransactionFactory {

  public Purchase createPurchase(Share share, int week) {
    return new Purchase(share, week);
  }

  public Sale createSale(Share share, int week) {
    return new Sale(share, week);
  }
}
