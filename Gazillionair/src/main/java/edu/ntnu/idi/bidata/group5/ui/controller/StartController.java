package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.file.GameStateFileHandler;
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
  private final GameStateFileHandler gameStateFileHandler;

  /**
   * Constructor for the StartController, initializes the StockFileHandler.
   */
  public StartController() {
    this.stockFileHandler = new StockFileHandler();
    this.gameStateFileHandler = new GameStateFileHandler();
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

  /**
   * Saves an active game session to a JSON file.
   *
   * @param session the session to save
   * @param saveFilePath path to JSON save file
   */
  public void saveGame(GameSession session, Path saveFilePath) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    if (saveFilePath == null) {
      throw new IllegalArgumentException("Save file path cannot be null");
    }
    try {
      gameStateFileHandler.writeToFile(session, saveFilePath.toString());
    } catch (IOException e) {
      throw new IllegalStateException("Failed to save game state", e);
    }
  }

  /**
   * Loads a game session from a JSON save file.
   *
   * @param saveFilePath path to JSON save file
   * @return restored game session
   */
  public GameSession loadGame(Path saveFilePath) {
    if (saveFilePath == null) {
      throw new IllegalArgumentException("Save file path cannot be null");
    }
    try {
      return gameStateFileHandler.readFromFile(saveFilePath.toString());
    } catch (IOException | IllegalArgumentException e) {
      throw new IllegalStateException("Failed to load game state", e);
    }
  }
}
