package chess.engine.tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import chess.engine.board.Board;
import chess.engine.board.BoardSetup;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
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
        TestUtility.executeAndAssert(board, "e2", "e4");

        final String fenString = FenUtility.createFENFromGame(board);
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fenString);

        TestUtility.executeAndAssert(board, "c7", "c5");

        final String fenString2 = FenUtility.createFENFromGame(board);
        assertEquals("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 1", fenString2);

    }

}