package chess.engine.GUI.Lichess.Events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Challenge {

    @JsonProperty("id")
    String gameId;

    Status status;

    LichessPlayer challenger;

    LichessPlayer destUser;

    Boolean rated;

}
