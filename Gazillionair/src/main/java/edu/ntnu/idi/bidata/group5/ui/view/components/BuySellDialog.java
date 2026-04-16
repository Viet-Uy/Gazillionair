package edu.ntnu.idi.bidata.group5.ui.view.components;

import java.util.Optional;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 * Utility dialog for selecting buy/sell action and quantity.
 */
public class BuySellDialog {

  /**
   * Trade action.
   */
  public enum TradeAction {
    BUY,
    SELL
  }

  /**
   * Trade request returned from dialog.
   *
   * @param action selected action
   * @param quantity selected quantity
   */
  public record TradeRequest(TradeAction action, int quantity) {
  }

  private BuySellDialog() {
  }

  /**
   * Shows buy/sell + quantity prompts.
   *
   * @param stage owner stage
   * @param symbol stock symbol
   * @return optional trade request
   */
  public static Optional<TradeRequest> show(Stage stage, String symbol) {
    ChoiceDialog<TradeAction> actionDialog = new ChoiceDialog<>(TradeAction.BUY, TradeAction.BUY,
        TradeAction.SELL);
    if (stage != null) {
      actionDialog.initOwner(stage);
    }
    actionDialog.setTitle("Trade Stock");
    actionDialog.setHeaderText("Select action for " + symbol);
    actionDialog.setContentText("Action:");
    Optional<TradeAction> action = actionDialog.showAndWait();
    if (action.isEmpty()) {
      return Optional.empty();
    }

    TextInputDialog quantityDialog = new TextInputDialog("1");
    if (stage != null) {
      quantityDialog.initOwner(stage);
    }
    quantityDialog.setTitle("Trade Quantity");
    quantityDialog.setHeaderText("Enter quantity for " + symbol);
    quantityDialog.setContentText("Quantity:");

    Optional<String> quantityInput = quantityDialog.showAndWait();
    if (quantityInput.isEmpty()) {
      return Optional.empty();
    }

    int quantity;
    try {
      quantity = Integer.parseInt(quantityInput.get().trim());
    } catch (NumberFormatException e) {
      return Optional.empty();
    }

    if (quantity <= 0) {
      return Optional.empty();
    }

    return Optional.of(new TradeRequest(action.get(), quantity));
  }
}
