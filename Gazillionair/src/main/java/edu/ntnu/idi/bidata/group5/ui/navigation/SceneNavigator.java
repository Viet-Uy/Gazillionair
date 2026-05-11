package edu.ntnu.idi.bidata.group5.ui.navigation;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.ui.view.DashboardView;
import java.util.Objects;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Handles JavaFX scene navigation between top-level views.
 */
public final class SceneNavigator {

  private static final int WINDOW_WIDTH = 900;
  private static final int WINDOW_HEIGHT = 600;

  private SceneNavigator() {
  }

  /**
   * Opens the dashboard scene for the given session.
   *
   * @param stage active stage
   * @param session game session to display
   */
  public static void openDashboard(Stage stage, GameSession session) {
    if (stage == null) {
      throw new IllegalArgumentException("Stage cannot be null");
    }
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    DashboardView dashboardView = new DashboardView(session, stage);
    Scene scene = new Scene(dashboardView.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
    String cssResource = Objects.requireNonNull(
        SceneNavigator.class.getResource("/styles/app.css"),
        "CSS file not found: /styles/app.css"
    ).toExternalForm();
    scene.getStylesheets().add(cssResource);
    stage.setScene(scene);
  }
}
