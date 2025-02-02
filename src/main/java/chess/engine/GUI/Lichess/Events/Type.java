package chess.engine.GUI.Lichess.Events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Type {

    GAME_START("gameStart"),
    GAME_STATUS("gameStatus"),
    GAME_FULL("gameFull"),
    CHALLENGE("challenge"),
    CHAT_LINE("chatLine"),
    GAME_STATE("gameState"),
    GAME_FINISH("gameFinish"),
    UNKNOWN_TYPE("unknown");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Type fromValue(String value) {
        for (Type type : Type.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown key: " + value);
    }


}
