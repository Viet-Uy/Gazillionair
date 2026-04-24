package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.News;
import edu.ntnu.idi.bidata.group5.ui.view.NewsView;
import java.util.ArrayList;
import java.util.List;

/**
 * NewsController handles the news view logic and interactions.
 * Manages filtering, sorting, and display of news articles.
 */
public class NewsController {

  private final NewsView view;
  private final GameSession session;
  private final List<News> allNews;

  /**
   * Constructs a NewsController with the given NewsView and GameSession.
   *
   * @param view the NewsView for displaying news
   * @param session the GameSession containing news data
   * @throws IllegalArgumentException if view or session is null
   */
  public NewsController(NewsView view, GameSession session) {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    this.view = view;
    this.session = session;
    this.allNews = new ArrayList<>();
    initializeBindings();
    loadNews(session.getAllNews());
    setDefaultWeekFilter();
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
   * Sets the week filter to the current game week.
   */
  private void setDefaultWeekFilter() {
    view.setWeekFilterValue(session.getCurrentWeek());
  }

  /**
   * Loads news articles into the controller and refreshes display.
   *
   * @param news list of news articles to display
   */
  public void loadNews(List<News> news) {
    if (news == null) {
      this.allNews.clear();
    } else {
      this.allNews.clear();
      this.allNews.addAll(news);
    }
    view.updateWeekFilterOptions(session.getCurrentWeek());
    setDefaultWeekFilter();
  }

  /**
   * Refreshes the news display for the current game week.
   * Called when advancing to a new week.
   */
  public void refreshForCurrentWeek() {
    view.updateWeekFilterOptions(session.getCurrentWeek());
    loadNews(session.getAllNews());
  }

  /**
   * Applies active filters to the news display.
   */
  private void applyFilters() {
    String weekFilter = view.getWeekFilter();
    String sentimentFilter = view.getSentimentFilter();

    List<News> filteredNews = filterNews(allNews, weekFilter, sentimentFilter);

    view.clearNews();
    for (News newsItem : filteredNews) {
      view.addNewsCard(
          newsItem.getHeadline(),
          newsItem.getContent(),
          newsItem.getAffectedStocks(),
          newsItem.getWeek(),
          newsItem.getSentimentAsString()
      );
    }
  }

  /**
   * Filters news based on week and sentiment criteria.
   *
   * @param news the news list to filter
   * @param weekFilter the week filter value
   * @param sentimentFilter the sentiment filter value
   * @return filtered news list
   */
  private List<News> filterNews(List<News> news, String weekFilter, String sentimentFilter) {
    List<News> result = new ArrayList<>(news);

    if (weekFilter != null && !weekFilter.equals("All Weeks")) {
      int selectedWeek = Integer.parseInt(weekFilter.replaceAll("\\D", ""));
      result.removeIf(n -> n.getWeek() != selectedWeek);
    }

    if (sentimentFilter != null && !sentimentFilter.equals("All Sentiments")) {
      News.Sentiment selectedSentiment = News.Sentiment.fromString(sentimentFilter);
      result.removeIf(n -> n.getSentiment() != selectedSentiment);
    }

    return result;
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
