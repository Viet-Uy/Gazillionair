# Gazillionair 📈

  **Group 5**
  
  **Students:** Viet-Uy Nguyen, Jørgen Urfjell Rørvik

  Gazillionair is a JavaFX stock market simulation game where the player builds a portfolio, reacts to weekly market changes, and tries to increase net worth over time. The
  game combines trading, portfolio management, weekly progression, transaction history, and market statistics in one graphical application.

  ## Table of Contents

  - [About](#about)
  - [Installation](#installation)
  - [How to Build and Run](#how-to-build-and-run)
  - [How to Use](#how-to-use)
  - [Trading Rules](#trading-rules)
  - [Own Extensions](#own-extensions)
  - [Player Status](#player-status)
  - [Stock Data File Format](#stock-data-file-format)
  - [Project Structure](#project-structure)
  - [Design Patterns Used](#design-patterns-used)

  ## About 🎮

  In Gazillionair, the player starts a new stock market session with a chosen name, a starting capital, and either uploaded stock data or bundled sample data. From there,
  the player can search for stocks, inspect prices and price history, buy and sell shares, review completed transactions, track market news, and advance the game week by
  week.

  The main goal is to grow net worth by making profitable decisions over time. Net worth is based on both available cash and the current value of owned shares.

  ## Installation ⚙️

  ### Requirements

  - Java 25
  - Maven

  ### Project Dependencies

  - JavaFX Controls 25.0.1
  - Jackson Databind 2.20.0
  - JUnit Jupiter 6.0.1

  ## How to Build and Run 🛠️

  ### Build

  ```bash
  mvn clean package

  ### Run Tests

  mvn test

  ### Run Full Verification

  mvn verify

  ### Start the Application

  mvn javafx:run

  ## How to Use 🚀

  > [!TIP]
  > For the best experience, we recommend playing Gazillionair in fullscreen, especially on smaller displays.

  1. Launch the application.
  2. Enter a player name.
  3. Enter a starting capital.
  4. Choose stock data:
      - upload a CSV file, or
      - use the bundled sample data
  5. Start the game to open the dashboard.
  6. Use the tabs to interact with the market:
      - Market: search stocks, inspect price changes, buy, sell, and view price graphs
      - News: inspect weekly news and sentiment impact
      - Portfolio: inspect owned shares and sell holdings
      - Transactions: filter and inspect completed transactions
      - Stats: inspect weekly gainers and losers
  7. Press Next Week to advance the market simulation.
  8. Save the game to JSON if you want to continue later.

  ## Trading Rules 💰

  ### Buying

  - The player can buy by quantity or by amount.
  - Fractional shares are supported.
  - A buy commission of 0.5% is added to the transaction.
  - A transaction preview is shown before the trade is confirmed.
  - A receipt is shown after the trade is completed.

  ### Selling

  - The player can sell specific quantities or all owned holdings.
  - Fractional shares are supported.
  - A sell commission of 1% is deducted from the transaction.
  - Tax is 30% on profit only.
  - Losses are not taxed.
  - A transaction preview is shown before the trade is confirmed.
  - A receipt is shown after the trade is completed.

  ## Own Extensions ✨

  The project includes several additions beyond the minimum trading loop:

  - Save and load functionality
      - active game sessions can be stored in JSON format
      - saved sessions can be loaded from the start screen
  - Graphical stock visualization
      - stocks can be viewed in a separate price chart popup
      - holdings in the portfolio also show graphical price history
  - Transaction filtering and detail view
      - transaction history can be filtered by type, week, and search query
      - detailed transaction information is shown for the selected transaction
  - Weekly market news
      - the game generates weekly news items that affect market prices

## Player Status 🏆

The player status is updated in real time and is based on trading progress and net worth growth.

Trading weeks only count when the player actively makes trades. Simply holding a stock over time does not increase the trading week count.

- Novice
    - starting status
    - no minimum requirement
- Investor
    - at least 10 active trading weeks
    - at least 20% net worth growth
- Speculator
    - at least 20 active trading weeks
    - at least 100% net worth growth

The dashboard also shows progress toward the next status level.

  ## Stock Data File Format 📄

  Stock data is loaded from CSV files.

  ### Supported format

  Each non-empty data line must follow this format:

  symbol,name,price

  ### Rules

  - lines starting with # are treated as comments
  - blank lines are ignored
  - the file must contain exactly three comma-separated values per stock row
  - price must be a positive decimal number

  ### Example

  # Top 500 US Stocks by Market Cap
  # Ticker,Name,Price
  AAPL,Apple Inc.,276.43
  MSFT,Microsoft,404.68
  NVDA,Nvidia,191.27

  ## Project Structure 🧭

  Stock_game/
  ├── README.md
  └── Gazillionair/
      ├── pom.xml
      ├── src/
      │   ├── main/
      │   │   ├── java/
      │   │   │   └── edu/ntnu/idi/bidata/group5/
      │   │   │       ├── calculator/
      │   │   │       ├── file/
      │   │   │       ├── main/
      │   │   │       ├── model/
      │   │   │       ├── service/
      │   │   │       └── ui/
      │   │   └── resources/
      │   │       ├── sp500.csv
      │   │       └── styles/
      │   └── test/
      │       └── java/
      └── target/

  ### Package Overview

  - calculator: transaction cost and tax calculations
  - file: reading and writing stock data and saved game state
  - main: application entry point
  - model: core domain classes such as player, stock, portfolio, transactions, and session
  - service: business services such as exchange, game engine, news generation, and factories
  - ui.controller: controller layer for the graphical interface
  - ui.view: JavaFX views
  - ui.view.components: reusable UI components and dialogs

  ## Design Patterns Used 🧠

  ### Factory

  The project uses a factory for transaction creation.

  - TransactionFactory creates Purchase and Sale objects
  - this centralizes transaction construction and keeps creation logic out of the UI

  ### Observer

  The project uses the observer pattern to keep the UI synchronized with the model.

  - GameSession acts as an observable model
  - DashboardView acts as an observer
  - when the model changes, observers are notified and the UI refreshes

  ### MVC

  The graphical interface is organized using MVC principles.

  - Model: domain classes such as GameSession, Player, Stock, Portfolio
  - View: JavaFX views such as StartView, DashboardView, MarketView
  - Controller: classes such as StartController, MarketController, TransactionsController

  This separation helps keep business logic out of the view layer and makes the code easier to test and maintain.
