package edu.ntnu.idi.bidata.group_5;


import java.math.BigDecimal;
import java.sql.SQLOutput;

public class Main {
    static void main() {
        Stock stock = new Stock("AAPL","Apple", BigDecimal.valueOf(10000));

      System.out.println(stock.addNewSalesPrice(BigDecimal.valueOf(10000)));
    }
}
