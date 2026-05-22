package edu.ntnu.idi.bidata.group5.ui.view.components;

import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Confirmation dialogs that preview transaction values before a trade is completed.
 */
public final class TradePreviewDialog {

  private TradePreviewDialog() {
  }

  /**
   * Shows a confirmation dialog for a purchase preview.
   *
   * @param stage owner stage
   * @param purchase uncommitted purchase preview
   * @return {@code true} if the user confirms the purchase
   */
  public static boolean confirmPurchase(Stage stage, Purchase purchase) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    if (stage != null) {
      alert.initOwner(stage);
    }
    alert.setTitle("Confirm Purchase");
    alert.setHeaderText("Review purchase before confirming");
    alert.setContentText(
        "Symbol: "
            + purchase.getShare().getStock().getSymbol()
            + "\nQuantity: "
            + purchase.getShare().getQuantity().stripTrailingZeros().toPlainString()
            + "\nPrice: "
            + formatMoney(purchase.getShare().getPurchasePrice())
            + "\nGross: "
            + formatMoney(purchase.getCalculator().calculateGross())
            + "\nCommission: "
            + formatMoney(purchase.getCalculator().calculateCommission())
            + "\nTotal Cost: "
            + formatMoney(purchase.getCalculator().calculateTotal()));
    return alert.showAndWait().filter(ButtonType.OK::equals).isPresent();
  }

  /**
   * Shows a confirmation dialog for a sale preview.
   *
   * @param stage owner stage
   * @param sale uncommitted sale preview
   * @return {@code true} if the user confirms the sale
   */
  public static boolean confirmSale(Stage stage, Sale sale) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    if (stage != null) {
      alert.initOwner(stage);
    }
    alert.setTitle("Confirm Sale");
    alert.setHeaderText("Review sale before confirming");
    alert.setContentText(
        "Symbol: "
            + sale.getShare().getStock().getSymbol()
            + "\nQuantity: "
            + sale.getShare().getQuantity().stripTrailingZeros().toPlainString()
            + "\nSale Price: "
            + formatMoney(sale.getShare().getStock().getSalesPrice())
            + "\nGross: "
            + formatMoney(sale.getCalculator().calculateGross())
            + "\nCommission: "
            + formatMoney(sale.getCalculator().calculateCommission())
            + "\nTax: "
            + formatMoney(sale.getCalculator().calculateTax())
            + "\nNet Received: "
            + formatMoney(sale.getCalculator().calculateTotal()));
    return alert.showAndWait().filter(ButtonType.OK::equals).isPresent();
  }

  private static String formatMoney(BigDecimal amount) {
    return "$" + amount.setScale(2, RoundingMode.HALF_UP);
  }
}
