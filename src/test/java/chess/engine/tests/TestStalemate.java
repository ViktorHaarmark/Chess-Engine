package chess.engine.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.Board.Builder;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move.MoveFactory;
import chess.engine.pieces.Bishop;
import chess.engine.pieces.King;
import chess.engine.pieces.Pawn;

public class TestStalemate {
    @Test
    public void testAnandKramnikStaleMate() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Pawn(14, Color.BLACK, false));
        builder.setPiece(new Pawn(21, Color.BLACK, false));
        builder.setPiece(new King(36, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Pawn(29, Color.WHITE, false));
        builder.setPiece(new King(31, Color.WHITE, false, false));
        builder.setPiece(new Pawn(39, Color.WHITE, false));
        // Set the current player
        builder.setMoveMaker(Color.BLACK);
        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStaleMate());
        TestUtility.executeAndAssert(board, "e4", "f5");

        assertTrue(board.currentPlayer().isInStaleMate());
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
    }

    @Test
    public void testAnonymousStaleMate() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(2, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Pawn(10, Color.WHITE, false));
        builder.setPiece(new King(26, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);
        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStaleMate());
        TestUtility.executeAndAssert(board, "c5", "c6");

        assertTrue(board.currentPlayer().isInStaleMate());
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
    }

    @Test
    public void testAnonymousStaleMate2() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(0, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Pawn(16, Color.WHITE, false));
        builder.setPiece(new King(17, Color.WHITE, false, false));
        builder.setPiece(new Bishop(19, Color.WHITE, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);
        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStaleMate());
        TestUtility.executeAndAssert(board, "a6", "a7");

        assertTrue(board.currentPlayer().isInStaleMate());
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
    }
}