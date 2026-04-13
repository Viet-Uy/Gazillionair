package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.PlayerStatus;
import java.math.BigDecimal;

public class DashboardController {

  private final GameSession session;

  public DashboardController(GameSession session) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    this.session = session;
  }

  public void nextWeek() {
    session.nextWeek();
  }

  public BigDecimal getNetWorth() {
    return session.getNetWorth();
  }

  public PlayerStatus getStatus() {
    return session.getPlayerStatus();
  }
}
