package chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.CaptureMove;
import chess.engine.board.Move.MajorPieceMove;
import chess.engine.board.Tile;

public class Bishop extends Piece {

    private final static int[] DIRECTION = {-9, -7, 7, 9};

    public Bishop(final int piecePosition, final Color color, final boolean isFirstMove) {
        super(piecePosition, color, PieceType.BISHOP, isFirstMove);
    }

    public Bishop(final int piecePosition, final Color color) {
        super(piecePosition, color, PieceType.BISHOP, false);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<Move>();

        for (final int direction : DIRECTION) {
            for (int i = 1; i < 8; i++) {
                candidateDestinationCoordinate = this.piecePosition + i * direction;
                if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate) || !isBishopMove(candidateDestinationCoordinate)) {
                    break;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add( new MajorPieceMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceOnDestination = candidateDestinationTile.getPiece();
                    if (pieceOnDestination.getPieceColor() != this.color) {
                        legalMoves.add (new CaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate) );
                    }
                    break; 
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }
    
}

