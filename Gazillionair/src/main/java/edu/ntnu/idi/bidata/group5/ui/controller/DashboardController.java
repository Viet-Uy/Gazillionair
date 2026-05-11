package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.PlayerStatus;
import edu.ntnu.idi.bidata.group5.model.Sale;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

/**
 * Controller for the dashboard view,
 * responsible for handling user interactions and updating the view.
 */

public class DashboardController {

  private final GameSession session;
  private final StartController startController;

  /**
   * Constructor for the DashboardController, initializes the GameSession.
   *
   * @param session the GameSession to be used by the controller.
   * @throws IllegalArgumentException if the session is null.
   */
  public DashboardController(GameSession session) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    this.session = session;
    this.startController = new StartController();
  }

  /**
   * Simulating the passage of time by advancing to the next week in the game session.
   *
   */
  public void nextWeek() {
    session.nextWeek();
  }

  /**
   * Sells all holdings for the current session.
   *
   * @return list of sale transactions
   */
  public List<Sale> sellAllHoldings() {
    return session.sellAllHoldings();
  }

  /**
   * Saves the current session to the given path.
   *
   * @param saveFilePath destination file path
   */
  public void saveGame(Path saveFilePath) {
    startController.saveGame(session, saveFilePath);
  }

  /**
   * Retrieves the current week number from the game session.
   *
   * @return return networth
   */
  public BigDecimal getNetWorth() {
    return session.getNetWorth();
  }

  /**
   * Retrieves the current player status from the game session.
   *
   * @return get status
   */
  public PlayerStatus getStatus() {
    return session.getPlayer().getStatus();
  }
}
