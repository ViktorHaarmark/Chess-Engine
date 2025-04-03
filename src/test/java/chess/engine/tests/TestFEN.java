package chess.engine.tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import chess.engine.board.Board;
import chess.engine.board.BoardSetup;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.MoveTransition;
import chess.pgn.FenUtility;


public class TestFEN {

    @Test
    public void testWriteFEN1()  {
        final Board board = BoardSetup.createStandardBoard();
        final String fenString = FenUtility.createFENFromGame(board);
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", fenString);
    }

    @Test
    public void testWriteFEN2()  {
        final Board board = BoardSetup.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.moveStatus().isDone());
        final String fenString = FenUtility.createFENFromGame(t1.toBoard());
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fenString);
        final MoveTransition t2 = t1.toBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("c7"),
                        BoardUtils.getCoordinateAtPosition("c5")));
        assertTrue(t2.moveStatus().isDone());
        final String fenString2 = FenUtility.createFENFromGame(t2.toBoard());
        assertEquals("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 1", fenString2);

    }

}