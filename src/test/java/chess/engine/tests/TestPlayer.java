package chess.engine.tests;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.Board.Builder;
import chess.engine.board.BoardSetup;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.MoveFactory;
import chess.engine.pieces.Bishop;
import chess.engine.pieces.King;
import chess.engine.pieces.Rook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPlayer {


    @Test
    public void testSimpleEvaluation() {
        Board board = BoardSetup.createStandardBoard();
        TestUtility.executeAndAssert(board, "e2", "e4");
        TestUtility.executeAndAssert(board, "e7", "e5");

        //assertEquals(StandardBoardEvaluator.get().evaluate(board, 0), 0);
    }

    @Test
    public void testDiscoveredCheck() {
        Builder builder = new Builder();
        builder.setPiece(new King(4, Color.BLACK, false, false));
        builder.setPiece(new Rook(24, Color.BLACK, false));
        builder.setPiece(new Bishop(44, Color.WHITE, false));
        builder.setPiece(new Rook(52, Color.WHITE, false));
        builder.setPiece(new King(58, Color.WHITE, false, false));
        builder.setMoveMaker(Color.WHITE);

        Board board = builder.build();

        TestUtility.executeAndAssert(board, "e3", "b6");
        assertTrue(board.currentPlayer().isInCheck());
}
    @Test
    public void testTakeback() {
        Board board = BoardSetup.createStandardBoard();
        Move move = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("e4"));
        board.execute(move);
        board.takeback(move);
        assertEquals(BoardSetup.createStandardBoard(), board);
    }

    @Test
    public void testIllegalMove() {
        Board board = BoardSetup.createStandardBoard();
        TestUtility.executeAndAssert(board, "e2", "e6");
    }
}
