package chess.engine.pieces;

import chess.Color;
import chess.engine.board.*;
import chess.engine.board.Move.CaptureMove;
import chess.engine.board.Move.MajorPieceMove;

import java.util.ArrayList;
import java.util.List;

import static chess.engine.pieces.PieceType.KING;

public class King extends Piece {

    public final static int[] DIRECTION = {-8, -1, 1, 8, -9, -7, 7, 9};
    private final boolean isKingSideCastleCapable;
    private final boolean isQueenSideCastleCapable;


    public King(final int piecePosition, final Color color, final boolean isKingSideCastleCapable, final boolean isQueenSideCastleCapable) {
        super(piecePosition, color, KING, false);
        this.isKingSideCastleCapable = isKingSideCastleCapable;
        this.isQueenSideCastleCapable = isQueenSideCastleCapable;
    }

    public King(final int piecePosition, final Color color, final boolean isCastled, final boolean isKingSideCastleCapable, final boolean isQueenSideCastleCapable) {
        super(piecePosition, color, KING, false);
        this.isKingSideCastleCapable = isKingSideCastleCapable;
        this.isQueenSideCastleCapable = isQueenSideCastleCapable;
    }

    public boolean isKingSideCastleCapable() {
        return this.isKingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable() {
        return this.isQueenSideCastleCapable;
    }

    @Override
    public List<Move> calculatePossibleMoves(final Board board) {

        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int direction : DIRECTION) {

            candidateDestinationCoordinate = this.piecePosition + direction;
            if (!isKingMove(candidateDestinationCoordinate)) {
                continue;
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
            }
        }
        return legalMoves;
    }


    public boolean isKingMove(final int coordinate) {
        return BoardUtils.isValidTileCoordinate(coordinate) && BoardUtils.rowDifference(coordinate, this.piecePosition) + BoardUtils.columnDifference(coordinate, this.piecePosition) <= 2;
    }

    @Override
    public String toString() {
        return KING.toString();
    }


    @Override
    public King movePiece(Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), move.isCastlingMove(), false, false);
    }

}



