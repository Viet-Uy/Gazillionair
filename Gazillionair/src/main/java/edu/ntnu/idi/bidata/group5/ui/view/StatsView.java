package edu.ntnu.idi.bidata.group5.ui.view;

import edu.ntnu.idi.bidata.group5.model.Stock;
import edu.ntnu.idi.bidata.group5.ui.controller.StatsController;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.math.BigDecimal;
import java.util.List;

/**
 * View for showing market gainers and losers.
 */
public class StatsView {

  private final VBox root;
  private final Label summaryLabel;
  private final ListView<String> gainersList;
  private final ListView<String> losersList;
  private final StatsController controller;

  /**
   * Creates a stats view.
   *
   * @param controller stats controller
   */
  public StatsView(StatsController controller) {
    if (controller == null) {
      throw new IllegalArgumentException("Controller cannot be null");
    }
    this.controller = controller;
    this.root = new VBox(12);
    this.summaryLabel = new Label();
    this.gainersList = new ListView<>();
    this.losersList = new ListView<>();
    initialize();
  }

  private void initialize() {
    root.setStyle("-fx-padding: 16;");
    summaryLabel.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 14;");
    HBox lists = new HBox(12);
    VBox gainersBox = createListCard("Top Gainers", gainersList);
    VBox losersBox = createListCard("Top Losers", losersList);
    lists.getChildren().addAll(gainersBox, losersBox);
    root.getChildren().addAll(summaryLabel, lists);
    refresh();
  }

  /**
   * Refreshes stats from controller.
   */
  public void refresh() {
    int count = 3;
    if (!controller.hasWeeklyPriceChanges()) {
      summaryLabel.setText("No weekly movement yet. Press Next Week to see top movers.");
      gainersList.setItems(FXCollections.observableArrayList());
      losersList.setItems(FXCollections.observableArrayList());
      return;
    }

    summaryLabel.setText("Showing top " + count + " gainers and losers this week");
    List<String> gainers = controller.getTopGainers(count).stream()
        .filter(stock -> stock.getLatestPriceChange().compareTo(BigDecimal.ZERO) > 0)
        .map(this::stockText)
        .toList();
    List<String> losers = controller.getTopLosers(count).stream()
        .filter(stock -> stock.getLatestPriceChange().compareTo(BigDecimal.ZERO) < 0)
        .map(this::stockText)
        .toList();

    gainersList.setItems(FXCollections.observableArrayList(gainers));
    losersList.setItems(FXCollections.observableArrayList(losers));
  }

  private String stockText(Stock stock) {
    BigDecimal change = stock.getLatestPriceChange();
    String sign = change.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
    return stock.getSymbol() + " (" + sign + change + ")";
  }

  private VBox createListCard(String title, ListView<String> listView) {
    Label titleLabel = new Label(title);
    titleLabel.setStyle("-fx-text-fill: #0f172a; -fx-font-size: 16; -fx-font-weight: 700;");
    listView.setStyle(
        "-fx-control-inner-background: #f8fafc; "
            + "-fx-background-color: #f8fafc; "
            + "-fx-border-color: #e2e8f0; "
            + "-fx-border-radius: 8; "
            + "-fx-background-radius: 8;");
    VBox card = new VBox(8, titleLabel, listView);
    card.setStyle(
        "-fx-background-color: rgba(255, 255, 255, 0.95); "
            + "-fx-padding: 12; "
            + "-fx-background-radius: 10;");
    return card;
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
