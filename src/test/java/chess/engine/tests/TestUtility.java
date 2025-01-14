package chess.engine.tests;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtility {

    TestUtility() {
        throw new IllegalStateException("Utility class");
    }

    public static void executeAndAssert(Board board, String from, String to) {
        Move move = Move.MoveFactory.createMove(board,
                BoardUtils.getCoordinateAtPosition(from),
                BoardUtils.getCoordinateAtPosition(to));
        board.execute(move);
        assertTrue(move.getMoveStatus().isDone());
    }

    public static void executeAndAssertIllegal(Board board, String from, String to) {
        Move move = Move.MoveFactory.createMove(board,
                BoardUtils.getCoordinateAtPosition(from),
                BoardUtils.getCoordinateAtPosition(to));
        board.execute(move);
        assertFalse(move.getMoveStatus().isDone());
    }
}
