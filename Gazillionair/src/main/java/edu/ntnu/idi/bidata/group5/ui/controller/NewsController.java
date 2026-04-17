package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.ui.view.NewsView;

/**
 * NewsController handles the news view logic and interactions.
 * Manages filtering, sorting, and display of news articles.
 */
public class NewsController {

  private final NewsView view;

  /**
   * Constructs a NewsController with the given NewsView.
   *
   * @param view the NewsView for displaying news
   * @throws IllegalArgumentException if view is null
   */
  public NewsController(NewsView view) {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
    initializeBindings();
  }

  /**
   * Initializes event bindings between view controls and controller logic.
   */
  private void initializeBindings() {
    view.getWeekFilterControl().valueProperty()
        .addListener((obs, oldVal, newVal) -> applyFilters());

    view.getSentimentFilterControl().valueProperty()
        .addListener((obs, oldVal, newVal) -> applyFilters());
  }

  /**
   * Applies active filters to the news display.
   * Currently, a placeholder for future filtering logic.
   */
  private void applyFilters() {
  }

  /**
   * Gets the associated NewsView.
   *
   * @return the NewsView
   */
  public NewsView getView() {
    return view;
  }
}
