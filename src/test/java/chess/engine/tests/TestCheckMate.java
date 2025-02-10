package chess.engine.tests;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.Board.Builder;
import chess.engine.board.BoardSetup;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move.MoveFactory;
import chess.engine.board.MoveTransition;
import chess.engine.pieces.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCheckMate {

    @Test
    public void testFoolsMate() {

        final Board board = BoardSetup.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t1.moveStatus().isDone());

        final MoveTransition t2 = t1.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.moveStatus().isDone());

        final MoveTransition t3 = t2.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g4")));

        assertTrue(t3.moveStatus().isDone());

        final MoveTransition t4 = t3.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("h4")));

        assertTrue(t4.moveStatus().isDone());

        assertTrue(t4.toBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testScholarsMate() {

        final Board board = BoardSetup.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.moveStatus().isDone());

        final MoveTransition t2 = t1.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("a7"),
                        BoardUtils.getCoordinateAtPosition("a6")));

        assertTrue(t2.moveStatus().isDone());

        final MoveTransition t3 = t2.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("d1"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t3.moveStatus().isDone());

        final MoveTransition t4 = t3.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("a6"),
                        BoardUtils.getCoordinateAtPosition("a5")));

        assertTrue(t4.moveStatus().isDone());

        final MoveTransition t5 = t4.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("f1"),
                        BoardUtils.getCoordinateAtPosition("c4")));

        assertTrue(t5.moveStatus().isDone());

        final MoveTransition t6 = t5.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.toBoard(), BoardUtils.getCoordinateAtPosition("a5"),
                        BoardUtils.getCoordinateAtPosition("a4")));

        assertTrue(t6.moveStatus().isDone());

        final MoveTransition t7 = t6.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.toBoard(), BoardUtils.getCoordinateAtPosition("f3"),
                        BoardUtils.getCoordinateAtPosition("f7")));

        assertTrue(t7.moveStatus().isDone());
        assertTrue(t7.toBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testLegalsMate() {

        final Board board = BoardSetup.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.moveStatus().isDone());

        final MoveTransition t2 = t1.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.moveStatus().isDone());

        final MoveTransition t3 = t2.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("f1"),
                        BoardUtils.getCoordinateAtPosition("c4")));

        assertTrue(t3.moveStatus().isDone());

        final MoveTransition t4 = t3.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d6")));

        assertTrue(t4.moveStatus().isDone());

        final MoveTransition t5 = t4.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t5.moveStatus().isDone());

        final MoveTransition t6 = t5.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.toBoard(), BoardUtils.getCoordinateAtPosition("c8"),
                        BoardUtils.getCoordinateAtPosition("g4")));

        assertTrue(t6.moveStatus().isDone());

        final MoveTransition t7 = t6.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.toBoard(), BoardUtils.getCoordinateAtPosition("b1"),
                        BoardUtils.getCoordinateAtPosition("c3")));

        assertTrue(t7.moveStatus().isDone());

        final MoveTransition t8 = t7.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.toBoard(), BoardUtils.getCoordinateAtPosition("g7"),
                        BoardUtils.getCoordinateAtPosition("g6")));

        assertTrue(t8.moveStatus().isDone());

        final MoveTransition t9 = t8.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.toBoard(), BoardUtils.getCoordinateAtPosition("f3"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t9.moveStatus().isDone());

        final MoveTransition t10 = t9.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.toBoard(), BoardUtils.getCoordinateAtPosition("g4"),
                        BoardUtils.getCoordinateAtPosition("d1")));

        assertTrue(t10.moveStatus().isDone());

        final MoveTransition t11 = t10.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.toBoard(), BoardUtils.getCoordinateAtPosition("c4"),
                        BoardUtils.getCoordinateAtPosition("f7")));

        assertTrue(t11.moveStatus().isDone());

        final MoveTransition t12 = t11.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.toBoard(), BoardUtils.getCoordinateAtPosition("e8"),
                        BoardUtils.getCoordinateAtPosition("e7")));

        assertTrue(t12.moveStatus().isDone());

        final MoveTransition t13 = t12.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t12.toBoard(), BoardUtils.getCoordinateAtPosition("c3"),
                        BoardUtils.getCoordinateAtPosition("d5")));

        assertTrue(t13.moveStatus().isDone());
        assertTrue(t13.toBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testSevenMoveMate() {

        final Board board = BoardSetup.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.moveStatus().isDone());

        final MoveTransition t2 = t1.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.moveStatus().isDone());

        final MoveTransition t3 = t2.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("d2"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t3.moveStatus().isDone());

        final MoveTransition t4 = t3.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d6")));

        assertTrue(t4.moveStatus().isDone());

        final MoveTransition t5 = t4.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t5.moveStatus().isDone());

        final MoveTransition t6 = t5.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.toBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("e7")));

        assertTrue(t6.moveStatus().isDone());

        final MoveTransition t7 = t6.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.toBoard(), BoardUtils.getCoordinateAtPosition("e5"),
                        BoardUtils.getCoordinateAtPosition("d6")));

        assertTrue(t7.moveStatus().isDone());

        final MoveTransition t8 = t7.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.toBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t8.moveStatus().isDone());

        final MoveTransition t9 = t8.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.toBoard(), BoardUtils.getCoordinateAtPosition("f1"),
                        BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t9.moveStatus().isDone());

        final MoveTransition t10 = t9.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.toBoard(), BoardUtils.getCoordinateAtPosition("e4"),
                        BoardUtils.getCoordinateAtPosition("g2")));

        assertTrue(t10.moveStatus().isDone());

        final MoveTransition t11 = t10.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.toBoard(), BoardUtils.getCoordinateAtPosition("d6"),
                        BoardUtils.getCoordinateAtPosition("c7")));

        assertTrue(t11.moveStatus().isDone());

        final MoveTransition t12 = t11.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.toBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("h1")));

        assertTrue(t12.moveStatus().isDone());

        final MoveTransition t13 = t12.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t12.toBoard(), BoardUtils.getCoordinateAtPosition("d1"),
                        BoardUtils.getCoordinateAtPosition("d8")));

        assertTrue(t13.moveStatus().isDone());
        assertTrue(t13.toBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testGrecoGame() {

        final Board board = BoardSetup.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d2"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t1.moveStatus().isDone());

        final MoveTransition t2 = t1.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("g8"),
                        BoardUtils.getCoordinateAtPosition("f6")));

        assertTrue(t2.moveStatus().isDone());

        final MoveTransition t3 = t2.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("b1"),
                        BoardUtils.getCoordinateAtPosition("d2")));

        assertTrue(t3.moveStatus().isDone());

        final MoveTransition t4 = t3.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t4.moveStatus().isDone());

        final MoveTransition t5 = t4.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t5.moveStatus().isDone());

        final MoveTransition t6 = t5.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.toBoard(), BoardUtils.getCoordinateAtPosition("f6"),
                        BoardUtils.getCoordinateAtPosition("g4")));


        assertTrue(t6.moveStatus().isDone());

        final MoveTransition t7 = t6.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.toBoard(), BoardUtils.getCoordinateAtPosition("h2"),
                        BoardUtils.getCoordinateAtPosition("h3")));

        assertTrue(t7.moveStatus().isDone());

        final MoveTransition t8 = t7.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.toBoard(), BoardUtils.getCoordinateAtPosition("g4"),
                        BoardUtils.getCoordinateAtPosition("e3")));

        assertTrue(t8.moveStatus().isDone());

        final MoveTransition t9 = t8.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.toBoard(), BoardUtils.getCoordinateAtPosition("f2"),
                        BoardUtils.getCoordinateAtPosition("e3")));

        assertTrue(t9.moveStatus().isDone());

        final MoveTransition t10 = t9.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.toBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("h4")));

        assertTrue(t10.moveStatus().isDone());

        final MoveTransition t11 = t10.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.toBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g3")));

        assertTrue(t11.moveStatus().isDone());

        final MoveTransition t12 = t11.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.toBoard(), BoardUtils.getCoordinateAtPosition("h4"),
                        BoardUtils.getCoordinateAtPosition("g3")));

        assertTrue(t12.moveStatus().isDone());
        assertTrue(t12.toBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testOlympicGame() {

        final Board board = BoardSetup.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.moveStatus().isDone());

        final MoveTransition t2 = t1.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("c7"),
                        BoardUtils.getCoordinateAtPosition("c6")));

        assertTrue(t2.moveStatus().isDone());

        final MoveTransition t3 = t2.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t3.moveStatus().isDone());

        final MoveTransition t4 = t3.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d5")));

        assertTrue(t4.moveStatus().isDone());

        final MoveTransition t5 = t4.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("b1"),
                        BoardUtils.getCoordinateAtPosition("c3")));

        assertTrue(t5.moveStatus().isDone());

        final MoveTransition t6 = t5.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.toBoard(), BoardUtils.getCoordinateAtPosition("d5"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t6.moveStatus().isDone());

        final MoveTransition t7 = t6.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.toBoard(), BoardUtils.getCoordinateAtPosition("c3"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t7.moveStatus().isDone());

        final MoveTransition t8 = t7.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.toBoard(), BoardUtils.getCoordinateAtPosition("b8"),
                        BoardUtils.getCoordinateAtPosition("d7")));

        assertTrue(t8.moveStatus().isDone());

        final MoveTransition t9 = t8.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.toBoard(), BoardUtils.getCoordinateAtPosition("d1"),
                        BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t9.moveStatus().isDone());

        final MoveTransition t10 = t9.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.toBoard(), BoardUtils.getCoordinateAtPosition("g8"),
                        BoardUtils.getCoordinateAtPosition("f6")));

        assertTrue(t10.moveStatus().isDone());

        final MoveTransition t11 = t10.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.toBoard(), BoardUtils.getCoordinateAtPosition("e4"),
                        BoardUtils.getCoordinateAtPosition("d6")));

        assertTrue(t11.moveStatus().isDone());
        assertTrue(t11.toBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testAnotherGame() {

        final Board board = BoardSetup.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.moveStatus().isDone());

        final MoveTransition t2 = t1.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.moveStatus().isDone());

        final MoveTransition t3 = t2.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t3.moveStatus().isDone());

        final MoveTransition t4 = t3.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("b8"),
                        BoardUtils.getCoordinateAtPosition("c6")));

        assertTrue(t4.moveStatus().isDone());

        final MoveTransition t5 = t4.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("f1"),
                        BoardUtils.getCoordinateAtPosition("c4")));

        assertTrue(t5.moveStatus().isDone());

        final MoveTransition t6 = t5.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.toBoard(), BoardUtils.getCoordinateAtPosition("c6"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t6.moveStatus().isDone());

        final MoveTransition t7 = t6.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.toBoard(), BoardUtils.getCoordinateAtPosition("f3"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t7.moveStatus().isDone());

        final MoveTransition t8 = t7.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.toBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("g5")));

        assertTrue(t8.moveStatus().isDone());

        final MoveTransition t9 = t8.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.toBoard(), BoardUtils.getCoordinateAtPosition("e5"),
                        BoardUtils.getCoordinateAtPosition("f7")));

        assertTrue(t9.moveStatus().isDone());

        final MoveTransition t10 = t9.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.toBoard(), BoardUtils.getCoordinateAtPosition("g5"),
                        BoardUtils.getCoordinateAtPosition("g2")));

        assertTrue(t10.moveStatus().isDone());

        final MoveTransition t11 = t10.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.toBoard(), BoardUtils.getCoordinateAtPosition("h1"),
                        BoardUtils.getCoordinateAtPosition("f1")));

        assertTrue(t11.moveStatus().isDone());

        final MoveTransition t12 = t11.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.toBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t12.moveStatus().isDone());

        final MoveTransition t13 = t12.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t12.toBoard(), BoardUtils.getCoordinateAtPosition("c4"),
                        BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t13.moveStatus().isDone());

        final MoveTransition t14 = t13.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t13.toBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t14.moveStatus().isDone());
        assertTrue(t14.toBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testSmotheredMate() {

        final Board board = BoardSetup.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.moveStatus().isDone());

        final MoveTransition t2 = t1.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.moveStatus().isDone());

        final MoveTransition t3 = t2.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t3.moveStatus().isDone());

        final MoveTransition t4 = t3.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("b8"),
                        BoardUtils.getCoordinateAtPosition("c6")));

        assertTrue(t4.moveStatus().isDone());

        final MoveTransition t5 = t4.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("b1"),
                        BoardUtils.getCoordinateAtPosition("c3")));

        assertTrue(t4.moveStatus().isDone());

        final MoveTransition t6 = t5.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.toBoard(), BoardUtils.getCoordinateAtPosition("c6"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t6.moveStatus().isDone());

        final MoveTransition t7 = t6.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.toBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g3")));

        assertTrue(t7.moveStatus().isDone());

        final MoveTransition t8 = t7.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.toBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t8.moveStatus().isDone());
        assertTrue(t8.toBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testHippopotamusMate() {

        final Board board = BoardSetup.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.moveStatus().isDone());

        final MoveTransition t2 = t1.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.moveStatus().isDone());

        final MoveTransition t3 = t2.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t3.moveStatus().isDone());

        final MoveTransition t4 = t3.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("h4")));

        assertTrue(t4.moveStatus().isDone());

        final MoveTransition t5 = t4.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("b1"),
                        BoardUtils.getCoordinateAtPosition("c3")));

        assertTrue(t5.moveStatus().isDone());

        final MoveTransition t6 = t5.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.toBoard(), BoardUtils.getCoordinateAtPosition("b8"),
                        BoardUtils.getCoordinateAtPosition("c6")));

        assertTrue(t6.moveStatus().isDone());

        final MoveTransition t7 = t6.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.toBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g3")));

        assertTrue(t7.moveStatus().isDone());

        final MoveTransition t8 = t7.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.toBoard(), BoardUtils.getCoordinateAtPosition("h4"),
                        BoardUtils.getCoordinateAtPosition("g5")));


        assertTrue(t8.moveStatus().isDone());

        final MoveTransition t9 = t8.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.toBoard(), BoardUtils.getCoordinateAtPosition("d2"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t9.moveStatus().isDone());

        final MoveTransition t10 = t9.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.toBoard(), BoardUtils.getCoordinateAtPosition("c6"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t10.moveStatus().isDone());

        final MoveTransition t11 = t10.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.toBoard(), BoardUtils.getCoordinateAtPosition("c1"),
                        BoardUtils.getCoordinateAtPosition("g5")));

        assertTrue(t11.moveStatus().isDone());

        final MoveTransition t12 = t11.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.toBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t12.moveStatus().isDone());
        assertTrue(t12.toBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testBlackburneShillingMate() {

        final Board board = BoardSetup.createStandardBoard();

        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.moveStatus().isDone());

        final MoveTransition t2 = t1.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.toBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.moveStatus().isDone());

        final MoveTransition t3 = t2.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.toBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t3.moveStatus().isDone());

        final MoveTransition t4 = t3.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.toBoard(), BoardUtils.getCoordinateAtPosition("b8"),
                        BoardUtils.getCoordinateAtPosition("c6")));

        assertTrue(t4.moveStatus().isDone());

        final MoveTransition t5 = t4.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.toBoard(), BoardUtils.getCoordinateAtPosition("f1"),
                        BoardUtils.getCoordinateAtPosition("c4")));

        assertTrue(t5.moveStatus().isDone());

        final MoveTransition t6 = t5.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.toBoard(), BoardUtils.getCoordinateAtPosition("c6"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t6.moveStatus().isDone());

        final MoveTransition t7 = t6.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.toBoard(), BoardUtils.getCoordinateAtPosition("f3"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t7.moveStatus().isDone());

        final MoveTransition t8 = t7.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.toBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("g5")));

        assertTrue(t8.moveStatus().isDone());

        final MoveTransition t9 = t8.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.toBoard(), BoardUtils.getCoordinateAtPosition("e5"),
                        BoardUtils.getCoordinateAtPosition("f7")));

        assertTrue(t9.moveStatus().isDone());

        final MoveTransition t10 = t9.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.toBoard(), BoardUtils.getCoordinateAtPosition("g5"),
                        BoardUtils.getCoordinateAtPosition("g2")));

        assertTrue(t10.moveStatus().isDone());

        final MoveTransition t11 = t10.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.toBoard(), BoardUtils.getCoordinateAtPosition("h1"),
                        BoardUtils.getCoordinateAtPosition("f1")));

        assertTrue(t11.moveStatus().isDone());

        final MoveTransition t12 = t11.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.toBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t12.moveStatus().isDone());

        final MoveTransition t13 = t12.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t12.toBoard(), BoardUtils.getCoordinateAtPosition("c4"),
                        BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t13.moveStatus().isDone());

        final MoveTransition t14 = t13.toBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t13.toBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t14.moveStatus().isDone());
        assertTrue(t14.toBoard().getCurrentPlayer().isInCheckMate());

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
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d5"),
                        BoardUtils.getCoordinateAtPosition("h5")));

        assertTrue(t1.moveStatus().isDone());
        assertTrue(t1.toBoard().getCurrentPlayer().isInCheckMate());
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
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("a3"),
                        BoardUtils.getCoordinateAtPosition("b2")));

        assertTrue(t1.moveStatus().isDone());
        assertTrue(t1.toBoard().getCurrentPlayer().isInCheckMate());
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
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("a6"),
                        BoardUtils.getCoordinateAtPosition("a8")));

        assertTrue(t1.moveStatus().isDone());
        assertTrue(t1.toBoard().getCurrentPlayer().isInCheckMate());

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
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("h7"),
                        BoardUtils.getCoordinateAtPosition("e7")));

        assertTrue(t1.moveStatus().isDone());
        assertTrue(t1.toBoard().getCurrentPlayer().isInCheckMate());

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

        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("c6"),
                        BoardUtils.getCoordinateAtPosition("c1")));

        assertTrue(t1.moveStatus().isDone());
        assertTrue(t1.toBoard().getCurrentPlayer().isInCheckMate());

    }


}