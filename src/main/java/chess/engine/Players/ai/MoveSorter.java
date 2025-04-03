package chess.engine.Players.ai;

import chess.engine.board.Move;

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
        moveScore += move.getMovedPiece().getPieceValue();

//        int movedPieceValue = move.getMovedPiece().getPieceValue();
//        int capturedPieceValue = 0;
//        if (move.isCapture()) {
//            capturedPieceValue = move.getCapturedPiece().getPieceValue();
//        }
//        if (move.isCapture()) {
//            moveScore += 10 * capturedPieceValue - movedPieceValue;
//        }
//        if (move.isPromotionMove()) {
//            moveScore += move.getPromotedPiece().getPieceValue();
//        }
//        if (move.execute().getCurrentPlayer().isInCheck()) {
//            moveScore += 100;
//        }
        return moveScore;

    }
}
