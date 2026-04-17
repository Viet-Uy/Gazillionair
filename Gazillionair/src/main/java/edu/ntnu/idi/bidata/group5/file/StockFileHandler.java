package edu.ntnu.idi.bidata.group5.file;

import edu.ntnu.idi.bidata.group5.model.Stock;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The StockFileHandler class is responsible for handling file operations related to stock data.
 * This may include reading stock information from files, writing stock data to files, and managing
 * the storage of stock-related information for the trading game.
 */
public class StockFileHandler {

  /**
   * Reads stock data from a file.
   *
   * @param filePath the path to the file to read from
   * @return a list of stocks read from the file
   * @throws IOException if the file format is invalid or an I/O error occurs
   */
  public List<Stock> readFromFile(String filePath) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      return parseStocks(reader);
    }
  }

  /**
   * Reads stock data from a classpath resource.
   *
   * @param resourcePath classpath resource path (for example "/sp500.csv")
   * @return a list of stocks read from the resource
   * @throws IOException if resource is missing, invalid, or unreadable
   */
  public List<Stock> readFromResource(String resourcePath) throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(resourcePath);
    if (inputStream == null) {
      throw new IOException("Resource not found: " + resourcePath);
    }
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      return parseStocks(reader);
    }
  }

  private List<Stock> parseStocks(BufferedReader reader) throws IOException {
    List<Stock> stocks = new ArrayList<>();
    int lineNumber = 0;
    String line;

    while ((line = reader.readLine()) != null) {
      lineNumber++;

      if (line.isBlank() || line.startsWith("#")) {
        continue;
      }

      String[] parts = line.split(",");

      if (parts.length != 3) {
        throw new IOException("Invalid format at line " + lineNumber);
      }

      String symbol = parts[0].trim();
      String name = parts[1].trim();

      if (symbol.isEmpty() || name.isEmpty()) {
        throw new IOException("Empty field at line " + lineNumber);
      }

      try {
        BigDecimal price = new BigDecimal(parts[2].trim());
        stocks.add(new Stock(symbol, name, price));
      } catch (IllegalArgumentException e) {
        throw new IOException("Bad data at line " + lineNumber, e);
      }
    }

    return stocks;
  }

  /**
   * Writes the given list of stocks to a file at the specified file path.
   *
   * @param stocks the list of stocks to be written to the file
   * @param filePath the path of the file where the stock data should be written
   * @throws IOException if an I/O error occurs while writing to the file
   */
  public void writeToFile(List<Stock> stocks, String filePath) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write("# symbol, name, price");
      writer.newLine();
      for (Stock stock : stocks) {
        writer.write(stock.getSymbol() + "," + stock.getCompany() + ','
            + stock.getSalesPrice());
        writer.newLine();
      }
    }
  }
}


