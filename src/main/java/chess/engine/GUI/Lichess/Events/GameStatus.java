package chess.engine.GUI.Lichess.Events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

import static chess.engine.GUI.Lichess.Events.Type.GAME_STATUS;
import static chess.engine.GUI.Lichess.LichessUtility.STARTING_POSITION;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameStatus extends Event {

    Type type = GAME_STATUS;

    String fullId;

    String gameId;

    TimeControl timeControl;

    String initialFen;

    @JsonProperty("color")
    String myColor;

    @JsonProperty("status")
    chess.engine.GUI.Lichess.Events.Status status;

    String InitialFen;

    String[] moves;

    LichessPlayer whitePlayer;

    LichessPlayer blackPlayer;

    Boolean rated;

    String winner;

    Boolean drawOffer;

    public void update(GameUpdate gameUpdate) {
        Optional.ofNullable(gameUpdate.getInitialFen()).ifPresent(this::setInitialFen);
        Optional.ofNullable(gameUpdate.getTimeControl()).ifPresent(this::setTimeControl);
        Optional.ofNullable(gameUpdate.getWhitePlayer()).ifPresent(this::setWhitePlayer);
        Optional.ofNullable(gameUpdate.getBlackPlayer()).ifPresent(this::setBlackPlayer);
        if (gameUpdate.getGameState() != null) {
            Optional.ofNullable(gameUpdate.getGameState().getStatus()).ifPresent(this::setStatus);
            Optional.ofNullable(gameUpdate.getGameState().getMoves()).ifPresent(this::setMoves);
            this.setDrawOffer(gameUpdate.getGameState().getWhiteDrawOffer() || gameUpdate.getGameState().getBlackDrawOffer());
        }
    }

    public void setInitialFen(String initialFen) {
        this.initialFen = initialFen.equals("startpos") ? STARTING_POSITION : initialFen;
    }

    public void setMoves(String moves) {
        this.moves = moves.equals("") ? new String[0] : moves.split(" ");
    }

    public void setMoves(String[] moves) {
        this.moves = moves;
    }


    // Nested class to capture only "name" from "status"
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {
        @JsonProperty("name")  // Extract only "name"
        private String name;
    }
}
