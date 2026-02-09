package edu.ntnu.idi.bidata.group5;

import java.math.BigDecimal;

/**
 * Represents a player in the trading game.
 * A player has a name, a starting amount of money, a current balance,
 * a portfolio of owned shares, and an archive of completed transactions.
 */
public class Player {

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
     * or if the starting money is {@code null} or not positive
     */
    public Player(String name, BigDecimal startingMoney) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (startingMoney == null) {
            throw new IllegalArgumentException("Starting money cannot be null");
        }
        if (startingMoney.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Starting money cannot be less than zero");
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
}
