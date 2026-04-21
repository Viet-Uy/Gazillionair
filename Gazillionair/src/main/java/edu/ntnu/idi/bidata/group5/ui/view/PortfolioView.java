package edu.ntnu.idi.bidata.group5.ui.view;

import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Share;
import edu.ntnu.idi.bidata.group5.ui.controller.PortfolioController;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * View for showing current holdings and portfolio summary.
 */
public class PortfolioView {

  private final HBox root;
  private final Label summaryLabel;
  private final ListView<String> holdingsList;
  private final Button sellAllButton;
  private final Label selectedHoldingLabel;
  private final TextField sellQuantityInput;
  private final Button sellAllSelectedButton;
  private final Button sellButton;
  private final Label feedbackLabel;
  private final PortfolioController controller;
  private List<Share> currentHoldings;
  private Share selectedHolding;

  /**
   * Creates a portfolio view.
   *
   * @param controller portfolio controller
   */
  public PortfolioView(PortfolioController controller) {
    if (controller == null) {
      throw new IllegalArgumentException("Controller cannot be null");
    }
    this.controller = controller;
    this.root = new HBox(16);
    this.summaryLabel = new Label();
    this.holdingsList = new ListView<>();
    this.sellAllButton = new Button("Sell All Holdings");
    this.selectedHoldingLabel = new Label("Select a holding");
    this.sellQuantityInput = new TextField();
    this.sellAllSelectedButton = new Button("Sell All Selected");
    this.sellButton = new Button("Sell");
    this.feedbackLabel = new Label();
    this.currentHoldings = List.of();
    initialize();
  }

  private void initialize() {
    root.setStyle(
        "-fx-padding: 16; "
            + "-fx-background-color: linear-gradient(to bottom, rgba(15, 23, 42, 0.60), "
            + "rgba(30, 41, 59, 0.75)); "
            + "-fx-background-radius: 12;");
    summaryLabel.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 14;");
    sellAllButton.setStyle(
        "-fx-background-color: #ef4444; "
            + "-fx-text-fill: white; "
            + "-fx-padding: 8 16; "
            + "-fx-cursor: hand;");
    holdingsList.setStyle(
        "-fx-control-inner-background: rgba(15, 23, 42, 0.70); "
            + "-fx-background-color: rgba(15, 23, 42, 0.70); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-border-radius: 8; "
            + "-fx-background-radius: 8;");
    VBox listPanel = new VBox(12, summaryLabel, sellAllButton, holdingsList);
    HBox.setHgrow(listPanel, Priority.ALWAYS);
    VBox.setVgrow(holdingsList, Priority.ALWAYS);

    selectedHoldingLabel.setStyle(
        "-fx-text-fill: #e2e8f0; -fx-font-size: 13; -fx-font-weight: 700;");
    Label quantityLabel = new Label("Sell quantity");
    quantityLabel.setStyle("-fx-text-fill: #cbd5e1;");
    sellQuantityInput.setPromptText("e.g. 1.25");
    sellQuantityInput.setStyle(
        "-fx-control-inner-background: rgba(2, 6, 23, 0.8); "
            + "-fx-text-fill: white; "
            + "-fx-prompt-text-fill: #64748b; "
            + "-fx-background-radius: 6; "
            + "-fx-border-color: #334155; "
            + "-fx-border-radius: 6; "
            + "-fx-padding: 8 10;");
    sellButton.setMaxWidth(Double.MAX_VALUE);
    sellAllSelectedButton.setMaxWidth(Double.MAX_VALUE);
    sellButton.setStyle(
        "-fx-background-color: #dc2626; "
            + "-fx-text-fill: white; "
            + "-fx-font-weight: 700; "
            + "-fx-padding: 10 14; "
            + "-fx-background-radius: 6; "
            + "-fx-cursor: hand;");
    sellAllSelectedButton.setStyle(
        "-fx-background-color: #b91c1c; "
            + "-fx-text-fill: white; "
            + "-fx-font-weight: 700; "
            + "-fx-padding: 10 14; "
            + "-fx-background-radius: 6; "
            + "-fx-cursor: hand;");
    feedbackLabel.setWrapText(true);
    feedbackLabel.setStyle("-fx-text-fill: #94a3b8;");

    VBox actionPanel =
        new VBox(
            10,
            new Label("Sell Holding"),
            selectedHoldingLabel,
            quantityLabel,
            sellQuantityInput,
            sellAllSelectedButton,
            sellButton,
            feedbackLabel);
    actionPanel.setPrefWidth(280);
    actionPanel.setStyle(
        "-fx-background-color: rgba(15, 23, 42, 0.65); "
            + "-fx-border-color: #334155; "
            + "-fx-border-radius: 8; "
            + "-fx-background-radius: 8; "
            + "-fx-padding: 12;");

    sellAllButton.setOnAction(
        ignored -> {
          List<Sale> sales = controller.sellAll();
          BigDecimal totalNet = sales.stream()
              .map(sale -> sale.getCalculator().calculateTotal())
              .reduce(BigDecimal.ZERO, BigDecimal::add);
          BigDecimal totalFee = sales.stream()
              .map(sale -> sale.getCalculator().calculateCommission())
              .reduce(BigDecimal.ZERO, BigDecimal::add);
          BigDecimal totalTax = sales.stream()
              .map(sale -> sale.getCalculator().calculateTax())
              .reduce(BigDecimal.ZERO, BigDecimal::add);
          feedbackLabel.setStyle("-fx-text-fill: #22c55e;");
          feedbackLabel.setText(
              "All holdings sold | Fee "
                  + formatMoney(totalFee)
                  + ", Tax "
                  + formatMoney(totalTax)
                  + ", Net "
                  + formatMoney(totalNet));
          refresh();
        });
    holdingsList
        .getSelectionModel()
        .selectedIndexProperty()
        .addListener((obs, oldIndex, newIndex) -> onHoldingSelected(newIndex.intValue()));
    sellAllSelectedButton.setOnAction(ignored -> onSellAllSelectedClicked());
    sellButton.setOnAction(ignored -> onSellClicked());

    root.getChildren().addAll(listPanel, actionPanel);
    refresh();
  }

  /**
   * Refreshes holdings and summary values from controller.
   */
  public void refresh() {
    BigDecimal portfolioValue = controller.getPortfolioValue();
    BigDecimal cash = controller.getCashBalance();
    currentHoldings = controller.getHoldings();
    summaryLabel.setText(
        "Holdings: "
            + currentHoldings.size()
            + "  | Portfolio Value: $"
            + String.format("%.2f", portfolioValue)
            + "  | Cash: $"
            + String.format("%.2f", cash));
    holdingsList.setItems(FXCollections.observableArrayList(
        currentHoldings.stream()
            .map(share -> share.getStock().getSymbol() + " x " + share.getQuantity())
            .toList()));
  }

  private void onHoldingSelected(int selectedIndex) {
    if (selectedIndex < 0 || selectedIndex >= currentHoldings.size()) {
      selectedHolding = null;
      selectedHoldingLabel.setText("Select a holding");
      return;
    }
    selectedHolding = currentHoldings.get(selectedIndex);
    selectedHoldingLabel.setText(
        selectedHolding.getStock().getSymbol()
            + " owned: "
            + selectedHolding.getQuantity().stripTrailingZeros().toPlainString());
  }

  private void onSellClicked() {
    if (selectedHolding == null) {
      feedbackLabel.setStyle("-fx-text-fill: #ef4444;");
      feedbackLabel.setText("Select a holding first.");
      return;
    }
    try {
      BigDecimal quantity = parsePositiveDecimal(sellQuantityInput.getText());
      if (quantity == null) {
        feedbackLabel.setStyle("-fx-text-fill: #ef4444;");
        feedbackLabel.setText("Enter a valid quantity.");
        return;
      }
      String symbol = selectedHolding.getStock().getSymbol();
      BigDecimal ownedQuantity = selectedHolding.getQuantity();
      if (quantity.compareTo(ownedQuantity) > 0) {
        feedbackLabel.setStyle("-fx-text-fill: #ef4444;");
        feedbackLabel.setText(
            "You only own " + ownedQuantity.stripTrailingZeros().toPlainString() + " " + symbol);
        return;
      }
      Sale sale = controller.sell(symbol, quantity);
      setSaleFeedback(sale, "Sold ", symbol);
      refresh();
    } catch (RuntimeException exception) {
      feedbackLabel.setStyle("-fx-text-fill: #ef4444;");
      feedbackLabel.setText(exception.getMessage());
    }
  }

  private void onSellAllSelectedClicked() {
    if (selectedHolding == null) {
      feedbackLabel.setStyle("-fx-text-fill: #ef4444;");
      feedbackLabel.setText("Select a holding first.");
      return;
    }
    try {
      String symbol = selectedHolding.getStock().getSymbol();
      Sale sale = controller.sellAllForSymbol(symbol);
      setSaleFeedback(sale, "Sold all ", symbol);
      refresh();
    } catch (RuntimeException exception) {
      feedbackLabel.setStyle("-fx-text-fill: #ef4444;");
      feedbackLabel.setText(exception.getMessage());
    }
  }

  private void setSaleFeedback(Sale sale, String prefix, String symbol) {
    feedbackLabel.setStyle("-fx-text-fill: #22c55e;");
    feedbackLabel.setText(
        prefix
            + sale.getShare().getQuantity().stripTrailingZeros().toPlainString()
            + " "
            + symbol
            + " | Fee "
            + formatMoney(sale.getCalculator().calculateCommission())
            + ", Tax "
            + formatMoney(sale.getCalculator().calculateTax())
            + ", Net "
            + formatMoney(sale.getCalculator().calculateTotal()));
  }

  private BigDecimal parsePositiveDecimal(String input) {
    try {
      BigDecimal value = new BigDecimal(input.trim().replace(',', '.'));
      if (value.compareTo(BigDecimal.ZERO) <= 0) {
        return null;
      }
      return value;
    } catch (NumberFormatException exception) {
      return null;
    }
  }

  private String formatMoney(BigDecimal amount) {
    return "$" + amount.setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Returns the root node.
   *
   * @return root container
   */
  public HBox getRoot() {
    return root;
  }
}
