package edu.ntnu.idi.bidata.group5.ui.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * NewsCard displays a single news article in a styled card format.
 * Shows headline, brief content, affected stocks, week number, and sentiment indicator.
 */
public class NewsCard {

  private final VBox root;
  private final Label headlineLabel;
  private final Label contentLabel;
  private final Label metaLabel;
  private final Label sentimentBadge;

  /**
   * Constructs a NewsCard with given news data.
   *
   * @param headline the news headline text
   * @param content the news content/description
   * @param affectedStocks comma-separated stock symbols or "Market-wide"
   * @param week the week number this news is relevant for
   * @param sentiment sentiment type: "positive", "negative", or "neutral"
   */
  public NewsCard(String headline, String content, String affectedStocks, int week,
      String sentiment) {
    this.root = new VBox(8);
    this.headlineLabel = new Label(headline);
    this.contentLabel = new Label(content);
    this.metaLabel = new Label();
    this.sentimentBadge = new Label();

    initializeUi(affectedStocks, week, sentiment);
  }

  /**
   * Initializes the card UI with all styling and layout.
   *
   * @param affectedStocks stocks affected by this news
   * @param week week number
   * @param sentiment sentiment indicator
   */
  private void initializeUi(String affectedStocks, int week, String sentiment) {
    root.setPadding(new Insets(16));
    root.setStyle(
        "-fx-background-color: rgba(30, 41, 59, 0.6); "
            + "-fx-border-color: #334155; "
            + "-fx-border-width: 1; "
            + "-fx-background-radius: 8; "
            + "-fx-border-radius: 8;");

    headlineLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
    headlineLabel.setTextFill(Color.web("#ffffff"));
    headlineLabel.setWrapText(true);

    contentLabel.setFont(Font.font("System", 12));
    contentLabel.setTextFill(Color.web("#cbd5e1"));
    contentLabel.setWrapText(true);

    metaLabel.setText("Week " + week + " • " + affectedStocks);
    metaLabel.setFont(Font.font("System", 11));
    metaLabel.setTextFill(Color.web("#94a3b8"));

    setSentimentBadge(sentiment);

    HBox headerBox = new HBox(12);
    headerBox.setAlignment(Pos.CENTER_LEFT);
    headerBox.getChildren().addAll(headlineLabel, sentimentBadge);
    HBox.setHgrow(headlineLabel, Priority.ALWAYS);

    root.getChildren().addAll(headerBox, contentLabel, metaLabel);
  }

  /**
   * Sets the sentiment badge style and text based on sentiment type.
   *
   * @param sentiment the sentiment type
   */
  private void setSentimentBadge(String sentiment) {
    switch (sentiment.toLowerCase()) {
      case "positive":
        sentimentBadge.setText("↑");
        sentimentBadge.setTextFill(Color.web("#22c55e"));
        break;
      case "negative":
        sentimentBadge.setText("↓");
        sentimentBadge.setTextFill(Color.web("#ef4444"));
        break;
      case "neutral":
      default:
        sentimentBadge.setText("→");
        sentimentBadge.setTextFill(Color.web("#94a3b8"));
        break;
    }
    sentimentBadge.setFont(Font.font("System", FontWeight.BOLD, 18));
  }

  /**
   * Gets the root VBox container for this card.
   *
   * @return the root VBox
   */
  public VBox getRoot() {
    return root;
  }
}
