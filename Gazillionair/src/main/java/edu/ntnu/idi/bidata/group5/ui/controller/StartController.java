package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.file.StockFileHandler;
import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

public class StartController {

  private final StockFileHandler stockFileHandler;

  public StartController() {
    this.stockFileHandler = new StockFileHandler();
  }

  public GameSession startNewGame(String playerName, BigDecimal startingCapital, Path stockFilePath) {
    if (stockFilePath == null) {
      throw new IllegalArgumentException("Stock file path cannot be null");
    }

    try {
      List<Stock> stocks = stockFileHandler.readFromFile(stockFilePath.toString());
      return new GameSession(playerName, startingCapital, stocks);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load stocks from file", e);
    }
  }

  public GameSession startWithSampleData(String playerName, BigDecimal startingCapital) {
    try {
      List<Stock> stocks = stockFileHandler.readFromFile("src/main/resources/sp500.csv");
      return new GameSession(playerName, startingCapital, stocks);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load sample stock data", e);
    }
  }
}
