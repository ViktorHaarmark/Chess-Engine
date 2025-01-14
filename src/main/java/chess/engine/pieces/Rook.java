package chess.engine.pieces;

import chess.Color;
import chess.engine.board.*;
import chess.engine.board.Move.CaptureMove;
import chess.engine.board.Move.MajorPieceMove;

import java.util.ArrayList;
import java.util.List;

import static chess.engine.pieces.PieceType.ROOK;

public class Rook extends Piece {
    private final static int[] DIRECTION = {-8, -1, 1, 8};

    public Rook(final int piecePosition, final Color color, final boolean isFirstMove) {
        super(piecePosition, color, ROOK, isFirstMove);
    }

    public Rook(final int piecePosition, final Color color) {
        super(piecePosition, color, ROOK, false);
    }

    @Override
    public List<Move> calculatePossibleMoves(final Board board) {

        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int direction : DIRECTION) {

            for (int i = 1; i < 8; i++) {
                candidateDestinationCoordinate = this.piecePosition + i * direction;
                if (!isValidRookMove(this.piecePosition, candidateDestinationCoordinate)) {
                    break;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    Move move = new MajorPieceMove(board, this, candidateDestinationCoordinate);
                    if (move.getMoveStatus() == MoveStatus.DONE) {
                        legalMoves.add(move);
                    }
                } else {
                    final Piece pieceOnDestination = candidateDestinationTile.getPiece();
                    if (pieceOnDestination.getPieceColor() != this.color) {
                        Move move = new CaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate);
                        if (move.getMoveStatus() == MoveStatus.DONE) {
                            legalMoves.add(move);
                        }
                    }
                    break;
                }
            }
        }
        return legalMoves;
    }


    @Override
    public String toString() {
        return ROOK.toString();
    }

    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }
}
