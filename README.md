 # Gazillionair 📈💸

 Gazillionair is a JavaFX stock market simulation game made by group 5. 
 
 In this game you manage a portfolio, react to weekly market changes, and aim to grow your net worth over time.

 ## Requirements ✅
 - Java 25 (LTS)
 - Maven

 Minimum required versions used in the project:
 - Plugins:
   - maven-compiler-plugin 3.14.1
   - maven-surefire-plugin 3.5.4
   - javafx-maven-plugin 0.0.8
   - maven-javadoc-plugin 3.12.0
 - Dependencies:
   - javafx-controls 25.0.1
   - junit-jupiter 6.0.1

 ## Build 🧱
 ```bash
 mvn clean package

Run ▶️

 mvn javafx:run

How to Play 🎮

 1. Enter a player name and starting capital.
 2. Choose stock data:
 - Upload a CSV file, or
 - Use the bundled sample data (sp500.csv, 506 stocks).
 3. Start the game to open the dashboard.

Gameplay Highlights ✨

 - Market: Search stocks and buy/sell (fractional shares supported). Includes Buy Max.
 - Portfolio: View holdings and sell specific amounts or all holdings.
 - News: Weekly news affects market prices and can be filtered by week/sentiment.
 - Stats: See top weekly gainers/losers.
 - Transactions: Full history of purchases and sales.
 - Time: Click Next Week to advance the market simulation.
 - Save/Load: Save game state to JSON and load it later from the start screen.

Trading Rules (Simplified) 💰

 - Buy commission: 0.5%
 - Sell commission: 1%
 - Tax: 30% on profits only (no tax on losses)

Project Structure 🧭

 - src/main/java – application and game logic
 - src/main/resources – styles and sample stock data
 - src/test/java – unit tests
