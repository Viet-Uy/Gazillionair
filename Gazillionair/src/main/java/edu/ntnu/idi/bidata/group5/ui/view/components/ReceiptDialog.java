package edu.ntnu.idi.bidata.group5.ui.view.components;

import edu.ntnu.idi.bidata.group5.model.Transaction;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Utility dialog for showing transaction receipts and errors.
 */
public class ReceiptDialog {

  private ReceiptDialog() {
  }

  /**
   * Shows a success receipt for a committed transaction.
   *
   * @param stage owner stage
   * @param transaction committed transaction
   */
  public static void showTransaction(Stage stage, Transaction transaction) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    if (stage != null) {
      alert.initOwner(stage);
    }
    alert.setTitle("Transaction Receipt");
    alert.setHeaderText("Transaction completed");
    alert.setContentText(
        "Type: "
            + transaction.getClass().getSimpleName()
            + "\nSymbol: "
            + transaction.getShare().getStock().getSymbol()
            + "\nQuantity: "
            + transaction.getShare().getQuantity()
            + "\nWeek: "
            + transaction.getWeek()
            + "\nTotal: "
            + transaction.getCalculator().calculateTotal());
    alert.showAndWait();
  }

  /**
   * Shows an error alert.
   *
   * @param stage owner stage
   * @param message error message
   */
  public static void showError(Stage stage, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    if (stage != null) {
      alert.initOwner(stage);
    }
    alert.setTitle("Transaction Error");
    alert.setHeaderText("Unable to complete transaction");
    alert.setContentText(message);
    alert.showAndWait();
  }
}
