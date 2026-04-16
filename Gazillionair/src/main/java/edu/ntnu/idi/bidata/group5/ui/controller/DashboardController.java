package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.PlayerStatus;
import java.math.BigDecimal;

/**
 * Controller for the dashboard view,
 * responsible for handling user interactions and updating the view.
 */

public class DashboardController {

  private final GameSession session;

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
  }

  /**
   * Simulating the passage of time by advancing to the next week in the game session.
   *
   */
  public void nextWeek() {
    session.nextWeek();
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
    return session.getPlayerStatus();
  }
}
