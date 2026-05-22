package edu.ntnu.idi.bidata.group5.ui.view.components;

import edu.ntnu.idi.bidata.group5.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * PriceChartComponent displays a stock's price history as a line chart.
 * Shows price trends over time as the game progresses through weeks.
 * Provides visual representation of how stock prices have changed.
 */
public class PriceChartComponent {

  private final VBox root;
  private final LineChart<Number, Number> chart;
  private final Label titleLabel;
  private final Label metaLabel;

  /**
   * Constructs a PriceChartComponent for a given stock.
   *
   * @param stock the stock to display price history for
   */
  public PriceChartComponent(Stock stock) {
    if (stock == null) {
      throw new IllegalArgumentException("Stock cannot be null");
    }

    this.root = new VBox(12);
    this.titleLabel = new Label();
    this.metaLabel = new Label();
    this.chart = createLineChart(stock);

    initializeUi(stock);
  }

  /**
   * Initializes the component UI with styling and layout.
   *
   * @param stock the stock being displayed
   */
  private void initializeUi(Stock stock) {
    root.setPadding(new Insets(16));
    root.setStyle(
        "-fx-background-color: rgba(30, 41, 59, 0.6); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-background-radius: 8; "
            + "-fx-border-radius: 8;");

    titleLabel.setText(stock.getSymbol() + " - " + stock.getCompany());
    titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
    titleLabel.setTextFill(Color.web("#ffffff"));

    BigDecimal currentPrice = stock.getSalesPrice();
    BigDecimal highPrice = stock.getHighestPrice();
    BigDecimal lowPrice = stock.getLowestPrice();
    metaLabel.setText(
        "Current: $" + currentPrice + " | High: $" + highPrice + " | Low: $" + lowPrice);
    metaLabel.setFont(Font.font("System", 12));
    metaLabel.setTextFill(Color.web("#cbd5e1"));

    root.getChildren().addAll(titleLabel, metaLabel, chart);
  }

  /**
   * Creates a line chart displaying the stock's price history.
   *
   * @param stock the stock to create a chart for
   * @return configured LineChart
   */
  private LineChart<Number, Number> createLineChart(Stock stock) {
    NumberAxis numberAxisX = new NumberAxis();
    numberAxisX.setLabel("Week");
    numberAxisX.setStyle("-fx-tick-label-fill: #cbd5e1;");

    NumberAxis numberAxisY = new NumberAxis();
    numberAxisY.setLabel("Price ($)");
    numberAxisY.setStyle("-fx-tick-label-fill: #cbd5e1;");

    LineChart<Number, Number> lineChart = new LineChart<>(numberAxisX, numberAxisY);
    lineChart.setTitle(null);
    lineChart.setStyle(
        "-fx-background-color: transparent; "
            + "-fx-legend-visible: false; "
            + "-fx-padding: 16;");

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName("Price");

    List<BigDecimal> prices = stock.getHistoricalPrices();
    for (int i = 0; i < prices.size(); i++) {
      series.getData().add(new XYChart.Data<>(i + 1, prices.get(i)));
    }

    lineChart.getData().add(series);
    styleChartSeries(series);

    return lineChart;
  }

  /**
   * Applies styling to the chart series line.
   *
   * @param series the series to style
   */
  private void styleChartSeries(XYChart.Series<Number, Number> series) {
    series.getNode().setStyle("-fx-stroke: #22c55e; -fx-stroke-width: 2;");
    for (XYChart.Data<Number, Number> data : series.getData()) {
      data.getNode().setStyle("-fx-padding: 0; -fx-background-radius: 4; "
          + "-fx-background-color: #22c55e;");
    }
  }

  /**
   * Gets the root VBox container for this component.
   *
   * @return the root VBox
   */
  public VBox getRoot() {
    return root;
  }
}
