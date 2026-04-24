package edu.ntnu.idi.bidata.group5.ui.view;

import edu.ntnu.idi.bidata.group5.ui.view.components.NewsCard;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * NewsView displays a chronological feed of news articles.
 * Players can filter news by week, stock, or sentiment to predict market movements.
 * Includes filtering options for deeper analysis.
 */
public class NewsView {

  private final VBox root;
  private final VBox newsFeedContainer;
  private final ComboBox<String> weekFilter;
  private final ComboBox<String> sentimentFilter;
  private final Label noNewsLabel;

  /**
   * Constructs a NewsView with filter controls and empty news feed.
   */
  public NewsView() {
    this.root = new VBox(12);
    this.newsFeedContainer = new VBox(12);
    this.weekFilter = new ComboBox<>();
    this.sentimentFilter = new ComboBox<>();
    this.noNewsLabel = new Label("No news available.");

    initializeUi();
  }

  /**
   * Initializes the UI with filter controls and news feed layout.
   */
  private void initializeUi() {
    root.setPadding(new Insets(16));
    root.setStyle("-fx-background-color: transparent;");

    HBox filterBox = createFilterBox();
    VBox feedScrollableArea = createFeedScrollableArea();

    root.getChildren().addAll(filterBox, feedScrollableArea);
    VBox.setVgrow(feedScrollableArea, Priority.ALWAYS);
  }

  /**
   * Creates the filter control box with week and sentiment dropdowns.
   *
   * @return HBox containing filter controls
   */
  private HBox createFilterBox() {
    HBox filterBox = new HBox(16);
    filterBox.setAlignment(Pos.CENTER_LEFT);
    filterBox.setPadding(new Insets(0, 0, 8, 0));

    Label filterLabel = new Label("Filter:");
    filterLabel.setFont(Font.font("System", FontWeight.MEDIUM, 12));
    filterLabel.setTextFill(Color.web("#cbd5e1"));

    setupWeekFilter();
    setupSentimentFilter();

    filterBox.getChildren().addAll(
        filterLabel,
        createLabeledControl("Week:", weekFilter),
        createLabeledControl("Sentiment:", sentimentFilter)
    );

    return filterBox;
  }

  /**
   * Creates a labeled control wrapper for a ComboBox.
   *
   * @param label the label text
   * @param control the ComboBox to wrap
   * @return HBox containing label and control
   */
  private HBox createLabeledControl(String label, ComboBox<String> control) {
    HBox wrapper = new HBox(8);
    wrapper.setAlignment(Pos.CENTER_LEFT);

    Label labelText = new Label(label);
    labelText.setFont(Font.font("System", 11));
    labelText.setTextFill(Color.web("#94a3b8"));

    wrapper.getChildren().addAll(labelText, control);
    return wrapper;
  }

  /**
   * Sets up the week filter dropdown with default and week options.
   */
  private void setupWeekFilter() {
    updateWeekFilterOptions(5);
    styleComboBox(weekFilter);
  }

  /**
   * Updates the week filter dropdown to include weeks up to the given max week.
   *
   * @param maxWeek the maximum week number to include in the filter
   */
  public void updateWeekFilterOptions(int maxWeek) {
    List<String> weeks = new ArrayList<>();
    weeks.add("All Weeks");
    for (int i = 1; i <= Math.max(5, maxWeek); i++) {
      weeks.add("Week " + i);
    }
    weekFilter.setItems(FXCollections.observableArrayList(weeks));
  }

  /**
   * Sets up the sentiment filter dropdown with sentiment options.
   */
  private void setupSentimentFilter() {
    sentimentFilter.setItems(FXCollections.observableArrayList(
        "All Sentiments", "Positive", "Negative", "Neutral"
    ));
    sentimentFilter.setValue("All Sentiments");
    styleComboBox(sentimentFilter);
  }

  /**
   * Applies consistent styling to a ComboBox.
   *
   * @param comboBox the ComboBox to style
   */
  private void styleComboBox(ComboBox<String> comboBox) {
    comboBox.setPrefWidth(120);
    comboBox.setStyle(
        "-fx-control-inner-background: rgba(15, 23, 42, 0.8); "
            + "-fx-text-fill: white; "
            + "-fx-padding: 6px 10px; "
            + "-fx-background-radius: 4; "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-border-radius: 4; "
            + "-fx-font-size: 11;");
  }

  /**
   * Creates the scrollable news feed container.
   *
   * @return VBox containing the news feed
   */
  private VBox createFeedScrollableArea() {
    VBox scrollContainer = new VBox(12);
    scrollContainer.setPadding(new Insets(8));
    scrollContainer.setStyle(
        "-fx-background-color: rgba(15, 23, 42, 0.3); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-background-radius: 8; "
            + "-fx-border-radius: 8;");

    newsFeedContainer.setStyle("-fx-spacing: 12;");

    noNewsLabel.setFont(Font.font("System", 12));
    noNewsLabel.setTextFill(Color.web("#64748b"));
    noNewsLabel.setAlignment(Pos.CENTER);
    VBox.setVgrow(noNewsLabel, Priority.ALWAYS);

    scrollContainer.getChildren().add(newsFeedContainer);
    return scrollContainer;
  }

  /**
   * Adds a news card to the feed with the given data.
   *
   * @param headline the news headline
   * @param content the news content
   * @param affectedStocks affected stocks or "Market-wide"
   * @param week the week number
   * @param sentiment sentiment type: "positive", "negative", or "neutral"
   */
  public void addNewsCard(String headline, String content, String affectedStocks, int week,
      String sentiment) {
    NewsCard card = new NewsCard(headline, content, affectedStocks, week, sentiment);
    newsFeedContainer.getChildren().add(card.getRoot());
    updateEmptyState();
  }

  /**
   * Adds multiple news cards at once.
   *
   * @param newsList list of news data, each as [headline, content, stocks, week, sentiment]
   */
  public void addMultipleNews(List<String[]> newsList) {
    for (String[] newsData : newsList) {
      if (newsData.length == 5) {
        addNewsCard(newsData[0], newsData[1], newsData[2],
            Integer.parseInt(newsData[3]), newsData[4]);
      }
    }
  }

  /**
   * Clears all news cards from the feed.
   */
  public void clearNews() {
    newsFeedContainer.getChildren().clear();
    updateEmptyState();
  }

  /**
   * Updates the empty state display when no news is available.
   */
  private void updateEmptyState() {
    if (newsFeedContainer.getChildren().isEmpty()) {
      if (!newsFeedContainer.getChildren().contains(noNewsLabel)) {
        newsFeedContainer.getChildren().add(noNewsLabel);
      }
    } else {
      newsFeedContainer.getChildren().remove(noNewsLabel);
    }
  }

  /**
   * Gets the selected week filter value.
   *
   * @return the selected week filter string
   */
  public String getWeekFilter() {
    return weekFilter.getValue();
  }

  /**
   * Gets the selected sentiment filter value.
   *
   * @return the selected sentiment filter string
   */
  public String getSentimentFilter() {
    return sentimentFilter.getValue();
  }

  /**
   * Gets the week filter ComboBox for adding listeners.
   *
   * @return the week filter ComboBox
   */
  public ComboBox<String> getWeekFilterControl() {
    return weekFilter;
  }

  /**
   * Gets the sentiment filter ComboBox for adding listeners.
   *
   * @return the sentiment filter ComboBox
   */
  public ComboBox<String> getSentimentFilterControl() {
    return sentimentFilter;
  }

  /**
   * Sets the week filter to a specific week without triggering listener.
   *
   * @param week the week number to filter by
   */
  public void setWeekFilterValue(int week) {
    if (week < 1) {
      weekFilter.setValue("All Weeks");
    } else {
      weekFilter.setValue("Week " + week);
    }
  }

  /**
   * Gets the root VBox container.
   *
   * @return the root VBox of this view
   */
  public VBox getRoot() {
    return root;
  }
}
