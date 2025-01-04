package chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.board.Tile;
import chess.engine.board.Move.CaptureMove;
import chess.engine.board.Move.MajorPieceMove;
import chess.engine.board.BoardUtils;

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int piecePosition, final Color color, final boolean isFirstMove) {
        super(piecePosition, color, PieceType.KNIGHT, isFirstMove);
    }

    public Knight(final int piecePosition, final Color color) {
        super(piecePosition, color, PieceType.KNIGHT, false);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<Move>();

        for (final int currentMoveOffset : CANDIDATE_MOVE_COORDINATES) {

            candidateDestinationCoordinate = this.piecePosition + currentMoveOffset;

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate) && isValidKnightMove(currentMoveOffset, candidateDestinationCoordinate) ) { 

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add( new MajorPieceMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceOnDestination = candidateDestinationTile.getPiece();
                    if (pieceOnDestination.getPieceColor() != this.color) {
                        legalMoves.add (new CaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate) );
                    } 
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private boolean isValidKnightMove(int candidateOffset, int candidateDestinationCoordinate) { //Can be implemented in a different way, see video 5
        if (Math.abs(candidateOffset) == 6 || Math.abs(candidateOffset) == 10 ) {
            if (BoardUtils.rowDifference(candidateDestinationCoordinate, this.piecePosition) == 1 && BoardUtils.columnDifference(candidateDestinationCoordinate, this.piecePosition) == 2) {
                return true;
            }
        }

        if (Math.abs(candidateOffset) == 15 || Math.abs(candidateOffset) == 17 ) {
            if (BoardUtils.rowDifference(candidateDestinationCoordinate, this.piecePosition) == 2 && BoardUtils.columnDifference(candidateDestinationCoordinate, this.piecePosition) == 1 ) {
                return true;
            }
        }
    return false;
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }

}
