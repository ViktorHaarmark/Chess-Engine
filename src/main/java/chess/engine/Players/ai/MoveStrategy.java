package chess.engine.Players.ai;

import chess.engine.board.Board;
import chess.engine.board.ChessMove;

public interface MoveStrategy {
    ChessMove execute(Board board);
}
