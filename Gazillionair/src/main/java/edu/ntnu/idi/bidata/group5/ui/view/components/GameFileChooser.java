package edu.ntnu.idi.bidata.group5.ui.view.components;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * File chooser helpers for game input/output files.
 */
public final class GameFileChooser {

  private GameFileChooser() {
  }

  /**
   * Prompts for a stock CSV file.
   *
   * @param stage owner stage
   * @return selected file, or null when cancelled
   */
  public static File chooseStockCsv(Stage stage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Stock CSV File");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("CSV Files", "*.csv")
    );
    return fileChooser.showOpenDialog(stage);
  }

  /**
   * Prompts for a JSON save file to load.
   *
   * @param stage owner stage
   * @return selected file, or null when cancelled
   */
  public static File chooseLoadSaveJson(Stage stage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Load Saved Game");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("JSON Files", "*.json")
    );
    return fileChooser.showOpenDialog(stage);
  }

  /**
   * Prompts for a JSON save file destination.
   *
   * @param stage owner stage
   * @return selected file, or null when cancelled
   */
  public static File chooseSaveGameJson(Stage stage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Game");
    fileChooser.setInitialFileName("gazillionair-save.json");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("JSON Files", "*.json")
    );
    return fileChooser.showSaveDialog(stage);
  }
}
