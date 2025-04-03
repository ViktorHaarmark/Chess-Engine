package chess.engine.GUI.Lichess.Events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.net.URI;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Challenge {

    @JsonProperty("id")
    String gameId;

    URI url;

    String status;

    LichessPlayer challenger;

    LichessPlayer destUser;

    Variant variant;

    Boolean rated;

    String speed;

    TimeControl timeControl;

    String color;

    String finalColor;

    Perf perf;

    String direction;

    String declineReason;

    String declineReasonKey;

}
