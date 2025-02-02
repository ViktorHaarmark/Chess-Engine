package chess.engine.GUI.Lichess;

import chess.Color;
import chess.engine.GUI.Lichess.Events.LichessEvent;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.MoveTransition;
import chess.pgn.FenUtility;

public class LichessUtility {
    public final static String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    LichessUtility() {
        throw new RuntimeException("Not instantiable");
    }

    public static Board makeBoardFromUCIMove(Board board, String uci) {
        String startingSquare = uci.substring(0, 2);
        String endingSquare = uci.substring(2, 4);
        String promotionPiece = null;
        if (uci.length() == 5) {
            promotionPiece = uci.substring(4);
        }
        MoveTransition moveTransition = board.currentPlayer().makeMove(Move.MoveFactory.createLichessMove(board,
                BoardUtils.getCoordinateAtPosition(startingSquare),
                BoardUtils.getCoordinateAtPosition(endingSquare),
                promotionPiece));
        return moveTransition.getToBoard();
    }

    public static String getFenFromMoves(String[] moves, String initialfen) {
        if (moves == null || moves.length == 0 ) {
            return (initialfen.equals("startpos")) ? STARTING_POSITION : initialfen;
        } else {
            Board board = FenUtility.createGameFromFEN(initialfen);
            for (String move : moves) {
                board = makeBoardFromUCIMove(board, move);
            }
            return FenUtility.createFENFromGame(board);
        }
    }

    public static Color getBotColor(LichessEvent event) {
        String color = event.getGame().getMyColor();
        return (color.equals("white") ? Color.WHITE : Color.BLACK);
    }

}
