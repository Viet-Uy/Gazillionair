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
import javafx.util.StringConverter;

/**
 * PriceChartComponent displays a stock's price history as a line chart.
 * Shows price trends over time as the game progresses through weeks.
 * Provides visual representation of how stock prices have changed.
 */
public class PriceChartComponent {

  private static final String AXIS_TICK_COLOR = "#cbd5e1";
  private static final String AXIS_LABEL_COLOR = "#f8fafc";

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
        "-fx-background-color: #1e293b; "
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
    List<BigDecimal> prices = stock.getHistoricalPrices();
    int maxWeek = Math.max(1, prices.size());
    int tickUnit = Math.max(1, (int) Math.ceil(maxWeek / 10.0));

    NumberAxis numberAxisX = new NumberAxis(1, maxWeek, tickUnit);
    numberAxisX.setLabel("Week");
    numberAxisX.setStyle("-fx-tick-label-fill: " + AXIS_TICK_COLOR + ";");
    numberAxisX.setMinorTickVisible(false);
    numberAxisX.setMinorTickCount(0);
    numberAxisX.setTickLabelFormatter(new StringConverter<>() {
      @Override
      public String toString(Number value) {
        return String.valueOf(value.intValue());
      }

      @Override
      public Number fromString(String value) {
        return Integer.parseInt(value);
      }
    });

    NumberAxis numberAxisY = new NumberAxis();
    numberAxisY.setLabel("Price ($)");
    numberAxisY.setStyle("-fx-tick-label-fill: " + AXIS_TICK_COLOR + ";");

    LineChart<Number, Number> lineChart = new LineChart<>(numberAxisX, numberAxisY);
    lineChart.setTitle(null);
    lineChart.setHorizontalZeroLineVisible(false);
    lineChart.setVerticalZeroLineVisible(false);
    lineChart.setStyle(
        "-fx-background-color: transparent; "
            + "-fx-legend-visible: false; "
            + "-fx-padding: 16; "
            + "-fx-plot-background-color: rgba(15, 23, 42, 0.55); "
            + "-fx-grid-line-color: #334155;");

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName("Price");

    for (int i = 0; i < prices.size(); i++) {
      series.getData().add(new XYChart.Data<>(i + 1, prices.get(i)));
    }

    lineChart.getData().add(series);
    styleChartSeries(series);
    styleAxisLabels(numberAxisX, numberAxisY);

    return lineChart;
  }

  /**
   * Applies a higher-contrast color to axis labels so they remain readable on the popup background.
   *
   * @param numberAxisX the week axis
   * @param numberAxisY the price axis
   */
  private void styleAxisLabels(NumberAxis numberAxisX, NumberAxis numberAxisY) {
    numberAxisX.applyCss();
    numberAxisY.applyCss();

    if (numberAxisX.lookup(".axis-label") != null) {
      numberAxisX.lookup(".axis-label").setStyle("-fx-text-fill: " + AXIS_LABEL_COLOR + ";");
    }
    if (numberAxisY.lookup(".axis-label") != null) {
      numberAxisY.lookup(".axis-label").setStyle("-fx-text-fill: " + AXIS_LABEL_COLOR + ";");
    }
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
