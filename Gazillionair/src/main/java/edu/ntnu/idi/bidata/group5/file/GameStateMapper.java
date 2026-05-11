package edu.ntnu.idi.bidata.group5.file;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.News;
import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Share;
import edu.ntnu.idi.bidata.group5.model.Stock;
import edu.ntnu.idi.bidata.group5.model.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maps game state between domain model and JSON DTO.
 */
public class GameStateMapper {

  /**
   * Maps a game session to persisted DTO structure.
   *
   * @param session active session
   * @return DTO payload
   */
  public GameStateData toData(GameSession session) {
    GameStateData data = new GameStateData();
    data.playerName = session.getPlayer().getName();
    data.startingCapital = session.getPlayer().getStartingMoney();
    data.cashBalance = session.getCashBalance();
    data.currentWeek = session.getCurrentWeek();
    data.stocks = new ArrayList<>();
    data.holdings = new ArrayList<>();
    data.transactions = new ArrayList<>();
    data.news = new ArrayList<>();

    for (Stock stock : session.getMarketStocks()) {
      GameStateData.StockData stockData = new GameStateData.StockData();
      stockData.symbol = stock.getSymbol();
      stockData.company = stock.getCompany();
      stockData.prices = stock.getHistoricalPrices();
      data.stocks.add(stockData);
    }

    for (Share share : session.getHoldings()) {
      GameStateData.ShareData shareData = new GameStateData.ShareData();
      shareData.symbol = share.getStock().getSymbol();
      shareData.quantity = share.getQuantity();
      shareData.purchasePrice = share.getPurchasePrice();
      data.holdings.add(shareData);
    }

    for (Transaction transaction : session.getTransactions()) {
      GameStateData.TransactionData transactionData = new GameStateData.TransactionData();
      transactionData.type = transaction instanceof Purchase ? "PURCHASE" : "SALE";
      transactionData.symbol = transaction.getShare().getStock().getSymbol();
      transactionData.quantity = transaction.getShare().getQuantity();
      transactionData.purchasePrice = transaction.getShare().getPurchasePrice();
      transactionData.week = transaction.getWeek();
      data.transactions.add(transactionData);
    }

    for (News newsItem : session.getAllNews()) {
      GameStateData.NewsData newsData = new GameStateData.NewsData();
      newsData.headline = newsItem.getHeadline();
      newsData.content = newsItem.getContent();
      newsData.affectedStocks = newsItem.getAffectedStocks();
      newsData.week = newsItem.getWeek();
      newsData.sentiment = newsItem.getSentimentAsString();
      data.news.add(newsData);
    }
    return data;
  }

  /**
   * Maps persisted DTO to a restored game session.
   *
   * @param data persisted DTO payload
   * @return restored session
   */
  public GameSession fromData(GameStateData data) {
    List<Stock> stocks = rebuildStocks(data.stocks);
    Map<String, Stock> stockBySymbol = stocksBySymbol(stocks);
    List<Share> holdings = mapHoldings(data.holdings, stockBySymbol);
    List<Transaction> transactions = mapTransactions(data.transactions, stockBySymbol);
    List<News> newsItems = mapNews(data.news);

    return GameSession.restoreSession(data.playerName, data.startingCapital, data.cashBalance,
        data.currentWeek, stocks, holdings, transactions, newsItems);
  }

  private List<Stock> rebuildStocks(List<GameStateData.StockData> stockDataList) {
    List<Stock> stocks = new ArrayList<>();
    for (GameStateData.StockData stockData : stockDataList) {
      Stock stock = new Stock(stockData.symbol, stockData.company, stockData.prices.getFirst());
      for (int i = 1; i < stockData.prices.size(); i++) {
        stock.addNewSalesPrice(stockData.prices.get(i));
      }
      stocks.add(stock);
    }
    return stocks;
  }

  private Map<String, Stock> stocksBySymbol(List<Stock> stocks) {
    Map<String, Stock> bySymbol = new HashMap<>();
    for (Stock stock : stocks) {
      bySymbol.put(stock.getSymbol(), stock);
    }
    return bySymbol;
  }

  private List<Share> mapHoldings(List<GameStateData.ShareData> holdingData,
                                  Map<String, Stock> stockBySymbol) {
    List<Share> holdings = new ArrayList<>();
    for (GameStateData.ShareData shareData : holdingData) {
      Stock stock = stockBySymbol.get(shareData.symbol);
      if (stock == null) {
        throw new IllegalArgumentException("Unknown holding stock symbol: " + shareData.symbol);
      }
      holdings.add(new Share(stock, shareData.quantity, shareData.purchasePrice));
    }
    return holdings;
  }

  private List<Transaction> mapTransactions(List<GameStateData.TransactionData> transactionDataList,
                                            Map<String, Stock> stockBySymbol) {
    List<Transaction> transactions = new ArrayList<>();
    for (GameStateData.TransactionData transactionData : transactionDataList) {
      Stock stock = stockBySymbol.get(transactionData.symbol);
      if (stock == null) {
        throw new IllegalArgumentException(
            "Unknown transaction stock symbol: " + transactionData.symbol);
      }
      Share share = new Share(stock, transactionData.quantity, transactionData.purchasePrice);
      Transaction transaction;
      if ("PURCHASE".equals(transactionData.type)) {
        transaction = new Purchase(share, transactionData.week);
      } else if ("SALE".equals(transactionData.type)) {
        transaction = new Sale(share, transactionData.week);
      } else {
        throw new IllegalArgumentException("Unknown transaction type: " + transactionData.type);
      }
      transaction.markCommittedForRestore();
      transactions.add(transaction);
    }
    return transactions;
  }

  private List<News> mapNews(List<GameStateData.NewsData> newsDataList) {
    List<News> newsItems = new ArrayList<>();
    for (GameStateData.NewsData newsData : newsDataList) {
      News newsItem = new News(newsData.headline, newsData.content, newsData.affectedStocks,
          newsData.week, News.Sentiment.fromString(newsData.sentiment));
      newsItems.add(newsItem);
    }
    return newsItems;
  }
}
