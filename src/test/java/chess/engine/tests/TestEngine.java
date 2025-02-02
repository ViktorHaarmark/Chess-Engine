package chess.engine.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import chess.engine.Players.ai.AlphaBeta;
import chess.pgn.FenUtility;
import org.junit.jupiter.api.Test;

import chess.engine.board.Board;
import chess.engine.board.BoardSetup;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.MoveFactory;
import chess.engine.board.MoveTransition;

import chess.engine.Players.ai.MoveStrategy;
import chess.engine.Players.ai.MiniMax;


public class TestEngine {

    @Test
    public void testFoolsMate() {
        final Board board = BoardSetup.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t1.moveStatus().isDone());

        final MoveTransition t2 = t1.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t2.moveStatus().isDone());

        final MoveTransition t3 = t2.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g4")));
        assertTrue(t3.moveStatus().isDone());

        final MoveStrategy strategy = new MiniMax(4);

        final Move aiMove = strategy.execute(t3.toBoard());

        final Move bestMove = MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                BoardUtils.getCoordinateAtPosition("h4"));

        assertEquals(aiMove, bestMove);

    }

    @Test
    public void testMinMaxEqualsAlphaBeta() {
        final Board board = BoardSetup.createStandardBoard();
        assertEquals(board.currentPlayer(), board.getWhitePlayer());

        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));
        final MoveTransition t2 = t1.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e6")));

        final MoveTransition t3 = t2.toBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("d2"),
                        BoardUtils.getCoordinateAtPosition("d4")));
        final MoveStrategy miniMax = new MiniMax(4);
        final MoveStrategy alphaBeta = new AlphaBeta(4);
        final Move miniMaxBestMove = miniMax.execute(t3.toBoard());
        final Move alphaBetaBestMove = alphaBeta.execute(t3.toBoard());
        System.out.println("MiniMax Best Move: " + miniMaxBestMove);
        System.out.println("AlphaBeta Best Move: " + alphaBetaBestMove);
        assertEquals(miniMaxBestMove, alphaBetaBestMove);
    }
    @Test
    public void testAlphaBetaSpeed() {
        final Board board = FenUtility.createGameFromFEN("6k1/1b4pp/1B1Q4/4p1P1/p3q3/2P3r1/P1P2PP1/R5K1 w - - 1 0");

        final MoveStrategy alphaBeta = new AlphaBeta(4);

        final Move bestMove = alphaBeta.execute(board);
        assertEquals(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d6"), BoardUtils.getCoordinateAtPosition("e6")), bestMove);
        //Last seen speed was 4.6s and pos/sec was 56k, eval +10
    }

    @Test
    public void testMateInTwoTest1() {
        final Board board = FenUtility.createGameFromFEN("6k1/1b4pp/1B1Q4/4p1P1/p3q3/2P3r1/P1P2PP1/R5K1 w - - 1 0");
        final MoveStrategy miniMax = new MiniMax(4);
        final Move bestMove = miniMax.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d6"), BoardUtils.getCoordinateAtPosition("e6")));
    }

    @Test
    public void testMateInTwoTest2() {
        final Board board = FenUtility.createGameFromFEN("3r3r/1Q5p/p3q2k/3NBp1B/3p3n/5P2/PP4PP/4R2K w - - 1 0");
        final MiniMax alphaBeta = new MiniMax(4);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(
                bestMove,
                Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("b7"),
                        BoardUtils.getCoordinateAtPosition("g7")));
    }

    @Test
    public void testMateInTwoTest3() {
        final Board board = FenUtility.createGameFromFEN("rn3rk1/1R3ppp/2p5/8/PQ2P3/1P5P/2P1qPP1/3R2K1 w - - 1 0");
        final MiniMax miniMax = new MiniMax(4);
        final Move bestMove = miniMax.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("b4"), BoardUtils.getCoordinateAtPosition("f8")));
    }

    @Test
    public void testMateInFourTest1() {
        final Board board = FenUtility.createGameFromFEN("7k/4r2B/1pb5/2P5/4p2Q/2q5/2P2R2/1K6 w - - 1 0");
        final MiniMax miniMax = new MiniMax(4);
        final Move bestMove = miniMax.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("f2"), BoardUtils.getCoordinateAtPosition("f8")));
    }

    /* //TODO: Still too difficult for the engine
    @Test
    public void testMagnusBlackToMoveAndWinTest1() {
        final Board board = FenUtility.createGameFromFEN("2rr2k1/pb3pp1/4q2p/2pn4/2Q1P3/P4P2/1P3BPP/2KR2NR b - - 0 1");
        final MiniMax miniMax = new MiniMax(4);
        final Move bestMove = miniMax.execute(board);
        assertEquals(bestMove, Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d5"), BoardUtils.getCoordinateAtPosition("e3")));
    } */


}
