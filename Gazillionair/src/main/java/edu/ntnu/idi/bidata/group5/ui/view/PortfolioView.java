package edu.ntnu.idi.bidata.group5.ui.view;

import edu.ntnu.idi.bidata.group5.ui.controller.PortfolioController;
import java.math.BigDecimal;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

/**
 * View for showing current holdings and portfolio summary.
 */
public class PortfolioView {

  private final VBox root;
  private final Label summaryLabel;
  private final ListView<String> holdingsList;
  private final Button sellAllButton;
  private final PortfolioController controller;

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
    this.root = new VBox(12);
    this.summaryLabel = new Label();
    this.holdingsList = new ListView<>();
    this.sellAllButton = new Button("Sell All Holdings");
    initialize();
  }

  private void initialize() {
    root.setStyle("-fx-padding: 16;");
    summaryLabel.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 14;");
    sellAllButton.setStyle(
        "-fx-background-color: #ef4444; -fx-text-fill: white; -fx-padding: 8 16; -fx-cursor: hand;");
    sellAllButton.setOnAction(ignored -> {
      controller.sellAll();
      refresh();
    });
    root.getChildren().addAll(summaryLabel, sellAllButton, holdingsList);
    refresh();
  }

  /**
   * Refreshes holdings and summary values from controller.
   */
  public void refresh() {
    BigDecimal portfolioValue = controller.getPortfolioValue();
    BigDecimal cash = controller.getCashBalance();
    summaryLabel.setText(
        "Holdings: "
            + controller.getHoldings().size()
            + "  | Portfolio Value: $"
            + String.format("%.2f", portfolioValue)
            + "  | Cash: $"
            + String.format("%.2f", cash));
    holdingsList.setItems(FXCollections.observableArrayList(
        controller.getHoldings().stream()
            .map(share -> share.getStock().getSymbol() + " x " + share.getQuantity())
            .toList()));
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
