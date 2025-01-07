package chess.engine.Players.ai;

import chess.engine.board.Move;
import chess.engine.pieces.Piece;

import java.util.Comparator;

public class MoveSorter implements Comparator<Move> {

    public MoveSorter() {
    }


    @Override
    public int compare(Move move1, Move move2) {
        return getMoveScore(move1) - getMoveScore(move2);
    }


    private int getMoveScore(Move move) {

        int moveScore = 0;
        Piece piece = move.getMovedPiece();
        if (move.isCapture()) {
            moveScore += 2 * (move.getCapturedPiece().getPieceValue() - piece.getPieceValue());
        }
//        if (move.isPromotionMove()) {
//            moveScore += move.getPromotedPiece().getPieceValue();
//        }
//        if (move.execute().currentPlayer().isInCheck()) {
//            moveScore += 50;
//        }
        return moveScore;

    }
}
