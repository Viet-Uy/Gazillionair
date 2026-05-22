package edu.ntnu.idi.bidata.group5.ui.view;

import edu.ntnu.idi.bidata.group5.model.Transaction;
import edu.ntnu.idi.bidata.group5.ui.controller.TransactionsController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * View for browsing, filtering, and inspecting transaction history.
 */
public class TransactionsView {

  private final VBox root;
  private final Label summaryLabel;
  private final TextField searchInput;
  private final ComboBox<String> typeFilter;
  private final ComboBox<String> weekFilter;
  private final ListView<Transaction> transactionsList;
  private final Label detailLabel;
  private final TransactionsController controller;

  /**
   * Creates a transactions view.
   *
   * @param controller transactions controller
   */
  public TransactionsView(TransactionsController controller) {
    if (controller == null) {
      throw new IllegalArgumentException("Controller cannot be null");
    }
    this.controller = controller;
    this.root = new VBox(12);
    this.summaryLabel = new Label();
    this.searchInput = new TextField();
    this.typeFilter = new ComboBox<>();
    this.weekFilter = new ComboBox<>();
    this.transactionsList = new ListView<>();
    this.detailLabel = new Label("Select a transaction to view details.");
    initialize();
  }

  private void initialize() {
    root.setStyle(
        "-fx-padding: 16; "
            + "-fx-background-color: linear-gradient(to bottom, rgba(15, 23, 42, 0.60), "
            + "rgba(30, 41, 59, 0.75)); "
            + "-fx-background-radius: 12;");
    summaryLabel.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 14;");

    HBox filterBar = createFilterBar();
    VBox listCard = createTransactionsCard();
    VBox detailCard = createDetailCard();

    root.getChildren().addAll(summaryLabel, filterBar, listCard, detailCard);
    VBox.setVgrow(listCard, Priority.ALWAYS);
    refresh();
  }

  private HBox createFilterBar() {
    HBox filterBar = new HBox(12);
    filterBar.setAlignment(Pos.CENTER_LEFT);

    searchInput.setPromptText("Search by symbol or company...");
    searchInput.setStyle(inputStyle());
    HBox.setHgrow(searchInput, Priority.ALWAYS);
    searchInput.textProperty().addListener((obs, oldValue, newValue) -> applyFilters());

    typeFilter.setItems(FXCollections.observableArrayList("All Types", "Purchases", "Sales"));
    typeFilter.setValue("All Types");
    typeFilter.setStyle(inputStyle());
    typeFilter.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());

    weekFilter.setItems(FXCollections.observableArrayList("All Weeks"));
    weekFilter.setValue("All Weeks");
    weekFilter.setStyle(inputStyle());
    weekFilter.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());

    filterBar.getChildren().addAll(searchInput, typeFilter, weekFilter);
    return filterBar;
  }

  private VBox createTransactionsCard() {
    Label transactionsTitle = new Label("Transaction History");
    transactionsTitle.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 14; -fx-font-weight: 700;");

    transactionsList.setStyle(
        "-fx-control-inner-background: rgba(15, 23, 42, 0.70); "
            + "-fx-background-color: rgba(15, 23, 42, 0.70); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-border-radius: 8; "
            + "-fx-background-radius: 8;");
    transactionsList.setCellFactory(listView -> new ListCell<>() {
      @Override
      protected void updateItem(Transaction item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(
              item.getClass().getSimpleName()
                  + " | "
                  + item.getShare().getStock().getSymbol()
                  + " | qty "
                  + item.getShare().getQuantity().stripTrailingZeros().toPlainString()
                  + " | week "
                  + item.getWeek());
        }
      }
    });
    transactionsList.getSelectionModel()
        .selectedItemProperty()
        .addListener((obs, oldValue, newValue) -> detailLabel.setText(
            controller.getTransactionDetails(newValue)));

    VBox card = new VBox(8, transactionsTitle, transactionsList);
    VBox.setVgrow(transactionsList, Priority.ALWAYS);
    return card;
  }

  private VBox createDetailCard() {
    Label detailTitle = new Label("Transaction Details");
    detailTitle.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 14; -fx-font-weight: 700;");

    detailLabel.setWrapText(true);
    detailLabel.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 13;");

    VBox card = new VBox(8, detailTitle, detailLabel);
    card.setPadding(new Insets(12));
    card.setStyle(
        "-fx-background-color: rgba(15, 23, 42, 0.65); "
            + "-fx-border-color: #334155; "
            + "-fx-border-radius: 8; "
            + "-fx-background-radius: 8;");
    return card;
  }

  /**
   * Refreshes transaction data from controller.
   */
  public void refresh() {
    summaryLabel.setText(
        "Total: "
            + controller.getTransactions().size()
            + "  | Purchases: "
            + controller.getPurchases().size()
            + "  | Sales: "
            + controller.getSales().size());

    weekFilter.setItems(FXCollections.observableArrayList("All Weeks"));
    weekFilter.getItems().addAll(controller.getWeekFilterOptions());
    if (!weekFilter.getItems().contains(weekFilter.getValue())) {
      weekFilter.setValue("All Weeks");
    }

    applyFilters();
  }

  private void applyFilters() {
    transactionsList.setItems(FXCollections.observableArrayList(
        controller.filterTransactions(
            typeFilter.getValue(),
            weekFilter.getValue(),
            searchInput.getText())));

    if (transactionsList.getItems().isEmpty()) {
      detailLabel.setText("No transactions match the selected filters.");
      return;
    }

    if (transactionsList.getSelectionModel().getSelectedItem() == null) {
      transactionsList.getSelectionModel().selectFirst();
    }
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

  /**
   * Returns the root node.
   *
   * @return root container
   */
  public VBox getRoot() {
    return root;
  }
}
