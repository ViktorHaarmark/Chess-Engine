package chess.engine.tests;

import chess.engine.Players.ai.AlphaBeta;
import chess.engine.Players.ai.MoveStrategy;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.pgn.FenUtility;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAlphaBetaSearch {


    @Test
    public void testMateInTwoTest1() {
        final Board board = FenUtility.createGameFromFEN("6k1/1b4pp/1B1Q4/4p1P1/p3q3/2P3r1/P1P2PP1/R5K1 w - - 1 0");
        final MoveStrategy engine = new AlphaBeta(4);
        final Move bestMove = engine.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d6"), BoardUtils.getCoordinateAtPosition("e6")));
    }
}
