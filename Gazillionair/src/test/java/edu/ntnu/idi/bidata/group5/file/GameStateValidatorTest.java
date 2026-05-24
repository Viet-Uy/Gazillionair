package edu.ntnu.idi.bidata.group5.file;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameStateValidatorTest {

  @Test
  void validateRejectsZeroStartingCapital() {
    GameStateData data = new GameStateData();
    data.playerName = "Uy";
    data.startingCapital = BigDecimal.ZERO;
    data.cashBalance = new BigDecimal("1000");
    data.currentWeek = 1;
    data.stocks = List.of();
    data.holdings = List.of();
    data.transactions = List.of();
    data.news = List.of();

    GameStateValidator validator = new GameStateValidator();

    assertThrows(IllegalArgumentException.class, () -> validator.validate(data));
  }
}
