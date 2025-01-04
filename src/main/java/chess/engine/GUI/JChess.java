package chess.engine.GUI;

import chess.engine.board.Board;
import chess.engine.board.BoardSetup;

public class JChess {
    public static void main(String[] args) {
        Board board = BoardSetup.createStandardBoard();

        System.out.println(board);

        Table.get().show();
    }
    
}
