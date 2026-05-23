package edu.ntnu.idi.bidata.group5.main;

import edu.ntnu.idi.bidata.group5.ui.view.StartView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Main application class for Gazillionair stock trading simulation.
 * Launches the JavaFX application and displays the StartView.
 */
public class GazillionairApp extends Application {

  private static final String APP_TITLE = "Gazillionair";
  private static final int WINDOW_WIDTH = 900;
  private static final int WINDOW_HEIGHT = 600;

  @Override
  public void start(Stage primaryStage) {
    try {
      StartView startView = new StartView(primaryStage);
      Scene scene = new Scene(startView.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

      String cssResource = resolveCssResource();
      scene.getStylesheets().add(cssResource);

      primaryStage.setTitle(APP_TITLE);
      primaryStage.setScene(scene);
      primaryStage.setWidth(WINDOW_WIDTH);
      primaryStage.setHeight(WINDOW_HEIGHT);
      primaryStage.centerOnScreen();
      primaryStage.show();
    } catch (Exception e) {
      showStartupErrorDialog(primaryStage, e);
    }
  }

  /**
   * Resolves the application stylesheet path.
   *
   * @return the external form of the stylesheet resource
   */
  private String resolveCssResource() {
    java.net.URL cssUrl = getClass().getResource("/styles/app.css");
    if (cssUrl == null) {
      throw new IllegalStateException("Stylesheet resource not found: /styles/app.css");
    }
    return cssUrl.toExternalForm();
  }

  /**
   * Shows a user-facing startup failure dialog and closes the application afterward.
   *
   * @param primaryStage application stage
   * @param exception startup failure
   */
  private void showStartupErrorDialog(Stage primaryStage, Exception exception) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.initOwner(primaryStage);
    alert.setTitle("Startup Failed");
    alert.setHeaderText("Gazillionair could not start");
    alert.setContentText(exception.getMessage());
    alert.showAndWait();
    Platform.exit();
  }

  /**
   * Launches the JavaFX application.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
