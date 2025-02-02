package chess.engine.GUI.Lichess.Events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static chess.engine.GUI.Lichess.LichessUtility.STARTING_POSITION;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameUpdate extends Event {

    @JsonProperty("id")
    String gameId;

    TimeControl timeControl;

    Boolean rated;

    @JsonProperty("white")
    LichessPlayer whitePlayer;

    @JsonProperty("black")
    LichessPlayer blackPlayer;

    String initialFen;

    @JsonProperty("state")
    GameState gameState;

    String text;

    public void setInitialFen(String initialFen) {
        this.initialFen = initialFen.equals("startpos") ? STARTING_POSITION : initialFen;
    }

    // Nested class to capture only "name" from "status"
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Variant {
        @JsonProperty("key")  // Extract only "name"
        private String variant;
    }

    public TimeControl getTimeControl() {
        if (this.getGameState() != null) {
            return new TimeControl(this.getGameState().getWhiteTime(),
                    this.getGameState().getBlackTime(),
                    this.getGameState().getWInc(),
                    this.getGameState().getBInc()
            );
        }
        return null;
    }

}
