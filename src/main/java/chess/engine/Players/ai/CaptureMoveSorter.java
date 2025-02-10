package chess.engine.Players.ai;

import chess.engine.board.Move.CaptureMove;

import java.util.Comparator;

public class CaptureMoveSorter implements Comparator<CaptureMove> {
    public CaptureMoveSorter() {
    }


    @Override
    public int compare(CaptureMove move1, CaptureMove.CaptureMove move2) {
        return getMoveScore(move1) - getMoveScore(move2);
    }


    private int getMoveScore(CaptureMove move) {

        int moveScore = 0;
        int movedPieceValue = move.getMovedPiece().getPieceValue();
        int capturedPieceValue = move.getCapturedPiece().getPieceValue();
        moveScore += capturedPieceValue - movedPieceValue;

        return moveScore;

    }
}
