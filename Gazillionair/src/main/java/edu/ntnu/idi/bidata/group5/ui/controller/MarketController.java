package edu.ntnu.idi.bidata.group5.ui.controller;

import edu.ntnu.idi.bidata.group5.model.GameSession;
import edu.ntnu.idi.bidata.group5.model.Purchase;
import edu.ntnu.idi.bidata.group5.model.Sale;
import edu.ntnu.idi.bidata.group5.model.Share;
import edu.ntnu.idi.bidata.group5.model.Stock;
import edu.ntnu.idi.bidata.group5.ui.view.MarketView;
import edu.ntnu.idi.bidata.group5.ui.view.components.ReceiptDialog;
import edu.ntnu.idi.bidata.group5.ui.view.components.TradePreviewDialog;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MarketController is responsible for handling market operations such as
 * searching for stocks, and executing buy/sell transactions.
 * It connects the MarketView (UI) with the GameSession (business logic).
 */
public class MarketController {

  private static final BigDecimal PURCHASE_FEE_MULTIPLIER = new BigDecimal("1.005");
  private final GameSession session;
  private final MarketView view;
  private Consumer<Stock> onStockSelected;

  /**
   * Constructs a MarketController with the given GameSession and MarketView.
   *
   * @param session the GameSession containing market data
   * @param view the MarketView for UI interaction
   * @throws IllegalArgumentException if session or view is null
   */
  public MarketController(GameSession session, MarketView view) {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.session = session;
    this.view = view;
    initializeBindings();
  }

  /**
   * Initializes event bindings between view and controller.
   */
  private void initializeBindings() {
    view.getSearchInput()
        .textProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              List<Stock> results = search(newVal);
              view.updateStocks(results);
            });

    view.setOnRowSelected(
        stock -> {
          if (onStockSelected != null) {
            onStockSelected.accept(stock);
          }
        });
    view.setOnBuyRequested(this::handleBuyRequest);
    view.setOnSellRequested(this::handleSellRequest);
    view.setOnBuyMaxRequested(this::handleBuyMaxRequest);

    List<Stock> allStocks = session.getMarketStocks();
    view.updateStocks(allStocks);
  }

  /**
   * Searches for stocks based on symbol or company name.
   *
   * @param query the search query string
   * @return list of matching stocks (empty if none found)
   */
  public List<Stock> search(String query) {
    return session.searchStocks(query);
  }

  /**
   * Executes a buy transaction.
   *
   * @param symbol the stock symbol
   * @param quantity the number of shares to buy
   * @return Purchase containing transaction details
   */
  public Purchase buy(String symbol, int quantity) {
    return session.buy(symbol, quantity);
  }

  /**
   * Executes a buy transaction.
   *
   * @param symbol the stock symbol
   * @param quantity the number of shares to buy
   * @return Purchase containing transaction details
   */
  public Purchase buy(String symbol, BigDecimal quantity) {
    return session.buy(symbol, quantity);
  }

  /**
   * Executes a sell transaction.
   *
   * @param symbol the stock symbol
   * @param quantity the number of shares to sell
   * @return Sale containing transaction details
   */
  public Sale sell(String symbol, int quantity) {
    return session.sell(symbol, quantity);
  }

  /**
   * Executes a sell transaction.
   *
   * @param symbol the stock symbol
   * @param quantity the number of shares to sell
   * @return Sale containing transaction details
   */
  public Sale sell(String symbol, BigDecimal quantity) {
    return session.sell(symbol, quantity);
  }

  /**
   * Executes a buy transaction by amount and converts amount to fractional quantity.
   *
   * @param symbol stock symbol
   * @param amount amount in currency to spend
   * @return committed purchase
   */
  public Purchase buyForAmount(String symbol, BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be greater than zero");
    }
    Stock stock = session.getStock(symbol);
    BigDecimal quantity =
        amount.divide(stock.getSalesPrice(), 6, RoundingMode.HALF_UP);
    return buy(symbol, quantity);
  }

  /**
   * Executes a buy transaction for the maximum affordable quantity.
   *
   * @param symbol stock symbol
   * @return committed purchase
   */
  public Purchase buyMax(String symbol) {
    Stock stock = session.getStock(symbol);
    BigDecimal denominator = stock.getSalesPrice().multiply(PURCHASE_FEE_MULTIPLIER);
    BigDecimal quantity = session.getCashBalance().divide(denominator, 6, RoundingMode.DOWN);
    if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalStateException("Not enough cash to buy this stock");
    }
    return buy(symbol, quantity);
  }

  private void handleBuyRequest(Stock stock, BigDecimal quantity) {
    try {
      Purchase previewPurchase = createPurchasePreview(stock, quantity);
      if (!TradePreviewDialog.confirmPurchase(getOwnerStage(), previewPurchase)) {
        view.showTradeError("Purchase cancelled.");
        return;
      }
      Purchase purchase = buy(stock.getSymbol(), quantity);
      ReceiptDialog.showTransaction(getOwnerStage(), purchase);
      view.showTradeSuccess(
          "Bought "
              + purchase.getShare().getQuantity().stripTrailingZeros().toPlainString()
              + " "
              + stock.getSymbol());
    } catch (IllegalArgumentException | IllegalStateException exception) {
      view.showTradeError(exception.getMessage());
    }
  }

  private void handleSellRequest(Stock stock, BigDecimal quantity) {
    try {
      Sale previewSale = createSalePreview(stock, quantity);
      if (!TradePreviewDialog.confirmSale(getOwnerStage(), previewSale)) {
        view.showTradeError("Sale cancelled.");
        return;
      }
      Sale sale = sell(stock.getSymbol(), quantity);
      ReceiptDialog.showTransaction(getOwnerStage(), sale);
      view.showTradeSuccess(formatSaleMessage(sale, stock.getSymbol()));
    } catch (IllegalArgumentException | IllegalStateException exception) {
      view.showTradeError(exception.getMessage());
    }
  }

  private void handleBuyMaxRequest(Stock stock) {
    try {
      Purchase previewPurchase = createPurchasePreview(stock, resolveMaxQuantity(stock));
      if (!TradePreviewDialog.confirmPurchase(getOwnerStage(), previewPurchase)) {
        view.showTradeError("Purchase cancelled.");
        return;
      }
      Purchase purchase = buyMax(stock.getSymbol());
      ReceiptDialog.showTransaction(getOwnerStage(), purchase);
      view.showTradeSuccess(
          "Bought "
              + purchase.getShare().getQuantity().stripTrailingZeros().toPlainString()
              + " "
              + stock.getSymbol()
              + " (max)");
    } catch (IllegalArgumentException | IllegalStateException exception) {
      view.showTradeError(exception.getMessage());
    }
  }

  private String formatSaleMessage(Sale sale, String symbol) {
    BigDecimal gross = sale.getCalculator().calculateGross();
    BigDecimal commission = sale.getCalculator().calculateCommission();
    BigDecimal tax = sale.getCalculator().calculateTax();
    BigDecimal net = sale.getCalculator().calculateTotal();
    return "Sold "
        + sale.getShare().getQuantity().stripTrailingZeros().toPlainString()
        + " "
        + symbol
        + " | Gross "
        + formatMoney(gross)
        + ", Fee "
        + formatMoney(commission)
        + ", Tax "
        + formatMoney(tax)
        + ", Net "
        + formatMoney(net);
  }

  private String formatMoney(BigDecimal amount) {
    return "$" + amount.setScale(2, RoundingMode.HALF_UP);
  }

  private Purchase createPurchasePreview(Stock stock, BigDecimal quantity) {
    Share share = new Share(stock, quantity, stock.getSalesPrice());
    return new Purchase(share, session.getCurrentWeek());
  }

  private Sale createSalePreview(Stock stock, BigDecimal quantity) {
    List<Share> ownedShares = session.getPlayer().getPortfolio().getShares(stock.getSymbol());
    if (ownedShares.isEmpty()) {
      throw new IllegalStateException("No owned share has enough quantity to sell");
    }
    BigDecimal totalOwnedQuantity = ownedShares.stream()
        .map(Share::getQuantity)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    if (totalOwnedQuantity.compareTo(quantity) < 0) {
      throw new IllegalStateException("No owned share has enough quantity to sell");
    }
    BigDecimal weightedPurchasePrice = calculateWeightedPurchasePrice(ownedShares, quantity);
    Share shareToSell = new Share(stock, quantity, weightedPurchasePrice);
    return new Sale(shareToSell, session.getCurrentWeek());
  }

  private BigDecimal calculateWeightedPurchasePrice(List<Share> ownedShares,
                                                    BigDecimal targetQuantity) {
    BigDecimal remaining = targetQuantity;
    BigDecimal totalCost = BigDecimal.ZERO;
    for (Share share : ownedShares) {
      if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
        break;
      }
      BigDecimal consumedQuantity = share.getQuantity().min(remaining);
      totalCost = totalCost.add(consumedQuantity.multiply(share.getPurchasePrice()));
      remaining = remaining.subtract(consumedQuantity);
    }
    return totalCost.divide(targetQuantity, 6, RoundingMode.HALF_UP);
  }

  private BigDecimal resolveMaxQuantity(Stock stock) {
    BigDecimal denominator = stock.getSalesPrice().multiply(PURCHASE_FEE_MULTIPLIER);
    BigDecimal quantity = session.getCashBalance().divide(denominator, 6, RoundingMode.DOWN);
    if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalStateException("Not enough cash to buy this stock");
    }
    return quantity;
  }

  private Stage getOwnerStage() {
    Scene scene = view.getRoot().getScene();
    if (scene != null && scene.getWindow() instanceof Stage stage) {
      return stage;
    }
    return null;
  }

  /**
   * Sets the callback when a stock row is selected.
   *
   * @param callback the callback function to invoke with selected stock
   */
  public void setOnStockSelected(Consumer<Stock> callback) {
    this.onStockSelected = callback;
  }

  /**
   * Refreshes the stock table with current market data.
   */
  public void refreshStockTable() {
    List<Stock> currentStocks = session.getMarketStocks();
    view.updateStocks(currentStocks);
    view.refreshSelectedStock();
  }

  /**
   * Gets the associated MarketView.
   *
   * @return the MarketView
   */
  public MarketView getView() {
    return view;
  }
}
