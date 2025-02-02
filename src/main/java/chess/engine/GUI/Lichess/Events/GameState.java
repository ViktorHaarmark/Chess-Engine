package chess.engine.GUI.Lichess.Events;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameState extends Event {

    String[] moves;

    @JsonProperty("wtime")
    int whiteTime;

    @JsonProperty("btime")
    int blackTime;

    int wInc;

    int bInc;

    Status status;

    @JsonProperty("bdraw")
    Boolean blackDrawOffer = false;

    @JsonProperty("wdraw")
    Boolean whiteDrawOffer = false;

    @JsonIgnoreProperties(ignoreUnknown = true)
    String winner;

    public void setMoves(String moves) {
        this.moves = moves.isEmpty() ? new String[0] : moves.split(" ");
    }

}
