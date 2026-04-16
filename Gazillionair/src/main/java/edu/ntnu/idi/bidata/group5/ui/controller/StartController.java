package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.file.StockFileHandler;
import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Stock;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

/**
 * StartController class is responsible for handling the logic related
 * to starting a new game session.
 * It will be used to handle the logic related to starting a new game session,
 * such as loading stock data from a file, initializing the game session,
 * and other relevant logic related to starting a new game session.
 */
public class StartController {

  private final StockFileHandler stockFileHandler;

  /**
   * Constructor for the StartController, initializes the StockFileHandler.
   */
  public StartController() {
    this.stockFileHandler = new StockFileHandler();
  }

  /**
   * Starts a new game session by loading stock data from the specified file path.
   *
   * @param playerName the name of the player starting the game session.
   * @param startingCapital the initial capital for the player in the game session.
   * @param stockFilePath the file path to the stock data file
   * @return a new GameSession initialized with the loaded stock data and player information.
   * @throws IllegalArgumentException if the stock file path is null.
   * @throws IllegalStateException if there is an error loading the stock data from the file
   */
  public GameSession startNewGame(String playerName,
                                  BigDecimal startingCapital, Path stockFilePath) {
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

  /**
   * Starts a new game session with sample stock data loaded from a predefined file path.
   *
   * @param playerName the name of the player starting the game session.
   * @param startingCapital the initial capital for the player in the game session.
   * @return a new GameSession initialized with the sample stock data and player information.
   * @throws IllegalStateException if there is an error loading the sample stock data from the
   */
  public GameSession startWithSampleData(String playerName, BigDecimal startingCapital) {
    try {
      List<Stock> stocks = stockFileHandler.readFromFile("src/main/resources/sp500.csv");
      return new GameSession(playerName, startingCapital, stocks);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load sample stock data", e);
    }
  }
}
