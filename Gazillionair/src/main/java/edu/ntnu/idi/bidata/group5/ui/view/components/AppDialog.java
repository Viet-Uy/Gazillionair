package edu.ntnu.idi.bidata.group5.ui.view.components;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Common application dialogs used across views.
 */
public final class AppDialog {

  private AppDialog() {
  }

  /**
   * Shows an information dialog.
   *
   * @param stage owner stage
   * @param title dialog title
   * @param message dialog message
   */
  public static void showInfo(Stage stage, String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    if (stage != null) {
      alert.initOwner(stage);
    }
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Shows an error dialog.
   *
   * @param stage owner stage
   * @param title dialog title
   * @param message dialog message
   */
  public static void showError(Stage stage, String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    if (stage != null) {
      alert.initOwner(stage);
    }
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
