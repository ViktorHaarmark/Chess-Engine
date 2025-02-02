package chess.engine.GUI.Lichess.Events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LichessPlayer {

    String id;

    String name;

    int rating;

    String title;

    Boolean provisional;

    Boolean online;
}
