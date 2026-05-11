package edu.ntnu.idi.bidata.group5.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ntnu.idi.bidata.group5.model.GameSession;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Reads and writes full game state to JSON files.
 */
public class GameStateFileHandler {

  private final ObjectMapper objectMapper;
  private final GameStateMapper mapper;
  private final GameStateValidator validator;

  /**
   * Creates a handler for JSON game-state files.
   */
  public GameStateFileHandler() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    this.mapper = new GameStateMapper();
    this.validator = new GameStateValidator();
  }

  /**
   * Writes the current game state to a JSON file.
   *
   * @param session active game session
   * @param filePath path to output JSON file
   * @throws IOException if writing fails
   */
  public void writeToFile(GameSession session, String filePath) throws IOException {
    if (session == null) {
      throw new IllegalArgumentException("Session cannot be null");
    }
    if (filePath == null || filePath.isBlank()) {
      throw new IllegalArgumentException("File path cannot be null or blank");
    }

    GameStateData data = mapper.toData(session);
    objectMapper.writeValue(Path.of(filePath).toFile(), data);
  }

  /**
   * Reads game state from a JSON file.
   *
   * @param filePath path to input JSON file
   * @return restored game session
   * @throws IOException if reading or parsing fails
   */
  public GameSession readFromFile(String filePath) throws IOException {
    if (filePath == null || filePath.isBlank()) {
      throw new IllegalArgumentException("File path cannot be null or blank");
    }

    GameStateData data = objectMapper.readValue(Path.of(filePath).toFile(), GameStateData.class);
    validator.validate(data);
    return mapper.fromData(data);
  }
}
