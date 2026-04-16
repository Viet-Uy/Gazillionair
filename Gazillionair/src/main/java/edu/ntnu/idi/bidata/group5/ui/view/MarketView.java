package edu.ntnu.idi.bidata.group5.ui.view;

import edu.ntnu.idi.bidata.group5.model.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

/**
 * MarketView displays the stock market with searchable stock listings.
 * Shows a table with stock data (symbol, company, price, change, volume, 1w change).
 * Allows players to search stocks and select rows to initiate buy/sell transactions.
 */
public class MarketView {

  private final VBox root;
  private final TextField searchInput;
  private final TableView<Stock> stockTable;
  private final ObservableList<Stock> tableData;
  private Consumer<Stock> onRowSelected;

  /**
   * Constructs a MarketView with empty stock list.
   */
  public MarketView() {
    this.root = new VBox(12);
    this.root.setPadding(new Insets(16));
    this.searchInput = new TextField();
    this.tableData = FXCollections.observableArrayList();
    this.stockTable = createStockTable();
    initializeUI();
  }

  /**
   * Initializes the UI with search input and stock table.
   */
  private void initializeUI() {
    root.setStyle("-fx-background-color: transparent;");

    HBox searchBox = createSearchBox();
    root.getChildren().addAll(searchBox, stockTable);
    VBox.setVgrow(stockTable, Priority.ALWAYS);
  }

  /**
   * Creates the search box with input field and label.
   *
   * @return HBox containing search input
   */
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

  /**
   * Creates the stock data table with columns.
   *
   * @return TableView containing stock listings
   */
  private TableView<Stock> createStockTable() {
    TableView<Stock> table = new TableView<>();
    table.setStyle(
        "-fx-control-inner-background: rgba(15, 23, 42, 0.5); "
            + "-fx-table-cell-border-color: #334155; "
            + "-fx-text-fill: white;");
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    table.setItems(tableData);

    TableColumn<Stock, String> symbolCol = createColumn("Symbol", 80, "symbol");
    TableColumn<Stock, String> companyCol = createColumn("Company", 150, "company");
    TableColumn<Stock, BigDecimal> priceCol = createColumn("Price", 100, "salesPrice");
    TableColumn<Stock, String> changeCol = new TableColumn<>("Change");
    changeCol.setPrefWidth(100);
    changeCol.setCellValueFactory(
        cellData -> new javafx.beans.property.SimpleStringProperty(
            formatChange(cellData.getValue().getLatestPriceChange())));
    changeCol.setCellFactory(
        col -> new TableCell<Stock, String>() {
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
        });

    table.getColumns().addAll(symbolCol, companyCol, priceCol, changeCol);

    table.setOnMouseClicked(
        event -> {
          Stock selected = table.getSelectionModel().getSelectedItem();
          if (selected != null && onRowSelected != null) {
            onRowSelected.accept(selected);
          }
        });

    return table;
  }

  /**
   * Creates a table column with property binding.
   *
   * @param title the column title
   * @param width the column width
   * @param property the property name to bind
   * @return TableColumn with proper styling
   */
  private <T> TableColumn<Stock, T> createColumn(String title, double width, String property) {
    TableColumn<Stock, T> column = new TableColumn<>(title);
    column.setPrefWidth(width);
    column.setCellValueFactory(new PropertyValueFactory<>(property));
    column.setStyle("-fx-text-alignment: LEFT;");
    return column;
  }

  /**
   * Formats a BigDecimal change as percentage string with +/- prefix.
   *
   * @param change the price change
   * @return formatted change string
   */
  private String formatChange(BigDecimal change) {
    if (change == null || change.compareTo(BigDecimal.ZERO) == 0) {
      return "0%";
    }
    String prefix = change.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
    return prefix + String.format("%.2f", change) + "%";
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
   * @param callback the callback function to invoke with selected stock
   */
  public void setOnRowSelected(Consumer<Stock> callback) {
    this.onRowSelected = callback;
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
