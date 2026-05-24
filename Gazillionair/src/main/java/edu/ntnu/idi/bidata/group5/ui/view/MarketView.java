package edu.ntnu.idi.bidata.group5.ui.view;

import edu.ntnu.idi.bidata.group5.model.Stock;
import edu.ntnu.idi.bidata.group5.ui.view.components.AppDialog;
import edu.ntnu.idi.bidata.group5.ui.view.components.PriceChartComponent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * MarketView displays searchable stock listings and inline buy/sell actions.
 */
public class MarketView {

  private final VBox root;
  private final TextField searchInput;
  private final TableView<Stock> stockTable;
  private final ObservableList<Stock> tableData;
  private final Label selectedStockLabel;
  private final Label selectedPriceLabel;
  private final TextField quantityInput;
  private final TextField amountInput;
  private final Label amountHintLabel;
  private final Label tradeFeedbackLabel;
  private final Button buyButton;
  private final Button buyMaxButton;
  private final Button sellButton;
  private final Button watchChartButton;
  private Consumer<Stock> onRowSelected;
  private BiConsumer<Stock, BigDecimal> onBuyRequested;
  private BiConsumer<Stock, BigDecimal> onSellRequested;
  private Consumer<Stock> onBuyMaxRequested;
  private Stock selectedStock;
  private Stage chartStage;

  private static final int CHART_WINDOW_WIDTH = 640;
  private static final int CHART_WINDOW_HEIGHT = 420;

  /**
   * Constructs a MarketView with empty stock list.
   */
  public MarketView() {
    this.root = new VBox(12);
    this.root.setPadding(new Insets(16));
    this.searchInput = new TextField();
    this.tableData = FXCollections.observableArrayList();
    this.stockTable = createStockTable();
    this.selectedStockLabel = new Label("Select a stock");
    this.selectedPriceLabel = new Label("Price: -");
    this.quantityInput = new TextField();
    this.amountInput = new TextField();
    this.amountHintLabel = new Label("Enter quantity or amount.");
    this.tradeFeedbackLabel = new Label();
    this.buyButton = new Button("Buy");
    this.buyMaxButton = new Button("Buy Max");
    this.sellButton = new Button("Sell");
    this.watchChartButton = new Button("Watch Graph");
    initializeUi();
  }

  private void initializeUi() {
    root.setStyle("-fx-background-color: transparent;");
    HBox searchBox = createSearchBox();
    VBox rightPanel = createRightPanel();
    HBox content = new HBox(16, stockTable, rightPanel);
    HBox.setHgrow(stockTable, Priority.ALWAYS);
    VBox.setVgrow(content, Priority.ALWAYS);
    root.getChildren().addAll(searchBox, content);
  }

  /**
   * Creates the right panel containing the trade panel.
   *
   * @return VBox with trade section
   */
  private VBox createRightPanel() {
    VBox rightPanel = new VBox(12);
    rightPanel.setFillWidth(true);
    ScrollPane tradePanelContainer = createTradePanelContainer();
    rightPanel.getChildren().add(tradePanelContainer);
    VBox.setVgrow(tradePanelContainer, Priority.ALWAYS);
    return rightPanel;
  }

  private HBox createSearchBox() {
    HBox searchBox = new HBox(12);
    searchBox.setAlignment(Pos.CENTER_LEFT);
    searchBox.setPadding(new Insets(0, 0, 8, 0));

    Label searchLabel = new Label("Search stocks:");
    searchLabel.setFont(Font.font("System", FontWeight.MEDIUM, 12));
    searchLabel.setTextFill(Color.web("#cbd5e1"));

    searchInput.setStyle(
        "-fx-control-inner-background: rgba(15, 23, 42, 0.8); "
            + "-fx-text-fill: white; "
            + "-fx-prompt-text-fill: #94a3b8; "
            + "-fx-padding: 8px 12px; "
            + "-fx-background-radius: 6; "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-border-radius: 6; "
            + "-fx-font-size: 12;");
    searchInput.setPromptText("Search by symbol or company name...");
    HBox.setHgrow(searchInput, Priority.ALWAYS);

    searchBox.getChildren().addAll(searchLabel, searchInput);
    return searchBox;
  }

  private VBox createTradePanel() {
    VBox tradePanel = new VBox(10);
    tradePanel.setPrefWidth(300);
    tradePanel.setPadding(new Insets(12));
    tradePanel.setStyle(
        "-fx-background-color: rgba(15, 23, 42, 0.65); "
            + "-fx-border-color: #334155; "
            + "-fx-border-radius: 8; "
            + "-fx-background-radius: 8;");

    Label panelTitle = new Label("Trade");
    panelTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
    panelTitle.setTextFill(Color.WHITE);

    selectedStockLabel.setTextFill(Color.web("#e2e8f0"));
    selectedStockLabel.setFont(Font.font("System", FontWeight.MEDIUM, 13));
    selectedPriceLabel.setTextFill(Color.web("#94a3b8"));

    Label quantityLabel = new Label("Quantity");
    quantityLabel.setTextFill(Color.web("#cbd5e1"));
    quantityInput.setPromptText("e.g. 1.25");
    quantityInput.setStyle(inputStyle());
    quantityInput.textProperty().addListener((obs, oldText, newText) -> updateAmountHint());

    Label amountLabel = new Label("Amount");
    amountLabel.setTextFill(Color.web("#cbd5e1"));
    amountInput.setPromptText("e.g. 250");
    amountInput.setStyle(inputStyle());
    amountInput.textProperty().addListener((obs, oldText, newText) -> updateAmountHint());

    amountHintLabel.setTextFill(Color.web("#94a3b8"));
    amountHintLabel.setWrapText(true);
    amountHintLabel.setText("Choose one input: Quantity or Amount.");

    buyButton.setStyle(buttonStyle("#16a34a"));
    sellButton.setStyle(buttonStyle("#dc2626"));
    buyMaxButton.setStyle(buttonStyle("#059669"));
    watchChartButton.setStyle(buttonStyle("#3b82f6"));

    buyButton.setMaxWidth(Double.MAX_VALUE);
    sellButton.setMaxWidth(Double.MAX_VALUE);
    buyMaxButton.setMaxWidth(Double.MAX_VALUE);
    watchChartButton.setMaxWidth(Double.MAX_VALUE);

    buyButton.setOnAction(event -> onBuyClicked());
    sellButton.setOnAction(event -> onSellClicked());
    buyMaxButton.setOnAction(event -> onBuyMaxClicked());
    watchChartButton.setOnAction(event -> onWatchChartClicked());

    tradeFeedbackLabel.setWrapText(true);
    tradeFeedbackLabel.setTextFill(Color.web("#94a3b8"));

    tradePanel
        .getChildren()
        .addAll(
            panelTitle,
            selectedStockLabel,
            selectedPriceLabel,
            quantityLabel,
            quantityInput,
            amountLabel,
            amountInput,
            amountHintLabel,
            buyButton,
            buyMaxButton,
            sellButton,
            watchChartButton,
            tradeFeedbackLabel);
    return tradePanel;
  }

  private ScrollPane createTradePanelContainer() {
    ScrollPane tradePanelContainer = new ScrollPane(createTradePanel());
    tradePanelContainer.setFitToWidth(true);
    tradePanelContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    tradePanelContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    tradePanelContainer.setPrefWidth(300);
    tradePanelContainer.setMinWidth(300);
    tradePanelContainer.setMaxWidth(300);
    tradePanelContainer.setStyle(
        "-fx-background: transparent; "
            + "-fx-background-color: transparent;");
    return tradePanelContainer;
  }

  private String inputStyle() {
    return "-fx-control-inner-background: rgba(2, 6, 23, 0.8); "
        + "-fx-text-fill: white; "
        + "-fx-prompt-text-fill: #64748b; "
        + "-fx-background-radius: 6; "
        + "-fx-border-color: #334155; "
        + "-fx-border-radius: 6; "
        + "-fx-padding: 8 10;";
  }

  private String buttonStyle(String color) {
    return "-fx-background-color: "
        + color
        + "; "
        + "-fx-text-fill: white; "
        + "-fx-font-weight: 700; "
        + "-fx-padding: 10 14; "
        + "-fx-background-radius: 6; "
        + "-fx-cursor: hand;";
  }

  @SuppressWarnings("unchecked")
  private TableView<Stock> createStockTable() {
    TableView<Stock> table = new TableView<>();
    table.setStyle(
        "-fx-control-inner-background: rgba(15, 23, 42, 0.5); "
            + "-fx-table-cell-border-color: #334155; "
            + "-fx-text-fill: white;");
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    table.setItems(tableData);

    final TableColumn<Stock, String> symbolCol = new TableColumn<>("Symbol");
    symbolCol.setPrefWidth(80);
    symbolCol.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getSymbol()));
    symbolCol.setStyle("-fx-text-alignment: LEFT;");

    final TableColumn<Stock, String> companyCol = new TableColumn<>("Company");
    companyCol.setPrefWidth(150);
    companyCol.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getCompany()));
    companyCol.setStyle("-fx-text-alignment: LEFT;");

    final TableColumn<Stock, BigDecimal> priceCol = new TableColumn<>("Price");
    priceCol.setPrefWidth(100);
    priceCol.setCellValueFactory(
        cellData -> new SimpleObjectProperty<>(cellData.getValue().getSalesPrice()));
    priceCol.setStyle("-fx-text-alignment: LEFT;");
    TableColumn<Stock, String> changeCol = new TableColumn<>("Change");
    changeCol.setPrefWidth(100);
    changeCol.setCellValueFactory(
        cellData -> new SimpleStringProperty(formatChangePercent(cellData.getValue())));
    changeCol.setCellFactory(col -> createChangeCell());

    table.getColumns().addAll(symbolCol, companyCol, priceCol, changeCol);
    table.getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, previous, selected) -> {
              if (selected == null) {
                return;
              }
              selectedStock = selected;
              selectedStockLabel.setText(selected.getSymbol() + " - " + selected.getCompany());
              selectedPriceLabel.setText("Price: $" + selected.getSalesPrice());
              updateAmountHint();
              if (onRowSelected != null) {
                onRowSelected.accept(selected);
              }
            });
    return table;
  }

  private TableCell<Stock, String> createChangeCell() {
    return new TableCell<>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
          setStyle("");
        } else {
          setText(item);
          if (item.startsWith("+")) {
            setStyle("-fx-text-fill: #22c55e;");
          } else if (item.startsWith("-")) {
            setStyle("-fx-text-fill: #ef4444;");
          } else {
            setStyle("-fx-text-fill: #94a3b8;");
          }
        }
      }
    };
  }

  private String formatChangePercent(Stock stock) {
    List<BigDecimal> history = stock.getHistoricalPrices();
    if (history.size() < 2) {
      return "0%";
    }
    BigDecimal previous = history.get(history.size() - 2);
    if (previous.compareTo(BigDecimal.ZERO) == 0) {
      return "0%";
    }
    BigDecimal change =
        stock.getLatestPriceChange().divide(previous, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    if (change.compareTo(BigDecimal.ZERO) == 0) {
      return "0%";
    }
    String prefix = change.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
    return prefix + String.format("%.2f", change) + "%";
  }

  private void onBuyClicked() {
    if (selectedStock == null) {
      showTradeError("Select a stock first.");
      return;
    }
    BigDecimal quantity = resolveTradeQuantity();
    if (quantity == null) {
      return;
    }
    if (onBuyRequested != null) {
      onBuyRequested.accept(selectedStock, quantity);
    }
  }

  private void onSellClicked() {
    if (selectedStock == null) {
      showTradeError("Select a stock first.");
      return;
    }
    BigDecimal quantity = resolveTradeQuantity();
    if (quantity == null) {
      return;
    }
    if (onSellRequested != null) {
      onSellRequested.accept(selectedStock, quantity);
    }
  }

  private void onBuyMaxClicked() {
    if (selectedStock == null) {
      showTradeError("Select a stock first.");
      return;
    }
    if (onBuyMaxRequested != null) {
      onBuyMaxRequested.accept(selectedStock);
    }
  }

  private void onWatchChartClicked() {
    if (selectedStock == null) {
      showTradeError("Select a stock first.");
      return;
    }
    showChartPopup(selectedStock);
  }

  /**
   * Refreshes the selected stock details and updates the chart if it is open.
   */
  public void refreshSelectedStock() {
    if (selectedStock == null) {
      return;
    }
    selectedPriceLabel.setText("Price: $" + selectedStock.getSalesPrice());
    updateAmountHint();
    if (chartStage != null && chartStage.isShowing()) {
      updateChart(selectedStock);
    }
  }

  private BigDecimal resolveTradeQuantity() {
    String quantityText = quantityInput.getText();
    String amountText = amountInput.getText();
    boolean hasQuantity = quantityText != null && !quantityText.isBlank();
    boolean hasAmount = amountText != null && !amountText.isBlank();
    if (!hasQuantity && !hasAmount) {
      showTradeInputRequiredDialog();
      showTradeError("Enter quantity or amount.");
      return null;
    }
    if (hasQuantity && hasAmount) {
      showTradeInputConflictDialog();
      showTradeError("Use either quantity or amount, not both.");
      return null;
    }
    if (hasQuantity) {
      return parsePositiveDecimal(quantityText, "Enter a valid quantity.");
    }
    BigDecimal amount = parsePositiveDecimal(amountText, "Enter a valid amount.");
    if (amount == null) {
      return null;
    }
    return amount.divide(selectedStock.getSalesPrice(), 6, RoundingMode.HALF_UP);
  }

  private BigDecimal parsePositiveDecimal(String input, String errorMessage) {
    try {
      BigDecimal value = new BigDecimal(input.trim().replace(',', '.'));
      if (value.compareTo(BigDecimal.ZERO) <= 0) {
        showTradeError(errorMessage);
        return null;
      }
      return value;
    } catch (NumberFormatException exception) {
      showTradeError(errorMessage);
      return null;
    }
  }

  private void updateAmountHint() {
    String quantityText = quantityInput.getText();
    String amountText = amountInput.getText();
    boolean hasQuantity = quantityText != null && !quantityText.isBlank();
    boolean hasAmount = amountText != null && !amountText.isBlank();
    if (hasQuantity && hasAmount) {
      amountHintLabel.setText("Use either quantity or amount.");
      amountHintLabel.setTextFill(Color.web("#fbbf24"));
      return;
    }
    amountHintLabel.setTextFill(Color.web("#94a3b8"));
    if (selectedStock == null || !hasAmount) {
      amountHintLabel.setText("Choose one input: Quantity or Amount.");
      return;
    }
    BigDecimal amount;
    try {
      amount = new BigDecimal(amountText.trim().replace(',', '.'));
      if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        amountHintLabel.setText("Choose one input: Quantity or Amount.");
        return;
      }
    } catch (NumberFormatException exception) {
      amountHintLabel.setText("Choose one input: Quantity or Amount.");
      return;
    }
    BigDecimal quantity = amount.divide(selectedStock.getSalesPrice(), 6, RoundingMode.HALF_UP);
    amountHintLabel.setText(
        "Estimated quantity: " + quantity.stripTrailingZeros().toPlainString() + " shares");
  }

  private void showTradeInputConflictDialog() {
    Stage ownerStage = null;
    if (root.getScene() != null && root.getScene().getWindow() instanceof Stage stage) {
      ownerStage = stage;
    }
    AppDialog.showError(
        ownerStage,
        "Choose One Input",
        "Use either Quantity or Amount for the trade, not both at the same time.");
  }

  private void showTradeInputRequiredDialog() {
    Stage ownerStage = null;
    if (root.getScene() != null && root.getScene().getWindow() instanceof Stage stage) {
      ownerStage = stage;
    }
    AppDialog.showError(
        ownerStage,
        "Missing Trade Input",
        "Enter either Quantity or Amount before submitting the trade.");
  }

  /**
   * Updates the stock table with data.
   *
   * @param stocks the list of stocks to display
   */
  public void updateStocks(List<Stock> stocks) {
    tableData.setAll(stocks);
  }

  /**
   * Sets the callback when a row is selected.
   *
   * @param callback row selected callback
   */
  public void setOnRowSelected(Consumer<Stock> callback) {
    this.onRowSelected = callback;
  }

  /**
   * Sets callback for buy requests.
   *
   * @param callback callback with selected stock and quantity
   */
  public void setOnBuyRequested(BiConsumer<Stock, BigDecimal> callback) {
    this.onBuyRequested = callback;
  }

  /**
   * Sets callback for sell requests.
   *
   * @param callback callback with selected stock and quantity
   */
  public void setOnSellRequested(BiConsumer<Stock, BigDecimal> callback) {
    this.onSellRequested = callback;
  }

  /**
   * Sets callback for buy-max requests.
   *
   * @param callback callback with selected stock
   */
  public void setOnBuyMaxRequested(Consumer<Stock> callback) {
    this.onBuyMaxRequested = callback;
  }

  /**
   * Shows an inline success message.
   *
   * @param message success message
   */
  public void showTradeSuccess(String message) {
    tradeFeedbackLabel.setTextFill(Color.web("#22c55e"));
    tradeFeedbackLabel.setText(message);
  }

  /**
   * Shows an inline error message.
   *
   * @param message error message
   */
  public void showTradeError(String message) {
    tradeFeedbackLabel.setTextFill(Color.web("#ef4444"));
    tradeFeedbackLabel.setText(message);
  }

  /**
   * Gets the search input text.
   *
   * @return the search query text
   */
  public String getSearchQuery() {
    return searchInput.getText();
  }

  /**
   * Gets the search input field for binding listeners.
   *
   * @return the TextField for the search input
   */
  public TextField getSearchInput() {
    return searchInput;
  }

  /**
   * Displays the price chart in a separate pop-up window.
   *
   * @param stock the stock to display
   */
  private void showChartPopup(Stock stock) {
    if (chartStage == null) {
      chartStage = createChartStage();
    }
    updateChart(stock);
    if (!chartStage.isShowing()) {
      chartStage.show();
    } else {
      chartStage.toFront();
    }
  }

  private Stage createChartStage() {
    Stage stage = new Stage();
    stage.setTitle("Price Chart");
    stage.setMinWidth(480);
    stage.setMinHeight(360);
    stage.setOnHidden(event -> chartStage = null);
    return stage;
  }

  private void updateChart(Stock stock) {
    PriceChartComponent chartComponent = new PriceChartComponent(stock);
    if (chartStage.getScene() == null) {
      Scene scene = new Scene(chartComponent.getRoot(), CHART_WINDOW_WIDTH,
          CHART_WINDOW_HEIGHT);
      scene.setFill(Color.web("#0f172a"));
      chartStage.setScene(scene);
    } else {
      chartStage.getScene().setRoot(chartComponent.getRoot());
    }
    chartStage.setTitle("Price Chart - " + stock.getSymbol());
  }

  /**
   * Gets the root VBox container.
   *
   * @return the root VBox of this view
   */
  public VBox getRoot() {
    return root;
  }
}
