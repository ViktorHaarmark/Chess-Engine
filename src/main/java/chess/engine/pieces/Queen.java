package chess.engine.pieces;

import chess.Color;
import chess.engine.board.*;
import chess.engine.board.Move.CaptureMove;
import chess.engine.board.Move.MajorPieceMove;

import java.util.ArrayList;
import java.util.List;

import static chess.engine.pieces.PieceType.QUEEN;

public class Queen extends Piece {
    private final static int[] DIRECTION = {-8, -1, 1, 8, -9, -7, 7, 9}; //First 4 is rook moves, next 4 is bishop moves

    public Queen(final int piecePosition, final Color color, final boolean isFirstMove) {
        super(piecePosition, color, QUEEN, isFirstMove);
    }

    public Queen(final int piecePosition, final Color color) {
        super(piecePosition, color, QUEEN, false);
    }

    @Override
    public List<Move> calculatePossibleMoves(final Board board) {

        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int direction : DIRECTION) {

            for (int i = 1; i < 8; i++) {
                candidateDestinationCoordinate = this.piecePosition + i * direction;
                if (isRookDirection(direction) && !isValidRookMove(this.piecePosition, candidateDestinationCoordinate)) {
                    break;
                }
                if (isBishopDirection(direction) && !isValidBishopMove(this.piecePosition, candidateDestinationCoordinate)) {
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
        return QUEEN.toString();
    }

    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }


}
