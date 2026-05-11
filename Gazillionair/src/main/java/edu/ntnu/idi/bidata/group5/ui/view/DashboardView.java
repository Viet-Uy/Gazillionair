package edu.ntnu.idi.bidata.group5.ui.view;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.PlayerStatus;
import edu.ntnu.idi.bidata.group5.model.observer.ModelObserver;
import edu.ntnu.idi.bidata.group5.ui.controller.DashboardController;
import edu.ntnu.idi.bidata.group5.ui.controller.MarketController;
import edu.ntnu.idi.bidata.group5.ui.controller.NewsController;
import edu.ntnu.idi.bidata.group5.ui.controller.PortfolioController;
import edu.ntnu.idi.bidata.group5.ui.controller.StatsController;
import edu.ntnu.idi.bidata.group5.ui.controller.TransactionsController;
import edu.ntnu.idi.bidata.group5.ui.view.components.AppDialog;
import edu.ntnu.idi.bidata.group5.ui.view.components.GameFileChooser;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * DashboardView displays the main game interface after player starts a game.
 * Shows player stats, market data, portfolio, transactions, and market analysis
 * across tabbed navigation. Includes sticky header with player info and controls.
 */
public class DashboardView implements ModelObserver {

  private final BorderPane root;
  private final GameSession session;
  private final Stage stage;
  private final DashboardController dashboardController;

  private Label netWorthLabel;
  private Label cashLabel;
  private Label holdingsLabel;
  private Label weekLabel;
  private Label statusBadge;
  private Label weeklyChangeLabel;
  private Label overallPerformanceLabel;

  private Tab marketTab;
  private Tab portfolioTab;
  private Tab transactionsTab;
  private Tab statsTab;
  private Tab newsTab;

  private MarketController marketController;
  private NewsController newsController;
  private PortfolioController portfolioController;
  private TransactionsController transactionsController;
  private StatsController statsController;
  private PortfolioView portfolioView;
  private TransactionsView transactionsView;
  private StatsView statsView;

  /**
   * Constructs a DashboardView with empty placeholder layout.
   */
  public DashboardView() {
    this(null, null);
  }

  /**
   * Constructs a DashboardView with a GameSession and Stage.
   *
   * @param session the GameSession containing player and market data
   * @param stage   the JavaFX Stage for window operations
   */
  public DashboardView(GameSession session, Stage stage) {
    this.session = session;
    this.stage = stage;
    this.dashboardController = session == null ? null : new DashboardController(session);
    this.root = new BorderPane();
    initializeUi();
    if (session != null) {
      session.addObserver(this);
    }
  }

  /**
   * Initializes the view UI with header, stats, and tabbed content.
   */
  private void initializeUi() {
    root.setStyle(
        "-fx-background-color: linear-gradient(to bottom right, #0f172a, "
            + "#1e293b, #0f172a);");

    if (session != null) {
      VBox header = createHeader();
      root.setTop(header);

      VBox statsSection = createStatsCards();
      VBox centerContent = new VBox(16);
      centerContent.setPadding(new Insets(16));

      TabPane tabPane = createTabPane();
      centerContent.getChildren().addAll(statsSection, tabPane);
      VBox.setVgrow(tabPane, Priority.ALWAYS);
      root.setCenter(centerContent);
    } else {
      Label placeholder = new Label("Dashboard Loading...");
      placeholder.setTextFill(Color.web("#cbd5e1"));
      placeholder.setFont(Font.font("System", 18));
      root.setCenter(placeholder);
    }
  }

  /**
   * Creates the sticky header with player info and action buttons.
   *
   * @return VBox containing the header
   */
  private VBox createHeader() {
    VBox header = new VBox();
    header.setPadding(new Insets(16, 24, 16, 24));
    header.setStyle(
        "-fx-background-color: rgba(30, 41, 59, 0.8); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 0 0 1 0;");

    HBox headerContent = new HBox(20);
    headerContent.setAlignment(Pos.CENTER_LEFT);

    Circle logo = new Circle(20);
    LinearGradient gradient =
        new LinearGradient(
            0.0,
            0.0,
            1.0,
            1.0,
            true,
            null,
            new Stop(0.0, Color.web("#22c55e")),
            new Stop(1.0, Color.web("#10b981")));
    logo.setFill(gradient);

    Label playerName = new Label(session.getPlayer().getName());
    playerName.setFont(Font.font("System", FontWeight.BOLD, 20));
    playerName.setTextFill(Color.web("#ffffff"));

    statusBadge = new Label(session.getPlayerStatus().toString());
    statusBadge.setFont(Font.font("System", FontWeight.MEDIUM, 12));
    statusBadge.setTextFill(Color.web("#ffffff"));
    statusBadge.setPadding(new Insets(4, 12, 4, 12));
    applyStatusBadgeStyle(session.getPlayerStatus());

    HBox playerInfo = new HBox(12);
    playerInfo.setAlignment(Pos.CENTER_LEFT);
    playerInfo.getChildren().addAll(logo, playerName, statusBadge);

    HBox actionButtons = new HBox(12);
    actionButtons.setAlignment(Pos.CENTER_RIGHT);

    Button nextWeekBtn = new Button("Next Week");
    nextWeekBtn.setStyle(
        "-fx-background-color: #22c55e; "
            + "-fx-text-fill: white; "
            + "-fx-padding: 8px 16px; "
            + "-fx-background-radius: 8; "
            + "-fx-font-size: 14; "
            + "-fx-cursor: hand;");
    nextWeekBtn.setOnAction(e -> onNextWeek());

    Button saveAndExitBtn = new Button("Save & Exit");
    saveAndExitBtn.setStyle(
        "-fx-background-color: #3b82f6; "
            + "-fx-text-fill: white; "
            + "-fx-padding: 8px 16px; "
            + "-fx-background-radius: 8; "
            + "-fx-font-size: 14; "
            + "-fx-cursor: hand;");
    saveAndExitBtn.setOnAction(e -> onSaveAndExit());

    Button sellAllExitBtn = new Button("Sell All & Exit");
    sellAllExitBtn.setStyle(
        "-fx-background-color: #ef4444; "
            + "-fx-text-fill: white; "
            + "-fx-padding: 8px 16px; "
            + "-fx-background-radius: 8; "
            + "-fx-font-size: 14; "
            + "-fx-cursor: hand;");
    sellAllExitBtn.setOnAction(e -> onSellAllAndExit());

    actionButtons.getChildren().addAll(nextWeekBtn, saveAndExitBtn, sellAllExitBtn);
    HBox.setHgrow(playerInfo, Priority.ALWAYS);

    headerContent.getChildren().addAll(playerInfo, actionButtons);
    header.getChildren().add(headerContent);
    return header;
  }

  /**
   * Creates the stats cards section displaying key player metrics.
   *
   * @return VBox containing stats cards grid
   */
  private VBox createStatsCards() {
    VBox section = new VBox(8);
    section.setPadding(new Insets(0));

    GridPane grid = new GridPane();
    grid.setHgap(16);
    grid.setVgap(0);
    grid.setPrefHeight(100);

    final VBox netWorthCard = createStatCard("Net Worth", formatMoney(session.getNetWorth()));
    final VBox cashCard = createStatCard("Cash", formatMoney(session.getPlayer().getMoney()));
    final VBox holdingsCard =
        createStatCard("Holdings", String.valueOf(session.getHoldings().size()));
    final VBox weekCard = createStatCard("Week", String.valueOf(session.getCurrentWeek()));

    netWorthLabel = (Label) netWorthCard.getChildren().get(1);
    cashLabel = (Label) cashCard.getChildren().get(1);
    holdingsLabel = (Label) holdingsCard.getChildren().get(1);
    weekLabel = (Label) weekCard.getChildren().get(1);

    GridPane.setHgrow(netWorthCard, Priority.ALWAYS);
    GridPane.setHgrow(cashCard, Priority.ALWAYS);
    GridPane.setHgrow(holdingsCard, Priority.ALWAYS);
    GridPane.setHgrow(weekCard, Priority.ALWAYS);

    grid.add(netWorthCard, 0, 0);
    grid.add(cashCard, 1, 0);
    grid.add(holdingsCard, 2, 0);
    grid.add(weekCard, 3, 0);

    weeklyChangeLabel = new Label("Weekly Change: $0.00");
    weeklyChangeLabel.setStyle(
        "-fx-background-color: rgba(15, 23, 42, 0.5); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-background-radius: 8; "
            + "-fx-border-radius: 8; "
            + "-fx-padding: 10 14; "
            + "-fx-text-fill: #94a3b8; "
            + "-fx-font-size: 14; "
            + "-fx-font-weight: 600;");

    overallPerformanceLabel = new Label();
    overallPerformanceLabel.setStyle(
        "-fx-background-color: rgba(15, 23, 42, 0.5); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-background-radius: 8; "
            + "-fx-border-radius: 8; "
            + "-fx-padding: 10 14; "
            + "-fx-text-fill: #cbd5e1; "
            + "-fx-font-size: 14; "
            + "-fx-font-weight: 600;");
    setOverallPerformance();

    section.getChildren().addAll(grid, weeklyChangeLabel, overallPerformanceLabel);
    return section;
  }

  /**
   * Creates a single stat card with label and value.
   *
   * @param label the stat label
   * @param value the stat value
   * @return VBox containing the stat card
   */
  private VBox createStatCard(String label, String value) {
    VBox card = new VBox(8);
    card.setPadding(new Insets(16));
    card.setStyle(
        "-fx-background-color: rgba(15, 23, 42, 0.5); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-background-radius: 8; "
            + "-fx-border-radius: 8;");

    Label labelText = new Label(label);
    labelText.setFont(Font.font("System", FontWeight.MEDIUM, 12));
    labelText.setTextFill(Color.web("#94a3b8"));

    Label valueText = new Label(value);
    valueText.setFont(Font.font("System", FontWeight.BOLD, 24));
    valueText.setTextFill(Color.web("#ffffff"));

    card.getChildren().addAll(labelText, valueText);
    return card;
  }

  /**
   * Creates the tabbed navigation pane for Market, News, Portfolio, Transactions, Stats.
   *
   * @return TabPane with all tabs
   */
  private TabPane createTabPane() {
    TabPane tabPane = new TabPane();
    tabPane.setStyle(
        "-fx-padding: 0; "
            + "-fx-background-color: rgba(30, 41, 59, 0.5); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1 0 0 0;");
    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    tabPane.setMinHeight(200);
    tabPane.setPrefWidth(Double.MAX_VALUE);

    if (session != null) {
      MarketView marketView = new MarketView();
      marketController = new MarketController(session, marketView);
      portfolioController = new PortfolioController(session);
      transactionsController = new TransactionsController(session);
      statsController = new StatsController(session);

      portfolioView = new PortfolioView(portfolioController);
      transactionsView = new TransactionsView(transactionsController);
      statsView = new StatsView(statsController);

      NewsView newsView = new NewsView();
      newsController = new NewsController(newsView, session);

      marketTab = createTab("Market", marketView.getRoot());
      newsTab = createTab("News", newsView.getRoot());
      portfolioTab = createTab("Portfolio", portfolioView.getRoot());
      transactionsTab = createTab("Transactions", transactionsView.getRoot());
      statsTab = createTab("Stats", statsView.getRoot());
    } else {
      marketTab = createTab("Market", createPlaceholder("Market View"));
      newsTab = createTab("News", createPlaceholder("News View"));
      portfolioTab = createTab("Portfolio", createPlaceholder("Portfolio View"));
      transactionsTab = createTab("Transactions", createPlaceholder("Transactions View"));
      statsTab = createTab("Stats", createPlaceholder("Stats View"));
    }

    tabPane.getTabs().addAll(marketTab, newsTab, portfolioTab, transactionsTab, statsTab);
    return tabPane;
  }

  /**
   * Creates a single tab with the given title and content.
   *
   * @param title the tab title
   * @param content the tab content node
   * @return Tab with the given configuration
   */
  private Tab createTab(String title, javafx.scene.Node content) {
    Tab tab = new Tab();
    tab.setText(title);
    tab.setClosable(false);
    tab.setContent(content);
    return tab;
  }

  /**
   * Creates a placeholder content node.
   *
   * @param text the placeholder text
   * @return VBox with placeholder content
   */
  private VBox createPlaceholder(String text) {
    VBox placeholder = new VBox();
    placeholder.setAlignment(Pos.CENTER);
    placeholder.setPadding(new Insets(32));

    Label label = new Label(text);
    label.setFont(Font.font("System", FontWeight.MEDIUM, 18));
    label.setTextFill(Color.web("#cbd5e1"));

    placeholder.getChildren().add(label);
    return placeholder;
  }

  /**
   * Formats a BigDecimal as currency string.
   *
   * @param amount the amount to format
   * @return formatted currency string
   */
  private String formatMoney(BigDecimal amount) {
    return String.format("$%.2f", amount);
  }


  /**
   * Handles Next Week button action.
   */
  private void onNextWeek() {
    if (session != null) {
      BigDecimal before = session.getNetWorth();
      dashboardController.nextWeek();
      BigDecimal after = session.getNetWorth();
      setWeeklyChange(after.subtract(before));
    }
  }

  private void onSellAllAndExit() {
    if (session != null) {
      dashboardController.sellAllHoldings();
    }
    if (stage != null) {
      stage.close();
    }
  }

  private void onSaveAndExit() {
    if (session == null) {
      return;
    }
    File file = GameFileChooser.chooseSaveGameJson(stage);
    if (file == null) {
      return;
    }
    try {
      dashboardController.saveGame(file.toPath());
      AppDialog.showInfo(stage, "Game Saved", "Game state saved successfully.");
      if (stage != null) {
        stage.close();
      }
    } catch (IllegalStateException e) {
      AppDialog.showError(stage, "Save Failed", "Failed to save game: " + e.getMessage());
    }
  }

  /**
   * Called when the model changes.
   * Updates all stat cards and UI elements.
   */
  @Override
  public void onModelChanged() {
    if (session != null) {
      netWorthLabel.setText(formatMoney(session.getNetWorth()));
      cashLabel.setText(formatMoney(session.getPlayer().getMoney()));
      holdingsLabel.setText(String.valueOf(session.getHoldings().size()));
      weekLabel.setText(String.valueOf(session.getCurrentWeek()));
      statusBadge.setText(session.getPlayerStatus().toString());
      applyStatusBadgeStyle(session.getPlayerStatus());
      setOverallPerformance();

      if (marketController != null) {
        marketController.refreshStockTable();
      }

      if (newsController != null) {
        newsController.refreshForCurrentWeek();
      }

      if (portfolioView != null) {
        portfolioView.refresh();
      }
      if (transactionsView != null) {
        transactionsView.refresh();
      }
      if (statsView != null) {
        statsView.refresh();
      }
    }
  }

  private void setWeeklyChange(BigDecimal weeklyChange) {
    String sign = weeklyChange.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
    String text = "Weekly Change: " + sign + formatMoney(weeklyChange);
    String color = "#cbd5e1";
    if (weeklyChange.compareTo(BigDecimal.ZERO) > 0) {
      color = "#22c55e";
    } else if (weeklyChange.compareTo(BigDecimal.ZERO) < 0) {
      color = "#ef4444";
    }
    weeklyChangeLabel.setText(text);
    weeklyChangeLabel.setStyle(
        "-fx-background-color: rgba(15, 23, 42, 0.5); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-background-radius: 8; "
            + "-fx-border-radius: 8; "
            + "-fx-padding: 10 14; "
            + "-fx-text-fill: "
            + color
            + "; "
            + "-fx-font-size: 14; "
            + "-fx-font-weight: 600;");
  }

  private void setOverallPerformance() {
    BigDecimal startingMoney = session.getPlayer().getStartingMoney();
    BigDecimal gain = session.getNetWorth().subtract(startingMoney);
    String sign = gain.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
    BigDecimal percent = BigDecimal.ZERO;
    if (startingMoney.compareTo(BigDecimal.ZERO) > 0) {
      percent =
          gain.divide(startingMoney, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }
    String percentSign = percent.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
    String color = "#cbd5e1";
    if (gain.compareTo(BigDecimal.ZERO) > 0) {
      color = "#22c55e";
    } else if (gain.compareTo(BigDecimal.ZERO) < 0) {
      color = "#ef4444";
    }
    overallPerformanceLabel.setText(
        "Overall: "
            + sign
            + formatMoney(gain)
            + " ("
            + percentSign
            + String.format("%.2f", percent)
            + "%)");
    overallPerformanceLabel.setStyle(
        "-fx-background-color: rgba(15, 23, 42, 0.5); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-background-radius: 8; "
            + "-fx-border-radius: 8; "
            + "-fx-padding: 10 14; "
            + "-fx-text-fill: "
            + color
            + "; "
            + "-fx-font-size: 14; "
            + "-fx-font-weight: 600;");
  }

  private void applyStatusBadgeStyle(PlayerStatus status) {
    String style;
    if (status == PlayerStatus.SPECULATOR) {
      style =
          "-fx-background-color: linear-gradient(to right, #f59e0b, #f97316); "
              + "-fx-background-radius: 20; "
              + "-fx-border-color: #f59e0b; "
              + "-fx-border-width: 1; "
              + "-fx-border-radius: 20;";
    } else if (status == PlayerStatus.INVESTOR) {
      style =
          "-fx-background-color: linear-gradient(to right, #22c55e, #10b981); "
              + "-fx-background-radius: 20; "
              + "-fx-border-color: #22c55e; "
              + "-fx-border-width: 1; "
              + "-fx-border-radius: 20;";
    } else {
      style =
          "-fx-background-color: linear-gradient(to right, #64748b, #475569); "
              + "-fx-background-radius: 20; "
              + "-fx-border-color: #64748b; "
              + "-fx-border-width: 1; "
              + "-fx-border-radius: 20;";
    }
    statusBadge.setStyle(style);
  }

  /**
   * Returns the root BorderPane container.
   *
   * @return the root BorderPane of this view
   */
  public BorderPane getRoot() {
    return root;
  }

  /**
   * Returns the GameSession associated with this view.
   *
   * @return the GameSession, or null if not initialized
   */
  public GameSession getSession() {
    return session;
  }
}
