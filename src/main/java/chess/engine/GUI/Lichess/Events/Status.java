package chess.engine.GUI.Lichess.Events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {

    STARTED("started"),
    CREATED("created"),
    RESIGN("resign"),
    UNKNOWN_STATUS("");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static Status fromName(String name) {
        for (Status status : Status.values()) {
            if (status.name.equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown key: " + name);
    }


}
