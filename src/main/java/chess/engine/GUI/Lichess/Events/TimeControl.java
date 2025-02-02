package chess.engine.GUI.Lichess.Events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeControl {
    int whiteTime;

    int blackTime;

    int wInc;

    int bInc;

    TimeControl(int whiteTime, int blackTime, int wInc, int bInc) {
        this.whiteTime = whiteTime;
        this.blackTime = blackTime;
        this.wInc = wInc;
        this.bInc = bInc;
    }

}
