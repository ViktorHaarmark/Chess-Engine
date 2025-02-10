package chess.engine.pieces;

import chess.Color;
import chess.engine.Players.ai.PieceSquareTables;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.CaptureMove;
import chess.engine.board.Move.MajorPieceMove;
import chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static chess.engine.pieces.PieceType.KING;

public class King extends Piece {

    private final static int[] DIRECTION = {-8, -1, 1, 8, -9, -7, 7, 9};
    private final boolean isKingSideCastleCapable;
    private final boolean isQueenSideCastleCapable;

    public King(final int piecePosition, final Color color, final boolean isKingSideCastleCapable, final boolean isQueenSideCastleCapable) {
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
        final List<Move> possibleMoves = new ArrayList<>();

        for (final int direction : DIRECTION) {

            candidateDestinationCoordinate = this.piecePosition + direction;
            if (!isKingMove(candidateDestinationCoordinate)) {
                continue;
            }
            final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
            if (!candidateDestinationTile.isTileOccupied()) {
                possibleMoves.add(new MajorPieceMove(board, this, candidateDestinationCoordinate));
            } else {
                final Piece pieceOnDestination = candidateDestinationTile.getPiece();
                if (pieceOnDestination.getPieceColor() != this.color) {
                    possibleMoves.add(new CaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate));
                }
            }
        }
        return ImmutableList.copyOf(possibleMoves);
    }

    public HashSet<Integer> controlSquares(final HashSet<Integer> nonEmptySquares) {
        HashSet<Integer> controlledSquares = new HashSet<>();
        int candidateDestinationCoordinate;

        for (final int direction : DIRECTION) {

            candidateDestinationCoordinate = this.piecePosition + direction;
            if (!isKingMove(candidateDestinationCoordinate)) {
                continue;
            }
            controlledSquares.add(candidateDestinationCoordinate);
        }
        return controlledSquares;
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
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false, false);
    }

    @Override
    protected int[] getPieceSquareTable() {
        return PieceSquareTables.KING_SQUARE_TABLE;
    }

}



