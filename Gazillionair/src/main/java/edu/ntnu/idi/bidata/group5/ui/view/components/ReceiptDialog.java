package edu.ntnu.idi.bidata.group5.ui.view.components;

import edu.ntnu.idi.bidata.group5.model.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
            + transaction.getShare().getQuantity().stripTrailingZeros().toPlainString()
            + "\nWeek: "
            + transaction.getWeek()
            + "\nGross: "
            + formatMoney(transaction.getCalculator().calculateGross())
            + "\nCommission: "
            + formatMoney(transaction.getCalculator().calculateCommission())
            + "\nTax: "
            + formatMoney(transaction.getCalculator().calculateTax())
            + "\nTotal: "
            + formatMoney(transaction.getCalculator().calculateTotal()));
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

  private static String formatMoney(BigDecimal amount) {
    return "$" + amount.setScale(2, RoundingMode.HALF_UP);
  }
}
