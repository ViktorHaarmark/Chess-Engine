package chess.engine.GUI.Lichess;

import chess.engine.GUI.Lichess.Events.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static chess.engine.GUI.Lichess.Events.Status.*;

import static chess.engine.GUI.Lichess.Events.Type.*;

public class Mapper {

    public static LichessEvent jsonToLichessEvent(String json) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, LichessEvent.class);
    }

    public static GameUpdate jsonToGameUpdate(String gameId, String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);
        GameUpdate gameUpdate = new GameUpdate();
        if (rootNode.has("state")) {
            gameUpdate = objectMapper.readValue(json, GameUpdate.class);
        } else {
            gameUpdate.setGameState(objectMapper.readValue(json, GameState.class));
        }
        gameUpdate.setGameId(gameId);

        return gameUpdate;


    }


    private static String[] stringToMoves(String moves) {
        return moves.trim().split(" ");
    }

    private static Type stringToType(String type) {
        return switch (type) {
            case "gameStart" -> GAME_START;
            case "gameFull" -> GAME_FULL;
            default -> UNKNOWN_TYPE;
        };
    }

    private static Status stringToStatus(String status) {
        return switch (status) {
            case "started" -> STARTED;
            default -> UNKNOWN_STATUS;
        };
    }
}
