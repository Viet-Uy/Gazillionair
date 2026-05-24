package edu.ntnu.idi.bidata.group5.model;

import static java.math.RoundingMode.HALF_UP;

import edu.ntnu.idi.bidata.group5.service.TransactionArchive;
import java.math.BigDecimal;

/**
 * Represents a player in the trading game.
 * A player has a name, a starting amount of money, a current balance,
 * a portfolio of owned shares, and an archive of completed transactions.
 */
public class Player {

  private static final int INVESTOR_MIN_TRADING_WEEKS = 10;
  private static final int SPECULATOR_MIN_TRADING_WEEKS = 20;
  private static final BigDecimal INVESTOR_MIN_GROWTH = BigDecimal.valueOf(1.2);
  private static final BigDecimal SPECULATOR_MIN_GROWTH = BigDecimal.valueOf(2.0);

  /** The name of the player. */
  private final String name;

  /** The initial amount of money the player starts with. */
  private final BigDecimal startingMoney;

  /** The player's current balance. */
  private BigDecimal money;

  /** The player's portfolio of owned shares. */
  private final Portfolio portfolio;

  /** Archive containing all completed transactions made by the player. */
  private final TransactionArchive transactionArchive;

  /**
   * Constructs a new {@code Player} with the given name and starting capital.
   *
   * @param name the name of the player
   * @param startingMoney the starting amount of money
   * @throws IllegalArgumentException if the name is {@code null} or blank,
   *     or if the starting money is {@code null} or negative
   */
  public Player(final String name, final BigDecimal startingMoney) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null or blank");
    }
    if (startingMoney == null) {
      throw new IllegalArgumentException("Starting money cannot be null");
    }
    if (startingMoney.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Starting money must be greater than zero");
    }
    this.name = name;
    this.startingMoney = startingMoney;
    this.money = startingMoney;
    this.portfolio = new Portfolio();
    this.transactionArchive = new TransactionArchive();
  }

  /**
   * Returns the name of the player.
   *
   * @return the player's name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the player's starting balance.
   *
   * @return the initial amount of money the player started with
   */
  public BigDecimal getStartingMoney() {
    return startingMoney;
  }

  /**
   * Returns the player's current balance.
   *
   * @return the current amount of money the player has
   */
  public BigDecimal getMoney() {
    return money;
  }

  /**
   * Adds money to the player's current balance.
   *
   * @param money the amount to add
   * @return the updated balance
   */
  public BigDecimal addMoney(BigDecimal money) {
    if (money == null || money.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Money to add cannot be null or negative");
    }
    this.money = this.money.add(money);
    return this.money;
  }

  /**
   * Withdraws money from the player's current balance.
   *
   * @param money the amount to withdraw
   * @return the updated balance
   */
  public BigDecimal withdrawMoney(BigDecimal money) {
    if (money == null || money.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Money to withdraw cannot be null or negative");
    }

    if (this.money.compareTo(money) < 0) {
      throw new IllegalStateException("Insufficient funds");
    }

    this.money = this.money.subtract(money);
    return this.money;
  }

  /**
   * Returns the player's portfolio.
   *
   * @return the portfolio of owned shares
   */
  public Portfolio getPortfolio() {
    return portfolio;
  }

  /**
   * Returns the player's transaction archive.
   *
   * @return the archive of completed transactions
   */
  public TransactionArchive getTransactionArchive() {
    return transactionArchive;
  }

  /**
   * Calculates and returns the player's net worth, which is the sum of their current balance
   * and the total value of their owned shares based on current stock prices.
   *
   * @return the player's net worth
   */
  public BigDecimal getNetWorth() {
    return money.add(portfolio.getNetWorth());
  }

  /**
   * Returns the number of distinct weeks in which the player has completed trades.
   *
   * @return the number of trading weeks recorded in the archive
   */
  public int getTradingWeeks() {
    return transactionArchive.countDistinctWeeks();
  }

  /**
   * Determines the player's status based on weeks with transactions and net worth growth.
   *
   * @return the player's status as a PlayerStatus enum value
   */
  public PlayerStatus getStatus() {
    int tradingWeeks = getTradingWeeks();
    BigDecimal growth = getGrowthRatio();
    if (tradingWeeks >= SPECULATOR_MIN_TRADING_WEEKS
        && growth.compareTo(SPECULATOR_MIN_GROWTH) >= 0) {
      return PlayerStatus.SPECULATOR;
    } else if (tradingWeeks >= INVESTOR_MIN_TRADING_WEEKS
        && growth.compareTo(INVESTOR_MIN_GROWTH) >= 0) {
      return PlayerStatus.INVESTOR;
    } else {
      return PlayerStatus.NOVICE;
    }
  }

  /**
   * Returns a short description of the player's progress toward the next status tier.
   *
   * @return the status progress description
   */
  public String getStatusProgressText() {
    int tradingWeeks = getTradingWeeks();
    String growthPercent = String.format("%.2f", getGrowthPercent());

    if (getStatus() == PlayerStatus.SPECULATOR) {
      return "Status Progress: Top rank reached | Trading Weeks: "
          + tradingWeeks
          + " | Growth: +"
          + growthPercent
          + "%";
    }

    if (getStatus() == PlayerStatus.INVESTOR) {
      return "Status Progress: "
          + tradingWeeks
          + "/"
          + SPECULATOR_MIN_TRADING_WEEKS
          + " trading weeks for SPECULATOR"
          + " | Growth: +"
          + growthPercent
          + "% / +100.00%";
    }

    return "Status Progress: "
        + tradingWeeks
        + "/"
        + INVESTOR_MIN_TRADING_WEEKS
        + " trading weeks for INVESTOR"
        + " | Growth: +"
        + growthPercent
        + "% / +20.00%";
  }

  private BigDecimal getGrowthRatio() {
    return getNetWorth().divide(startingMoney, 2, HALF_UP);
  }

  private BigDecimal getGrowthPercent() {
    return getNetWorth()
        .subtract(startingMoney)
        .divide(startingMoney, 4, HALF_UP)
        .multiply(BigDecimal.valueOf(100));
  }
}
