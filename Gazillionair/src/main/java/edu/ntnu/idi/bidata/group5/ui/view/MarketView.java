package edu.ntnu.idi.bidata.group5.ui.view;

import edu.ntnu.idi.bidata.group5.model.Stock;
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
  private Consumer<Stock> onRowSelected;
  private BiConsumer<Stock, BigDecimal> onBuyRequested;
  private BiConsumer<Stock, BigDecimal> onSellRequested;
  private Consumer<Stock> onBuyMaxRequested;
  private Stock selectedStock;

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
    initializeUi();
  }

  private void initializeUi() {
    root.setStyle("-fx-background-color: transparent;");
    HBox searchBox = createSearchBox();
    HBox content = new HBox(16, stockTable, createTradePanelContainer());
    HBox.setHgrow(stockTable, Priority.ALWAYS);
    VBox.setVgrow(content, Priority.ALWAYS);
    root.getChildren().addAll(searchBox, content);
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

    Label amountLabel = new Label("Buy for amount");
    amountLabel.setTextFill(Color.web("#cbd5e1"));
    amountInput.setPromptText("e.g. 250");
    amountInput.setStyle(inputStyle());
    amountInput.textProperty().addListener((obs, oldText, newText) -> updateAmountHint());

    amountHintLabel.setTextFill(Color.web("#94a3b8"));
    amountHintLabel.setWrapText(true);

    buyButton.setStyle(buttonStyle("#16a34a"));
    sellButton.setStyle(buttonStyle("#dc2626"));
    buyMaxButton.setStyle(buttonStyle("#059669"));

    buyButton.setMaxWidth(Double.MAX_VALUE);
    sellButton.setMaxWidth(Double.MAX_VALUE);
    buyMaxButton.setMaxWidth(Double.MAX_VALUE);

    buyButton.setOnAction(event -> onBuyClicked());
    sellButton.setOnAction(event -> onSellClicked());
    buyMaxButton.setOnAction(event -> onBuyMaxClicked());

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
    BigDecimal quantity = resolveBuyQuantity();
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
    BigDecimal quantity = parsePositiveDecimal(quantityInput.getText(), "Enter a valid quantity.");
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

  private BigDecimal resolveBuyQuantity() {
    String quantityText = quantityInput.getText();
    String amountText = amountInput.getText();
    boolean hasQuantity = quantityText != null && !quantityText.isBlank();
    boolean hasAmount = amountText != null && !amountText.isBlank();
    if (!hasQuantity && !hasAmount) {
      showTradeError("Enter quantity or amount.");
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
    if (selectedStock == null || amountInput.getText().isBlank()) {
      amountHintLabel.setText("Enter quantity or amount.");
      return;
    }
    BigDecimal amount;
    try {
      amount = new BigDecimal(amountInput.getText().trim().replace(',', '.'));
      if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        amountHintLabel.setText("Enter quantity or amount.");
        return;
      }
    } catch (NumberFormatException exception) {
      amountHintLabel.setText("Enter quantity or amount.");
      return;
    }
    BigDecimal quantity = amount.divide(selectedStock.getSalesPrice(), 6, RoundingMode.HALF_UP);
    amountHintLabel.setText(
        "Estimated quantity: " + quantity.stripTrailingZeros().toPlainString() + " shares");
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
   * Gets the root VBox container.
   *
   * @return the root VBox of this view
   */
  public VBox getRoot() {
    return root;
  }
}
