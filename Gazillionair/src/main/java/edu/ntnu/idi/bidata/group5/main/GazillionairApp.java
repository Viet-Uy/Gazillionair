package edu.ntnu.idi.bidata.group5.main;

import edu.ntnu.idi.bidata.group5.ui.view.StartView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
      
      String cssResource = getClass().getResource("/styles/app.css").toExternalForm();
      scene.getStylesheets().add(cssResource);

      primaryStage.setTitle(APP_TITLE);
      primaryStage.setScene(scene);
      primaryStage.setWidth(WINDOW_WIDTH);
      primaryStage.setHeight(WINDOW_HEIGHT);
      primaryStage.centerOnScreen();
      primaryStage.show();
    } catch (Exception e) {
      System.err.println("Failed to start application: " + e.getMessage());
      e.printStackTrace();
    }
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
