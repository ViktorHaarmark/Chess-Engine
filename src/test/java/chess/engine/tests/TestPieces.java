package chess.engine.tests;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardSetup;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.MoveTransition;
import chess.engine.pieces.Bishop;
import chess.engine.pieces.King;
import chess.engine.pieces.Knight;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Queen;
import chess.engine.pieces.Rook;
import chess.pgn.FenUtility;

import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Sets;

import static org.junit.jupiter.api.Assertions.*;

public class TestPieces {


    public int moveGenerationTest(int depth, Board board) {
        if (depth == 0) {
            return 1;
        }
        int numPosition = 0;
        Collection<Move> moves = board.getCurrentPlayer().getPossibleMoves();
        for (Move move : moves) {
            MoveTransition transition = board.getCurrentPlayer().makeMove(move);
            if (transition.moveStatus().isDone()) {
                numPosition += moveGenerationTest(depth - 1, transition.toBoard());
            }
        }

        return numPosition;
    }

    @Test
    public void moveTestPl1() {
        Board board = FenUtility.createGameFromFEN("R6R/3Q4/1Q4Q1/4Q3/2Q4Q/Q4Q2/pp1Q4/kBNN1KB1 w - - 0 1");
        assertEquals(218, moveGenerationTest(1, board));
    }

    @Test
    public void moveTestCoolPosition() {
        Board board = FenUtility.createGameFromFEN("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 0 1");

        assertEquals(44, moveGenerationTest(1, board));

        assertEquals(1486, moveGenerationTest(2, board));
        assertEquals(62379, moveGenerationTest(3, board));
        assertEquals(2103487, moveGenerationTest(4, board));
        assertEquals(89941194, moveGenerationTest(5, board));

    }


    @Test
    public void moveTestPly2() {
        Board board = BoardSetup.createStandardBoard();
        assertEquals(400, moveGenerationTest(2, board));

    }

    @Test
    public void moveTestPly3() {
        Board board = BoardSetup.createStandardBoard();
        assertEquals(8902, moveGenerationTest(3, board));

    }

    @Test
    public void moveTestPly4() {
        Board board = BoardSetup.createStandardBoard();
        assertEquals(197281, moveGenerationTest(4, board));

    }

    @Test
    public void moveTestPly5() {
        Board board = BoardSetup.createStandardBoard();
        assertEquals(4865609, moveGenerationTest(5, board));

    }


    @Test
    public void testMiddleQueenOnEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Queen(36, Color.WHITE, false));
        builder.setPiece(new King(60, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.getWhitePlayer().getPossibleMoves();
        final Collection<Move> blackLegals = board.getBlackPlayer().getPossibleMoves();
        assertEquals(31, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e8"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("a4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("b4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("c4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("f4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("g4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("h4"))));
    }

    @Test
    public void testLegalMoveForKnightInCenter() {

        final Board.Builder boardBuilder = new Board.Builder();
        // Black Layout
        boardBuilder.setPiece(new King(4, Color.BLACK, false, false));
        boardBuilder.setPiece(new Knight(28, Color.BLACK, false));
        // White Layout
        boardBuilder.setPiece(new Knight(36, Color.WHITE, false));
        boardBuilder.setPiece(new King(60, Color.WHITE, false, false));
        // Set the current player
        boardBuilder.setMoveMaker(Color.WHITE);
        final Board board = boardBuilder.build();
        final Collection<Move> whiteLegals = board.getWhitePlayer().getPossibleMoves();
        assertEquals(13, whiteLegals.size());
        final Move wm1 = Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("d6"));
        final Move wm2 = Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("f6"));
        final Move wm3 = Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("c5"));
        final Move wm4 = Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("g5"));
        final Move wm5 = Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("c3"));
        final Move wm6 = Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("g3"));
        final Move wm7 = Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("d2"));
        final Move wm8 = Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("f2"));

        assertTrue(whiteLegals.contains(wm1));
        assertTrue(whiteLegals.contains(wm2));
        assertTrue(whiteLegals.contains(wm3));
        assertTrue(whiteLegals.contains(wm4));
        assertTrue(whiteLegals.contains(wm5));
        assertTrue(whiteLegals.contains(wm6));
        assertTrue(whiteLegals.contains(wm7));
        assertTrue(whiteLegals.contains(wm8));

        final Board.Builder boardBuilder2 = new Board.Builder();
        // Black Layout
        boardBuilder2.setPiece(new King(4, Color.BLACK, false, false));
        boardBuilder2.setPiece(new Knight(28, Color.BLACK, false));
        // White Layout
        boardBuilder2.setPiece(new Knight(36, Color.WHITE, false));
        boardBuilder2.setPiece(new King(60, Color.WHITE, false, false));
        // Set the current player
        boardBuilder2.setMoveMaker(Color.BLACK);
        final Board board2 = boardBuilder2.build();
        final Collection<Move> blackLegals = board2.getBlackPlayer().getPossibleMoves();

        final Move bm1 = Move.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("d7"));
        final Move bm2 = Move.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("f7"));
        final Move bm3 = Move.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("c6"));
        final Move bm4 = Move.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("g6"));
        final Move bm5 = Move.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("c4"));
        final Move bm6 = Move.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("g4"));
        final Move bm7 = Move.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("d3"));
        final Move bm8 = Move.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("f3"));

        assertEquals(13, blackLegals.size());

        assertTrue(blackLegals.contains(bm1));
        assertTrue(blackLegals.contains(bm2));
        assertTrue(blackLegals.contains(bm3));
        assertTrue(blackLegals.contains(bm4));
        assertTrue(blackLegals.contains(bm5));
        assertTrue(blackLegals.contains(bm6));
        assertTrue(blackLegals.contains(bm7));
        assertTrue(blackLegals.contains(bm8));
    }


    @Test
    public void testKnightInCorners() {
        final Board.Builder boardBuilder = new Board.Builder();
        boardBuilder.setPiece(new King(4, Color.BLACK, false, false));
        boardBuilder.setPiece(new Knight(0, Color.BLACK, false));
        boardBuilder.setPiece(new Knight(56, Color.WHITE, false));
        boardBuilder.setPiece(new King(60, Color.WHITE, false, false));
        boardBuilder.setMoveMaker(Color.WHITE);
        final Board board = boardBuilder.build();
        final Collection<Move> whiteLegals = board.getWhitePlayer().getPossibleMoves();
        final Collection<Move> blackLegals = board.getBlackPlayer().getPossibleMoves();
        assertEquals(7, whiteLegals.size());
        assertEquals(7, blackLegals.size());

        final Move wm1 = Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("b3"));
        final Move wm2 = Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("c2"));
        assertTrue(whiteLegals.contains(wm1));
        assertTrue(whiteLegals.contains(wm2));
        final Move bm1 = Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("b6"));
        final Move bm2 = Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("c7"));
        assertTrue(blackLegals.contains(bm1));
        assertTrue(blackLegals.contains(bm2));


    }


    @Test
    public void testMiddleBishopOnEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(35, Color.WHITE, false));
        builder.setPiece(new King(60, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.getWhitePlayer().getPossibleMoves();
        final Collection<Move> blackLegals = board.getBlackPlayer().getPossibleMoves();
        assertEquals(18, whiteLegals.size());
        assertEquals(5, blackLegals.size());

        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("a7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("b6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("c5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("f2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("g1"))));

    }


    @Test
    public void testTopLeftBishopOnEmptyBoard() {
        Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(0, Color.WHITE, false));
        builder.setPiece(new King(60, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.getWhitePlayer().getPossibleMoves();
        final Collection<Move> blackLegals = board.getBlackPlayer().getPossibleMoves();
        assertEquals(board.getPiece(0), board.getPiece(0));
        assertNotNull(board.getPiece(0));
        assertEquals(12, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("b7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("c6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("d5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("e4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("f3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("g2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("h1"))));
    }


    @Test
    public void testTopRightBishopOnEmptyBoard() {
        Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(7, Color.WHITE, false));
        builder.setPiece(new King(60, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.getWhitePlayer().getPossibleMoves();
        final Collection<Move> blackLegals = board.getBlackPlayer().getPossibleMoves();
        assertEquals(12, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("g7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("f6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("c3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("b2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("a1"))));
    }

    @Test
    public void testBottomLeftBishopOnEmptyBoard() {
        Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(56, Color.WHITE, false));
        builder.setPiece(new King(60, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.getWhitePlayer().getPossibleMoves();
        final Collection<Move> blackLegals = board.getBlackPlayer().getPossibleMoves();
        assertEquals(12, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("b2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("c3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("f6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("g7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("h8"))));
    }

    @Test
    public void testBottomRightBishopOnEmptyBoard() {
        Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(63, Color.WHITE, false));
        builder.setPiece(new King(60, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.getWhitePlayer().getPossibleMoves();
        final Collection<Move> blackLegals = board.getBlackPlayer().getPossibleMoves();
        assertEquals(12, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("g2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("f3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("e4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("d5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("c6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("b7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("a8"))));
    }

    @Test
    public void testMiddleRookOnEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Rook(36, Color.WHITE, false));
        builder.setPiece(new King(60, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.getWhitePlayer().getPossibleMoves();
        final Collection<Move> blackLegals = board.getBlackPlayer().getPossibleMoves();
        assertEquals(18, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e8"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("a4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("b4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("c4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("f4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("g4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("h4"))));
    }

    @Test
    public void testPawnPromotion() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new Rook(3, Color.BLACK, false));
        builder.setPiece(new King(22, Color.BLACK, false, false));
        // White Layout
        builder.setPiece(new Pawn(15, Color.WHITE, false));
        builder.setPiece(new King(52, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);
        final Board board = builder.build();
        final Move m1 = Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition(
                "h7"), BoardUtils.getCoordinateAtPosition("h8"));
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(m1);
        assertTrue(t1.moveStatus().isDone());
        final Move m2 = Move.MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("d8"), BoardUtils.getCoordinateAtPosition("h8"));
        final MoveTransition t2 = t1.toBoard().getCurrentPlayer().makeMove(m2);
        assertTrue(t2.moveStatus().isDone());
        final Move m3 = Move.MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("d2"));
        final MoveTransition t3 = board.getCurrentPlayer().makeMove(m3);
        assertTrue(t3.moveStatus().isDone());
    }

    @Test
    public void testSimpleWhiteEnPassant() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Color.BLACK, false, false));
        builder.setPiece(new Pawn(11, Color.BLACK, false));
        // White Layout
        builder.setPiece(new Pawn(52, Color.WHITE, false));
        builder.setPiece(new King(60, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);
        final Board board = builder.build();
        final Move m1 = Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition(
                "e2"), BoardUtils.getCoordinateAtPosition("e4"));
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(m1);
        assertTrue(t1.moveStatus().isDone());
        final Move m2 = Move.MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("e8"), BoardUtils.getCoordinateAtPosition("d8"));
        final MoveTransition t2 = t1.toBoard().getCurrentPlayer().makeMove(m2);
        assertTrue(t2.moveStatus().isDone());
        final Move m3 = Move.MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e5"));
        final MoveTransition t3 = t2.toBoard().getCurrentPlayer().makeMove(m3);
        assertTrue(t3.moveStatus().isDone());
        final Move m4 = Move.MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("d7"), BoardUtils.getCoordinateAtPosition("d5"));
        final MoveTransition t4 = t3.toBoard().getCurrentPlayer().makeMove(m4);
        assertTrue(t4.moveStatus().isDone());
        final Move m5 = Move.MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("d6"));
        final MoveTransition t5 = t4.toBoard().getCurrentPlayer().makeMove(m5);
        assertTrue(t5.moveStatus().isDone());

    }


    @Test
    public void testSimpleBlackEnPassant() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Color.BLACK, false, false));
        builder.setPiece(new Pawn(11, Color.BLACK, false));
        // White Layout
        builder.setPiece(new Pawn(52, Color.WHITE, false));
        builder.setPiece(new King(60, Color.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);
        final Board board = builder.build();
        final Move m1 = Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition(
                "e1"), BoardUtils.getCoordinateAtPosition("d1"));
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(m1);
        assertTrue(t1.moveStatus().isDone());
        final Move m2 = Move.MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("d7"), BoardUtils.getCoordinateAtPosition("d5"));
        final MoveTransition t2 = t1.toBoard().getCurrentPlayer().makeMove(m2);
        assertTrue(t2.moveStatus().isDone());
        final Move m3 = Move.MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("d1"), BoardUtils.getCoordinateAtPosition("c1"));
        final MoveTransition t3 = t2.toBoard().getCurrentPlayer().makeMove(m3);
        assertTrue(t3.moveStatus().isDone());
        final Move m4 = Move.MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("d5"), BoardUtils.getCoordinateAtPosition("d4"));
        final MoveTransition t4 = t3.toBoard().getCurrentPlayer().makeMove(m4);
        assertTrue(t4.moveStatus().isDone());
        final Move m5 = Move.MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("e4"));
        final MoveTransition t5 = t4.toBoard().getCurrentPlayer().makeMove(m5);
        assertTrue(t5.moveStatus().isDone());
        final Move m6 = Move.MoveFactory.createMove(t5.toBoard(), BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("e3"));
        final MoveTransition t6 = t5.toBoard().getCurrentPlayer().makeMove(m6);
        assertTrue(t6.moveStatus().isDone());
    }

    @Test
    public void testEnPassant2() {
        final Board board = BoardSetup.createStandardBoard();
        final Move m1 = Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition(
                "e2"), BoardUtils.getCoordinateAtPosition("e3"));
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(m1);
        assertTrue(t1.moveStatus().isDone());
        final Move m2 = Move.MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("h7"), BoardUtils.getCoordinateAtPosition("h5"));
        final MoveTransition t2 = t1.toBoard().getCurrentPlayer().makeMove(m2);
        assertTrue(t2.moveStatus().isDone());
        final Move m3 = Move.MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("e3"), BoardUtils.getCoordinateAtPosition("e4"));
        final MoveTransition t3 = t2.toBoard().getCurrentPlayer().makeMove(m3);
        assertTrue(t3.moveStatus().isDone());
        final Move m4 = Move.MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("h5"), BoardUtils.getCoordinateAtPosition("h4"));
        final MoveTransition t4 = t3.toBoard().getCurrentPlayer().makeMove(m4);
        assertTrue(t4.moveStatus().isDone());
        final Move m5 = Move.MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("g2"), BoardUtils.getCoordinateAtPosition("g4"));
        final MoveTransition t5 = t4.toBoard().getCurrentPlayer().makeMove(m5);
        assertTrue(t5.moveStatus().isDone());
    }

    @Test
    public void testKingEquality() {
        final Board board = BoardSetup.createStandardBoard();
        final Board board2 = BoardSetup.createStandardBoard();
        assertEquals(board.getPiece(60), board2.getPiece(60));
        assertNotEquals(null, board.getPiece(60));
    }


    @Test
    public void testHashCode() {
        final Board board = BoardSetup.createStandardBoard();
        final Set<Piece> pieceSet = Sets.newHashSet(board.getAllPieces());
        final Set<Piece> whitePieceSet = Sets.newHashSet(board.getWhitePieces());
        final Set<Piece> blackPieceSet = Sets.newHashSet(board.getBlackPieces());
        assertEquals(32, pieceSet.size());
        assertEquals(16, whitePieceSet.size());
        assertEquals(16, blackPieceSet.size());
    }

}