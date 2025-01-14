package chess.engine.tests;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.Board.Builder;
import chess.engine.board.BoardSetup;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.MoveFactory;
import chess.engine.pieces.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCheckMate {

    @Test
    public void testFoolsMate() {

        final Board board = BoardSetup.createStandardBoard();

        final Move m1 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"),
                BoardUtils.getCoordinateAtPosition("f3"));
        board.execute(m1);
        assertTrue(m1.getMoveStatus().isDone()); // Assuming Move has getMoveStatus()

        final Move m2 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e7"),
                BoardUtils.getCoordinateAtPosition("e5"));
        board.execute(m2);

        assertTrue(m2.getMoveStatus().isDone());

        final Move m3 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("g2"),
                BoardUtils.getCoordinateAtPosition("g4"));
        board.execute(m3);

        assertTrue(m3.getMoveStatus().isDone());

        final Move m4 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d8"),
                BoardUtils.getCoordinateAtPosition("h4"));
        board.execute(m4);

        assertTrue(m4.getMoveStatus().isDone());

        assertTrue(board.currentPlayer().isInCheckMate());
    }


    @Test
    public void testScholarsMate() {

        final Board board = BoardSetup.createStandardBoard();

        TestUtility.executeAndAssert(board, "e2", "e4");
        TestUtility.executeAndAssert(board, "a7", "a6");
        TestUtility.executeAndAssert(board, "d1", "f3");
        TestUtility.executeAndAssert(board, "a6", "a5");
        TestUtility.executeAndAssert(board, "f1", "c4");
        TestUtility.executeAndAssert(board, "a5", "a4");
        TestUtility.executeAndAssert(board, "f3", "f7");

        assertTrue(board.currentPlayer().isInCheckMate());

    }

    @Test
    public void testLegalsMate() {

        final Board board = BoardSetup.createStandardBoard();
        // Execute the first move: "e2" -> "e4"
        final Move m1 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                BoardUtils.getCoordinateAtPosition("e4"));
        board.execute(m1);

        assertTrue(m1.getMoveStatus().isDone()); // Assuming Move has getMoveStatus()

// Execute the second move: "e7" -> "e5"
        final Move m2 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e7"),
                BoardUtils.getCoordinateAtPosition("e5"));
        board.execute(m2);

        assertTrue(m2.getMoveStatus().isDone());

// Execute the third move: "f1" -> "c4"
        final Move m3 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f1"),
                BoardUtils.getCoordinateAtPosition("c4"));
        board.execute(m3);

        assertTrue(m3.getMoveStatus().isDone());

// Execute the fourth move: "d7" -> "d6"
        final Move m4 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d7"),
                BoardUtils.getCoordinateAtPosition("d6"));
        board.execute(m4);

        assertTrue(m4.getMoveStatus().isDone());

// Execute the fifth move: "g1" -> "f3"
        final Move m5 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("g1"),
                BoardUtils.getCoordinateAtPosition("f3"));
        board.execute(m5);

        assertTrue(m5.getMoveStatus().isDone());

// Execute the sixth move: "c8" -> "g4"
        final Move m6 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("c8"),
                BoardUtils.getCoordinateAtPosition("g4"));
        board.execute(m6);

        assertTrue(m6.getMoveStatus().isDone());

// Execute the seventh move: "b1" -> "c3"
        final Move m7 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("b1"),
                BoardUtils.getCoordinateAtPosition("c3"));
        board.execute(m7);

        assertTrue(m7.getMoveStatus().isDone());

// Execute the eighth move: "g7" -> "g6"
        final Move m8 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("g7"),
                BoardUtils.getCoordinateAtPosition("g6"));
        board.execute(m8);

        assertTrue(m8.getMoveStatus().isDone());

// Execute the ninth move: "f3" -> "e5"
        final Move m9 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f3"),
                BoardUtils.getCoordinateAtPosition("e5"));
        board.execute(m9);

        assertTrue(m9.getMoveStatus().isDone());

    }

    @Test
    public void testSevenMoveMate() {

        final Board board = BoardSetup.createStandardBoard();
        // Execute the first move: "e2" -> "e4"
        final Move m1 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                BoardUtils.getCoordinateAtPosition("e4"));
        board.execute(m1);
        assertTrue(m1.getMoveStatus().isDone()); // Validate move status

// Execute the second move: "e7" -> "e5"
        final Move m2 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e7"),
                BoardUtils.getCoordinateAtPosition("e5"));
        board.execute(m2);
        assertTrue(m2.getMoveStatus().isDone());

// Execute the third move: "d2" -> "d4"
        final Move m3 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d2"),
                BoardUtils.getCoordinateAtPosition("d4"));
        board.execute(m3);
        assertTrue(m3.getMoveStatus().isDone());

// Execute the fourth move: "d7" -> "d6"
        final Move m4 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d7"),
                BoardUtils.getCoordinateAtPosition("d6"));
        board.execute(m4);
        assertTrue(m4.getMoveStatus().isDone());

// Execute the fifth move: "d4" -> "e5"
        final Move m5 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d4"),
                BoardUtils.getCoordinateAtPosition("e5"));
        board.execute(m5);
        assertTrue(m5.getMoveStatus().isDone());

// Execute the sixth move: "d8" -> "e7"
        final Move m6 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d8"),
                BoardUtils.getCoordinateAtPosition("e7"));
        board.execute(m6);
        assertTrue(m6.getMoveStatus().isDone());

// Execute the seventh move: "e5" -> "d6"
        final Move m7 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e5"),
                BoardUtils.getCoordinateAtPosition("d6"));
        board.execute(m7);
        assertTrue(m7.getMoveStatus().isDone());

// Execute the eighth move: "e7" -> "e4"
        final Move m8 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e7"),
                BoardUtils.getCoordinateAtPosition("e4"));
        board.execute(m8);
        assertTrue(m8.getMoveStatus().isDone());

// Execute the ninth move: "f1" -> "e2"
        final Move m9 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f1"),
                BoardUtils.getCoordinateAtPosition("e2"));
        board.execute(m9);
        assertTrue(m9.getMoveStatus().isDone());

// Execute the tenth move: "e4" -> "g2"
        final Move m10 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e4"),
                BoardUtils.getCoordinateAtPosition("g2"));
        board.execute(m10);
        assertTrue(m10.getMoveStatus().isDone());

// Execute the eleventh move: "d6" -> "c7"
        final Move m11 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d6"),
                BoardUtils.getCoordinateAtPosition("c7"));
        board.execute(m11);
        assertTrue(m11.getMoveStatus().isDone());

// Execute the twelfth move: "g2" -> "h1"
        final Move m12 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("g2"),
                BoardUtils.getCoordinateAtPosition("h1"));
        board.execute(m12);
        assertTrue(m12.getMoveStatus().isDone());

// Execute the thirteenth move: "d1" -> "d8"
        final Move m13 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d1"),
                BoardUtils.getCoordinateAtPosition("d8"));
        board.execute(m13);
        assertTrue(m13.getMoveStatus().isDone());

// Validate checkmate condition
        assertTrue(board.currentPlayer().isInCheckMate());

    }

    @Test
    public void testGrecoGame() {

        final Board board = BoardSetup.createStandardBoard();
        // Execute the first move: "d2" -> "d4"
        final Move m1 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d2"),
                BoardUtils.getCoordinateAtPosition("d4"));
        board.execute(m1);
        assertTrue(m1.getMoveStatus().isDone()); // Ensure the move was successfully made

// Execute the second move: "g8" -> "f6"
        final Move m2 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("g8"),
                BoardUtils.getCoordinateAtPosition("f6"));
        board.execute(m2);
        assertTrue(m2.getMoveStatus().isDone());

// Execute the third move: "b1" -> "d2"
        final Move m3 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("b1"),
                BoardUtils.getCoordinateAtPosition("d2"));
        board.execute(m3);
        assertTrue(m3.getMoveStatus().isDone());

// Execute the fourth move: "e7" -> "e5"
        final Move m4 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e7"),
                BoardUtils.getCoordinateAtPosition("e5"));
        board.execute(m4);
        assertTrue(m4.getMoveStatus().isDone());

// Execute the fifth move: "d4" -> "e5"
        final Move m5 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d4"),
                BoardUtils.getCoordinateAtPosition("e5"));
        board.execute(m5);
        assertTrue(m5.getMoveStatus().isDone());

// Execute the sixth move: "f6" -> "g4"
        final Move m6 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f6"),
                BoardUtils.getCoordinateAtPosition("g4"));
        board.execute(m6);
        assertTrue(m6.getMoveStatus().isDone());

// Execute the seventh move: "h2" -> "h3"
        final Move m7 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("h2"),
                BoardUtils.getCoordinateAtPosition("h3"));
        board.execute(m7);
        assertTrue(m7.getMoveStatus().isDone());

// Execute the eighth move: "g4" -> "e3"
        final Move m8 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("g4"),
                BoardUtils.getCoordinateAtPosition("e3"));
        board.execute(m8);
        assertTrue(m8.getMoveStatus().isDone());

// Execute the ninth move: "f2" -> "e3"
        final Move m9 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"),
                BoardUtils.getCoordinateAtPosition("e3"));
        board.execute(m9);
        assertTrue(m9.getMoveStatus().isDone());

// Execute the tenth move: "d8" -> "h4"
        final Move m10 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d8"),
                BoardUtils.getCoordinateAtPosition("h4"));
        board.execute(m10);
        assertTrue(m10.getMoveStatus().isDone());

// Execute the eleventh move: "g2" -> "g3"
        final Move m11 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("g2"),
                BoardUtils.getCoordinateAtPosition("g3"));
        board.execute(m11);
        assertTrue(m11.getMoveStatus().isDone());

// Execute the twelfth move: "h4" -> "g3"
        final Move m12 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("h4"),
                BoardUtils.getCoordinateAtPosition("g3"));
        board.execute(m12);
        assertTrue(m12.getMoveStatus().isDone());

// Validate checkmate condition
        assertTrue(board.currentPlayer().isInCheckMate());

    }


    @Test
    public void testOlympicGame() {
        Board board = BoardSetup.createStandardBoard();

        TestUtility.executeAndAssert(board, "e2", "e4");
        TestUtility.executeAndAssert(board, "c7", "c6");
        TestUtility.executeAndAssert(board, "g1", "f3");
        TestUtility.executeAndAssert(board, "d7", "d5");
        TestUtility.executeAndAssert(board, "b1", "c3");
        TestUtility.executeAndAssert(board, "d5", "e4");
        TestUtility.executeAndAssert(board, "c3", "e4");
        TestUtility.executeAndAssert(board, "b8", "d7");
        TestUtility.executeAndAssert(board, "d1", "e2");
        TestUtility.executeAndAssert(board, "g8", "f6");
        TestUtility.executeAndAssert(board, "e4", "d6");

        assertTrue(board.currentPlayer().isInCheckMate());
    }

    @Test
    public void testAnotherGame() {
        Board board = BoardSetup.createStandardBoard();

        TestUtility.executeAndAssert(board, "e2", "e4");
        TestUtility.executeAndAssert(board, "e7", "e5");
        TestUtility.executeAndAssert(board, "g1", "f3");
        TestUtility.executeAndAssert(board, "b8", "c6");
        TestUtility.executeAndAssert(board, "f1", "c4");
        TestUtility.executeAndAssert(board, "c6", "d4");
        TestUtility.executeAndAssert(board, "f3", "e5");
        TestUtility.executeAndAssert(board, "d8", "g5");
        TestUtility.executeAndAssert(board, "e5", "f7");
        TestUtility.executeAndAssert(board, "g5", "g2");
        TestUtility.executeAndAssert(board, "h1", "f1");
        TestUtility.executeAndAssert(board, "g2", "e4");
        TestUtility.executeAndAssert(board, "c4", "e2");
        TestUtility.executeAndAssert(board, "d4", "f3");

        assertTrue(board.currentPlayer().isInCheckMate());
    }

    @Test
    public void testSmotheredMate() {
        Board board = BoardSetup.createStandardBoard();

        TestUtility.executeAndAssert(board, "e2", "e4");
        TestUtility.executeAndAssert(board, "e7", "e5");
        TestUtility.executeAndAssert(board, "g1", "e2");
        TestUtility.executeAndAssert(board, "b8", "c6");
        TestUtility.executeAndAssert(board, "b1", "c3");
        TestUtility.executeAndAssert(board, "c6", "d4");
        TestUtility.executeAndAssert(board, "g2", "g3");
        TestUtility.executeAndAssert(board, "d4", "f3");

        assertTrue(board.currentPlayer().isInCheckMate());
    }

    @Test
    public void testHippopotamusMate() {
        Board board = BoardSetup.createStandardBoard();

        TestUtility.executeAndAssert(board, "e2", "e4");
        TestUtility.executeAndAssert(board, "e7", "e5");
        TestUtility.executeAndAssert(board, "g1", "e2");
        TestUtility.executeAndAssert(board, "d8", "h4");
        TestUtility.executeAndAssert(board, "b1", "c3");
        TestUtility.executeAndAssert(board, "b8", "c6");
        TestUtility.executeAndAssert(board, "g2", "g3");
        TestUtility.executeAndAssert(board, "h4", "g5");
        TestUtility.executeAndAssert(board, "d2", "d4");
        TestUtility.executeAndAssert(board, "c6", "d4");
        TestUtility.executeAndAssert(board, "c1", "g5");
        TestUtility.executeAndAssert(board, "d4", "f3");

        assertTrue(board.currentPlayer().isInCheckMate());
    }

    @Test
    public void testBlackburneShillingMate() {
        Board board = BoardSetup.createStandardBoard();

        TestUtility.executeAndAssert(board, "e2", "e4");
        TestUtility.executeAndAssert(board, "e7", "e5");
        TestUtility.executeAndAssert(board, "g1", "f3");
        TestUtility.executeAndAssert(board, "b8", "c6");
        TestUtility.executeAndAssert(board, "f1", "c4");
        TestUtility.executeAndAssert(board, "c6", "d4");
        TestUtility.executeAndAssert(board, "f3", "e5");
        TestUtility.executeAndAssert(board, "d8", "g5");
        TestUtility.executeAndAssert(board, "e5", "f7");
        TestUtility.executeAndAssert(board, "g5", "g2");
        TestUtility.executeAndAssert(board, "h1", "f1");
        TestUtility.executeAndAssert(board, "g2", "e4");
        TestUtility.executeAndAssert(board, "c4", "e2");
        TestUtility.executeAndAssert(board, "d4", "f3");

        assertTrue(board.currentPlayer().isInCheckMate());
    }


    @Test
    public void testAnastasiaMate() {

        final Builder builder = new Builder();

        // Black Layout
        builder.setPiece(new Rook(0, Color.BLACK, true));
        builder.setPiece(new Rook(5, Color.BLACK, false));
        builder.setPiece(new Pawn(8, Color.BLACK, false));
        builder.setPiece(new Pawn(9, Color.BLACK, false));
        builder.setPiece(new Pawn(10, Color.BLACK, false));
        builder.setPiece(new Pawn(13, Color.BLACK, false));
        builder.setPiece(new Pawn(14, Color.BLACK, false));
        builder.setPiece(new King(15, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Knight(12, Color.WHITE, false));
        builder.setPiece(new Rook(27, Color.WHITE, false));
        builder.setPiece(new Pawn(41, Color.WHITE, false));
        builder.setPiece(new Pawn(48, Color.WHITE, false));
        builder.setPiece(new Pawn(53, Color.WHITE, false));
        builder.setPiece(new Pawn(54, Color.WHITE, false));
        builder.setPiece(new Pawn(55, Color.WHITE, false));
        builder.setPiece(new King(62, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);

        final Board board = builder.build();
        TestUtility.executeAndAssert(board, "d5", "h5");
        assertTrue(board.currentPlayer().isInCheckMate());
    }

    @Test
    public void testTwoBishopMate() {

        final Builder builder = new Builder();

        builder.setPiece(new King(7, Color.BLACK, false, false));
        builder.setPiece(new Pawn(8, Color.BLACK, false));
        builder.setPiece(new Pawn(10, Color.BLACK, false));
        builder.setPiece(new Pawn(15, Color.BLACK, false));
        builder.setPiece(new Pawn(17, Color.BLACK, false));
        // White Layout
        builder.setPiece(new Bishop(40, Color.WHITE, false));
        builder.setPiece(new Bishop(48, Color.WHITE, false));
        builder.setPiece(new King(53, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);

        final Board board = builder.build();
        TestUtility.executeAndAssert(board, "a3", "b2");
        assertTrue(board.currentPlayer().isInCheckMate());
    }

    @Test
    public void testQueenRookMate() {

        final Builder builder = new Builder();

        // Black Layout
        builder.setPiece(new King(5, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Rook(9, Color.WHITE, false));
        builder.setPiece(new Queen(16, Color.WHITE, false));
        builder.setPiece(new King(59, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);

        final Board board = builder.build();
        TestUtility.executeAndAssert(board, "a6", "a8");

        assertTrue(board.currentPlayer().isInCheckMate());

    }

    @Test
    public void testQueenKnightMate() {

        final Builder builder = new Builder();

        // Black Layout
        builder.setPiece(new King(4, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Queen(15, Color.WHITE, false));
        builder.setPiece(new Knight(29, Color.WHITE, false));
        builder.setPiece(new Pawn(55, Color.WHITE, false));
        builder.setPiece(new King(60, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);

        final Board board = builder.build();
        TestUtility.executeAndAssert(board, "h7", "e7");

        assertTrue(board.currentPlayer().isInCheckMate());

    }

    @Test
    public void testBackRankMate() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(4, Color.BLACK, false, false));
        builder.setPiece(new Rook(18, Color.BLACK, false));
        // White Layout
        builder.setPiece(new Pawn(53, Color.WHITE, false));
        builder.setPiece(new Pawn(54, Color.WHITE, false));
        builder.setPiece(new Pawn(55, Color.WHITE, false));
        builder.setPiece(new King(62, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.BLACK);

        final Board board = builder.build();
        TestUtility.executeAndAssert(board, "c6", "c1");
        assertTrue(board.currentPlayer().isInCheckMate());

    }
}

