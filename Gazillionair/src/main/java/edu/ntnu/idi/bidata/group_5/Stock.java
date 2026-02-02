package edu.ntnu.idi.bidata.group_5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a stock that is traded on an exchange.
 * A stock has a unique symbol , a company name, and a history of sales prices.
 * The current sales prices is defined as the most recently added price/Last price
 * in the list.
 *
 */
public class Stock {

  /** The unique stock symbol (e.g. "AAPL"). */
  private final String symbol;

  /** The name of the company issuing the stock. */
  private final String company;

  /** A list containing the historical sales prices of the stock. */
  private final List<BigDecimal> prices;

  /**
   * Constructs a new Stock with initial sales prices.
   *
   * @param symbol the unique stock symbol
   * @param company the name of the company
   * @param salesPrice the initial sales price of the stock
   */
  public Stock(String symbol, String company, BigDecimal salesPrice) {
    this.symbol = symbol;
    this.company = company;
    prices = new ArrayList<>();
    prices.add(salesPrice);
  }

  /**
   * Returns the stock's symbol.
   *
   * @return the stock symbol
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * Returns the name of the company.
   *
   * @return the company name
   */
  public String getCompany() {
    return company;
  }

  /**
   * Returns the current sales price of the stock.
   * The current sales price is defined by the most recently added price
   * in the price history.
   *
   * @return the current sales price
   */
  public BigDecimal getSalesPrice() {
    return prices.getLast();
  }

  /**
   * Adds a new sales price to the stock's price history.
   *
   * @param newPrice the new sales price to add
   * @return the updated current sales price
   */
  public BigDecimal addNewSalesPrice(BigDecimal newPrice) {
    prices.add(newPrice);
    return getSalesPrice();
  }
}
