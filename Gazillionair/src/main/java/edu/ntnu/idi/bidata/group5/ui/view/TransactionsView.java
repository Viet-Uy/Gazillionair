package edu.ntnu.idi.bidata.group5.ui.view;

import edu.ntnu.idi.bidata.group5.model.Transaction;
import edu.ntnu.idi.bidata.group5.ui.controller.TransactionsController;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

/**
 * View for showing transaction history summaries.
 */
public class TransactionsView {

  private final VBox root;
  private final Label summaryLabel;
  private final ListView<String> transactionsList;
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
    this.transactionsList = new ListView<>();
    initialize();
  }

  private void initialize() {
    root.setStyle("-fx-padding: 16;");
    summaryLabel.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 14;");
    root.getChildren().addAll(summaryLabel, transactionsList);
    refresh();
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
    transactionsList.setItems(FXCollections.observableArrayList(
        controller.getTransactions().stream()
            .map(this::toRowText)
            .toList()));
  }

  private String toRowText(Transaction transaction) {
    return transaction.getClass().getSimpleName()
        + " | "
        + transaction.getShare().getStock().getSymbol()
        + " | qty "
        + transaction.getShare().getQuantity()
        + " | week "
        + transaction.getWeek();
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
