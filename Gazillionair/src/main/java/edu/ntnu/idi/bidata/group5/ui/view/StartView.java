package edu.ntnu.idi.bidata.group5.ui.view;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.ui.controller.StartController;
import java.io.File;
import java.math.BigDecimal;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * StartView displays the initial game setup screen for Gazillionair.
 * Players enter their name, starting capital, and select stock data (upload CSV file
 * or use sample data). Upon form submission, creates a GameSession and navigates to
 * DashboardView.
 */
public class StartView {

  private final Stage stage;
  private final StartController controller;
  private final BorderPane root;
  private final TextField playerNameInput;
  private final Spinner<Integer> capitalSpinner;
  private File selectedStockFile;
  private Label fileStatusLabel;

  private static final int WINDOW_WIDTH = 900;
  private static final int WINDOW_HEIGHT = 600;
  private static final int DEFAULT_CAPITAL = 100000;
  private static final int MIN_CAPITAL = 1000;
  private static final int CAPITAL_STEP = 1000;

  /**
   * Constructs a StartView with the given stage.
   *
   * @param stage the JavaFX Stage for window operations
   */
  public StartView(Stage stage) {
    this.stage = stage;
    this.controller = new StartController();
    this.root = new BorderPane();
    this.playerNameInput = new TextField();
    this.capitalSpinner = new Spinner<>(
        MIN_CAPITAL, Integer.MAX_VALUE, DEFAULT_CAPITAL, CAPITAL_STEP);
    this.selectedStockFile = null;

    initializeUi();
  }

  private void initializeUi() {
    StackPane centerPane = createCenterPane();
    root.setCenter(centerPane);
    root.setStyle(
        "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #0f172a, "
            + "#1e293b, #0f172a);");
  }

  /**
   * Creates the center StackPane containing the form card.
   *
   * @return StackPane with centered form card
   */
  private StackPane createCenterPane() {
    StackPane container = new StackPane();
    VBox formCard = createFormCard();
    container.getChildren().add(formCard);
    return container;
  }

  /**
   * Creates the main form card with all input sections.
   *
   * @return VBox containing all form sections
   */
  private VBox createFormCard() {
    VBox card = new VBox(20);
    card.setPrefWidth(480);
    card.setMaxWidth(480);
    card.setPadding(new Insets(32));
    card.setStyle(
        "-fx-background-color: rgba(30, 41, 59, 0.5); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-background-radius: 16; "
            + "-fx-border-radius: 16; "
            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 20, 0.5, 0, 10);");

    final VBox headerSection = createHeaderSection();
    final VBox playerNameSection = createPlayerNameSection();
    final VBox capitalSection = createCapitalSection();
    final VBox fileUploadSection = createFileUploadSection();
    final VBox sampleDataSection = createSampleDataSection();
    final VBox startGameSection = createStartGameSection();

    card.getChildren().addAll(
        headerSection,
        playerNameSection,
        capitalSection,
        fileUploadSection,
        sampleDataSection,
        startGameSection
    );

    card.setAlignment(Pos.TOP_CENTER);
    return card;
  }

  /**
   * Creates the header section with title, and subtitle.
   *
   * @return VBox containing header elements
   */
  private VBox createHeaderSection() {
    final VBox section = new VBox(16);
    section.setAlignment(Pos.CENTER);

    Label title = new Label("Gazillionair");
    title.setFont(Font.font("System", FontWeight.BOLD, 48));
    title.setTextFill(Color.web("#ffffff"));

    Label subtitle = new Label("Stock Market Simulation Game");
    subtitle.setFont(Font.font("System", 20));
    subtitle.setTextFill(Color.web("#cbd5e1"));

    section.getChildren().addAll(title, subtitle);
    return section;
  }

  /**
   * Creates the player name input section.
   *
   * @return VBox containing label and text input field
   */
  private VBox createPlayerNameSection() {
    final VBox section = new VBox(8);
    Label label = new Label("Player Name");
    label.setFont(Font.font("System", FontWeight.MEDIUM, 14));
    label.setTextFill(Color.web("#cbd5e1"));

    playerNameInput.setPrefHeight(40);
    playerNameInput.setPromptText("Enter your name");
    playerNameInput.setStyle(
        "-fx-font-size: 14; "
            + "-fx-padding: 12px 16px; "
            + "-fx-background-color: rgba(15, 23, 42, 0.5); "
            + "-fx-border-color: #475569; "
            + "-fx-border-width: 1; "
            + "-fx-text-fill: white; "
            + "-fx-prompt-text-fill: #64748b; "
            + "-fx-background-radius: 8; "
            + "-fx-border-radius: 8; "
            + "-fx-focus-color: #22c55e; "
            + "-fx-faint-focus-color: rgba(34, 197, 94, 0.3);");

    section.getChildren().addAll(label, playerNameInput);
    return section;
  }

  /**
   * Creates the starting capital input section.
   *
   * @return VBox containing label and spinner control
   */
  private VBox createCapitalSection() {
    final VBox section = new VBox(8);
    Label label = new Label("Starting Capital");
    label.setFont(Font.font("System", FontWeight.MEDIUM, 14));
    label.setTextFill(Color.web("#cbd5e1"));

    capitalSpinner.setPrefHeight(40);
    capitalSpinner.setStyle(
        "-fx-font-size: 14; "
            + "-fx-padding: 12px 16px; "
            + "-fx-background-color: rgba(15, 23, 42, 0.5); "
            + "-fx-border-color: #475569; "
            + "-fx-border-width: 1; "
            + "-fx-text-fill: white; "
            + "-fx-background-radius: 8; "
            + "-fx-border-radius: 8;");

    section.getChildren().addAll(label, capitalSpinner);
    return section;
  }

  /**
   * Creates the file upload section with clickable upload zone.
   *
   * @return VBox containing label and upload area
   */
  private VBox createFileUploadSection() {
    final VBox section = new VBox(8);
    Label label = new Label("Stock Data");
    label.setFont(Font.font("System", FontWeight.MEDIUM, 14));
    label.setTextFill(Color.web("#cbd5e1"));

    HBox uploadBox = new HBox(12);
    uploadBox.setPadding(new Insets(16));
    uploadBox.setStyle(
        "-fx-border-color: #475569; "
            + "-fx-border-width: 2; "
            + "-fx-border-style: dashed; "
            + "-fx-background-radius: 8; "
            + "-fx-border-radius: 8; "
            + "-fx-alignment: center;");
    uploadBox.setCursor(javafx.scene.Cursor.HAND);

    fileStatusLabel = new Label("Click to upload CSV file");
    fileStatusLabel.setFont(Font.font("System", 14));
    fileStatusLabel.setTextFill(Color.web("#cbd5e1"));

    uploadBox.getChildren().add(fileStatusLabel);
    uploadBox.setOnMouseClicked(ignoredEvent -> selectStockFile());

    section.getChildren().addAll(label, uploadBox);
    return section;
  }

  /**
   * Creates the sample data button section.
   *
   * @return VBox containing sample data button
   */
  private VBox createSampleDataSection() {
    final VBox section = new VBox();
    Button sampleDataBtn = new Button("Use Sample Data (506 stocks)");
    sampleDataBtn.setPrefHeight(32);
    sampleDataBtn.setPrefWidth(Double.MAX_VALUE);
    sampleDataBtn.setFont(Font.font("System", FontWeight.MEDIUM, 14));
    sampleDataBtn.setStyle(
        "-fx-background-color: #475569; "
            + "-fx-text-fill: white; "
            + "-fx-background-radius: 8; "
            + "-fx-padding: 8px 16px; "
            + "-fx-cursor: hand;");

    sampleDataBtn.setOnAction(ignoredEvent -> {
      selectedStockFile = null;
      fileStatusLabel.setText("Sample data selected (506 stocks)");
    });

    section.getChildren().add(sampleDataBtn);
    return section;
  }

  /**
   * Creates the start game button section.
   *
   * @return VBox containing start game button with hover effects
   */
  private VBox createStartGameSection() {
    final VBox section = new VBox();
    Button startBtn = new Button("Start Game");
    startBtn.setPrefHeight(48);
    startBtn.setPrefWidth(Double.MAX_VALUE);
    startBtn.setFont(Font.font("System", FontWeight.BOLD, 16));
    startBtn.setStyle(
        "-fx-background-color: linear-gradient(to right, #22c55e, #10b981); "
            + "-fx-text-fill: white; "
            + "-fx-background-radius: 8; "
            + "-fx-padding: 16px 24px; "
            + "-fx-cursor: hand; "
            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 8, 0.5, 0, 4);");

    startBtn.setOnMouseEntered(ignoredEvent -> applyStartButtonHoverStyle(startBtn, true));
    startBtn.setOnMouseExited(ignoredEvent -> applyStartButtonHoverStyle(startBtn, false));
    startBtn.setOnAction(ignoredEvent -> onStartGame());

    section.getChildren().add(startBtn);
    return section;
  }

  /**
   * Applies hover or normal style to start button.
   *
   * @param button the button to style
   * @param isHovered true for hover style, false for normal style
   */
  private void applyStartButtonHoverStyle(Button button, boolean isHovered) {
    if (isHovered) {
      button.setStyle(
          "-fx-background-color: linear-gradient(to right, #16a34a, #059669); "
              + "-fx-text-fill: white; "
              + "-fx-background-radius: 8; "
              + "-fx-padding: 16px 24px; "
              + "-fx-cursor: hand; "
              + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 12, 0.5, 0, 6);");
    } else {
      button.setStyle(
          "-fx-background-color: linear-gradient(to right, #22c55e, #10b981); "
              + "-fx-text-fill: white; "
              + "-fx-background-radius: 8; "
              + "-fx-padding: 16px 24px; "
              + "-fx-cursor: hand; "
              + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 8, 0.5, 0, 4);");
    }
  }

  /**
   * Opens a FileChooser dialog for selecting a stock CSV file.
   * Updates fileStatusLabel on successful selection.
   */
  private void selectStockFile() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Stock CSV File");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("CSV Files", "*.csv")
    );
    File file = fileChooser.showOpenDialog(stage);
    if (file != null) {
      selectedStockFile = file;
      fileStatusLabel.setText(file.getName() + " loaded ✓");
    }
  }

  /**
   * Handles start game button action. Validates player name, retrieves capital from
   * spinner, and initiates game via StartController. On success, navigates to
   * DashboardView. On failure, shows error alert.
   */
  private void onStartGame() {
    String playerName = playerNameInput.getText().trim();
    if (playerName.isEmpty()) {
      showError("Player name cannot be empty.");
      return;
    }

    BigDecimal capital = BigDecimal.valueOf(capitalSpinner.getValue());

    try {
      GameSession session;
      if (selectedStockFile != null) {
        session = controller.startNewGame(playerName, capital, selectedStockFile.toPath());
      } else {
        session = controller.startWithSampleData(playerName, capital);
      }
      DashboardView dashboardView = new DashboardView(session, stage);
      Scene scene = new Scene(dashboardView.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
      String cssResource = Objects.requireNonNull(
          getClass().getResource("/styles/app.css"),
          "CSS file not found: /styles/app.css"
      ).toExternalForm();
      scene.getStylesheets().add(cssResource);
      stage.setScene(scene);
    } catch (Exception e) {
      showError("Failed to start game: " + e.getMessage());
    }
  }

  /**
   * Displays an error alert dialog to the user.
   *
   * @param message the error message to display
   */
  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Returns the root BorderPane container.
   *
   * @return the root BorderPane of this view
   */
  public BorderPane getRoot() {
    return root;
  }
}

