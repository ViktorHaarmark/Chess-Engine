package chess.engine.pieces;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.CaptureMove;
import chess.engine.board.Move.MajorPieceMove;
import chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece {

    private final static int[] DIRECTION = {-8, -1, 1, 8, -9, -7, 7, 9};
    private final boolean isKingSideCastleCapable;
    private final boolean isQueenSideCastleCapable;
    private final boolean isCastled;

    public King(final int piecePosition, final Color color, final boolean isKingSideCastleCapable, final boolean isQueenSideCastleCapable) {
        super(piecePosition, color, PieceType.KING, false);
        this.isKingSideCastleCapable = isKingSideCastleCapable;
        this.isQueenSideCastleCapable = isQueenSideCastleCapable;
        this.isCastled = false;
    }

    public King(final int piecePosition, final Color color, final boolean isCastled, final boolean isKingSideCastleCapable, final boolean isQueenSideCastleCapable) {
        super(piecePosition, color, PieceType.KING, false);
        this.isKingSideCastleCapable = isKingSideCastleCapable;
        this.isQueenSideCastleCapable = isQueenSideCastleCapable;
        this.isCastled = isCastled;
    }

    public boolean isKingSideCastleCapable() {
        return this.isKingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable() {
        return this.isQueenSideCastleCapable;
    }


    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int direction : DIRECTION) {

            candidateDestinationCoordinate = this.piecePosition + direction;
            if (!isKingMove(candidateDestinationCoordinate)) {
                continue;
            }
            final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
            if (!candidateDestinationTile.isTileOccupied()) {
                legalMoves.add(new MajorPieceMove(board, this, candidateDestinationCoordinate));
            } else {
                final Piece pieceOnDestination = candidateDestinationTile.getPiece();
                if (pieceOnDestination.getPieceColor() != this.color) {
                    legalMoves.add(new CaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate));
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }


    public boolean isKingMove(final int coordinate) {
        return BoardUtils.isValidTileCoordinate(coordinate) && BoardUtils.rowDifference(coordinate, this.piecePosition) + BoardUtils.columnDifference(coordinate, this.piecePosition) <= 2;
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }


    @Override
    public King movePiece(Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), move.isCastlingMove(), false, false);
    }

}



