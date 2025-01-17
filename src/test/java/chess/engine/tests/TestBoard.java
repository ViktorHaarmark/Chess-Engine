package chess.engine.tests;

import chess.Color;
import chess.engine.Players.ai.StandardBoardEvaluator;
import chess.engine.board.*;
import chess.engine.board.Board.Builder;
import chess.engine.board.Move.MoveFactory;
import chess.engine.pieces.*;
import com.google.common.collect.Iterables;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBoard {

    @Test
    public void initialBoard() {

        final Board board = BoardSetup.createStandardBoard();
        assertEquals(20, board.currentPlayer().getLegalMoves().size());
        assertEquals(20, board.currentPlayer().getOpponent().getLegalMoves().size());
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
        assertFalse(board.currentPlayer().isCastled());
        //assertTrue(board.currentPlayer().isKingSideCastleCapable());
        //assertTrue(board.currentPlayer().isQueenSideCastleCapable());
        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.currentPlayer().getOpponent().isCastled());
        //assertTrue(board.currentPlayer().getOpponent().isKingSideCastleCapable());
        //assertTrue(board.currentPlayer().getOpponent().isQueenSideCastleCapable());
        assertEquals("White", board.whitePlayer().toString());
        assertEquals("Black", board.blackPlayer().toString());

        final Iterable<Piece> allPieces = board.getAllPieces();
        final Iterable<Move> allMoves = Iterables.concat(board.whitePlayer().getLegalMoves(), board.blackPlayer().getLegalMoves());
        for (final Move move : allMoves) {
            assertFalse(move.isCapture());
            assertFalse(move.isCastlingMove());
            //assertEquals(MoveUtils.exchangeScore(move), 1);
        }

        assertEquals(40, Iterables.size(allMoves));
        assertEquals(32, Iterables.size(allPieces));
        assertFalse(BoardUtils.isEndGame(board));
        //assertFalse(BoardUtils.isThreatenedBoardImmediate(board));
        //assertEquals(StandardBoardEvaluator.get().evaluate(board, 0), 0);
        assertNull(board.getPiece(35));
    }

    @Test
    public void testPlainKingMove() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(4, Color.BLACK, false, false));
        builder.setPiece(new Pawn(12, Color.BLACK, false));
        // White Layout
        builder.setPiece(new Pawn(52, Color.WHITE, false));
        builder.setPiece(new King(60, Color.WHITE, false, false));
        builder.setMoveMaker(Color.WHITE);
        // Set the current player
        final Board board = builder.build();
        System.out.println(board);

        assertEquals(6, board.whitePlayer().getLegalMoves().size());
        assertEquals(6, board.blackPlayer().getLegalMoves().size());
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());
        StandardBoardEvaluator evaluator = new StandardBoardEvaluator();
        System.out.println(evaluator.evaluate(board));
        assertEquals(0, evaluator.evaluate(board));

        final Move move = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e1"),
                BoardUtils.getCoordinateAtPosition("f1"));

        final MoveTransition moveTransition = board.currentPlayer()
                .makeMove(move);

        assertEquals(moveTransition.move(), move);
        assertEquals(moveTransition.fromBoard(), board);
        assertEquals(moveTransition.toBoard().currentPlayer(), moveTransition.toBoard().blackPlayer());

        assertTrue(moveTransition.moveStatus().isDone());
        assertEquals(61, moveTransition.toBoard().whitePlayer().getPlayerKing().getPiecePosition());
        System.out.println(moveTransition.toBoard());

    }

    @Test
    public void testBoardConsistency() {
        final Board board = BoardSetup.createStandardBoard();
        assertEquals(board.currentPlayer(), board.whitePlayer());

        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));
        final MoveTransition t2 = t1.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        final MoveTransition t3 = t2.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));
        final MoveTransition t4 = t3.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d5")));

        final MoveTransition t5 = t4.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("e4"),
                        BoardUtils.getCoordinateAtPosition("d5")));
        final MoveTransition t6 = t5.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t5.toBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("d5")));

        final MoveTransition t7 = t6.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t6.toBoard(), BoardUtils.getCoordinateAtPosition("f3"),
                        BoardUtils.getCoordinateAtPosition("g5")));
        final MoveTransition t8 = t7.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t7.toBoard(), BoardUtils.getCoordinateAtPosition("f7"),
                        BoardUtils.getCoordinateAtPosition("f6")));

        final MoveTransition t9 = t8.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t8.toBoard(), BoardUtils.getCoordinateAtPosition("d1"),
                        BoardUtils.getCoordinateAtPosition("h5")));
        final MoveTransition t10 = t9.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t9.toBoard(), BoardUtils.getCoordinateAtPosition("g7"),
                        BoardUtils.getCoordinateAtPosition("g6")));

        final MoveTransition t11 = t10.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t10.toBoard(), BoardUtils.getCoordinateAtPosition("h5"),
                        BoardUtils.getCoordinateAtPosition("h4")));
        final MoveTransition t12 = t11.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t11.toBoard(), BoardUtils.getCoordinateAtPosition("f6"),
                        BoardUtils.getCoordinateAtPosition("g5")));

        final MoveTransition t13 = t12.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t12.toBoard(), BoardUtils.getCoordinateAtPosition("h4"),
                        BoardUtils.getCoordinateAtPosition("g5")));
        final MoveTransition t14 = t13.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t13.toBoard(), BoardUtils.getCoordinateAtPosition("d5"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertEquals(t14.toBoard().whitePlayer().getActivePieces().size(), calculatedActivesFor(t14.toBoard(), Color.WHITE));
        assertEquals(t14.toBoard().blackPlayer().getActivePieces().size(), calculatedActivesFor(t14.toBoard(), Color.BLACK));

    }

    @Test //(expected=RuntimeException.class)
    public void testInvalidBoard() {

        final Builder builder = new Builder();
        // Black pieces
        builder.setPiece(new Rook(0, Color.BLACK, true));
        builder.setPiece(new Knight(1, Color.BLACK, true));
        builder.setPiece(new Bishop(2, Color.BLACK, true));
        builder.setPiece(new Queen(3, Color.BLACK, true));
        builder.setPiece(new King(4, Color.BLACK, false, false));
        builder.setPiece(new Bishop(5, Color.BLACK, true));
        builder.setPiece(new Knight(6, Color.BLACK, true));
        builder.setPiece(new Rook(7, Color.BLACK, true));
        builder.setPiece(new Pawn(8, Color.BLACK, true));
        builder.setPiece(new Pawn(9, Color.BLACK, true));
        builder.setPiece(new Pawn(10, Color.BLACK, true));
        builder.setPiece(new Pawn(11, Color.BLACK, true));
        builder.setPiece(new Pawn(12, Color.BLACK, true));
        builder.setPiece(new Pawn(13, Color.BLACK, true));
        builder.setPiece(new Pawn(14, Color.BLACK, true));
        builder.setPiece(new Pawn(15, Color.BLACK, true));


        //White pieces
        builder.setPiece(new Rook(56, Color.WHITE, true));
        builder.setPiece(new Knight(57, Color.WHITE, true));
        builder.setPiece(new Bishop(58, Color.WHITE, true));
        builder.setPiece(new Queen(59, Color.WHITE, true));
        builder.setPiece(new King(60, Color.WHITE, false, false));
        builder.setPiece(new Bishop(61, Color.WHITE, true));
        builder.setPiece(new Knight(62, Color.WHITE, true));
        builder.setPiece(new Rook(63, Color.WHITE, true));
        builder.setPiece(new Pawn(48, Color.WHITE, true));
        builder.setPiece(new Pawn(49, Color.WHITE, true));
        builder.setPiece(new Pawn(50, Color.WHITE, true));
        builder.setPiece(new Pawn(51, Color.WHITE, true));
        builder.setPiece(new Pawn(52, Color.WHITE, true));
        builder.setPiece(new Pawn(53, Color.WHITE, true));
        builder.setPiece(new Pawn(54, Color.WHITE, true));
        builder.setPiece(new Pawn(55, Color.WHITE, true));

        builder.setMoveMaker(Color.WHITE);
        //build the board
        builder.build();
    }

    @Test
    public void testAlgebreicNotation() {
        assertEquals("a8", BoardUtils.getPositionAtCoordinate(0));
        assertEquals("b8", BoardUtils.getPositionAtCoordinate(1));
        assertEquals("c8", BoardUtils.getPositionAtCoordinate(2));
        assertEquals("d8", BoardUtils.getPositionAtCoordinate(3));
        assertEquals("e8", BoardUtils.getPositionAtCoordinate(4));
        assertEquals("f8", BoardUtils.getPositionAtCoordinate(5));
        assertEquals("g8", BoardUtils.getPositionAtCoordinate(6));
        assertEquals("h8", BoardUtils.getPositionAtCoordinate(7));
    }

    @Test
    public void mem() {
        final Runtime runtime = Runtime.getRuntime();
        long start, end;
        runtime.gc();
        start = runtime.freeMemory();
        Board board = BoardSetup.createStandardBoard();
        end = runtime.freeMemory();
        System.out.println("That took " + (start - end) + " bytes.");

    }

    private static int calculatedActivesFor(final Board board,
                                            final Color color) {
        int count = 0;
        for (final Piece p : board.getAllPieces()) {
            if (p.getPieceColor().equals(color)) {
                count++;
            }
        }
        return count;
    }

}