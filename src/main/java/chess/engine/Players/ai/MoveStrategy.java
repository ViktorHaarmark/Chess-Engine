package chess.engine.Players.ai;

import chess.engine.board.Board;
import chess.engine.board.Move;

public interface MoveStrategy {
    Move execute(Board board);
}
