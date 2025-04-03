package chess.engine.GUI.Lichess.Events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LichessEvent {

    Type type;

    GameStatus game;

    Challenge challenge;


}

