package chess.engine.pieces;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.CaptureMove;
import chess.engine.board.Move.MajorPieceMove;
import chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.List;

import static chess.engine.pieces.PieceType.KNIGHT;

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int piecePosition, final Color color, final boolean isFirstMove) {
        super(piecePosition, color, KNIGHT, isFirstMove);
    }

    public Knight(final int piecePosition, final Color color) {
        super(piecePosition, color, KNIGHT, false);
    }

    @Override
    public List<Move> calculatePossibleMoves(final Board board) {

        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentMoveOffset : CANDIDATE_MOVE_COORDINATES) {

            candidateDestinationCoordinate = this.piecePosition + currentMoveOffset;

            if (isValidKnightMove(currentMoveOffset, candidateDestinationCoordinate)) {

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (!candidateDestinationTile.isTileOccupied()) {
                    Move move = new MajorPieceMove(board, this, candidateDestinationCoordinate);
                    if (move.getMoveStatus().isDone()) {
                        legalMoves.add(move);
                    }
                } else {
                    final Piece pieceOnDestination = candidateDestinationTile.getPiece();
                    if (pieceOnDestination.getPieceColor() != this.color) {
                        Move move = new CaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate);
                        if (move.getMoveStatus().isDone()) {
                            legalMoves.add(move);
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    public static boolean isValidKnightMove(int candidateOffset, int candidateDestinationCoordinate) {
        if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
            int piecePosition = candidateDestinationCoordinate - candidateOffset;
            if (Math.abs(candidateOffset) == 6 || Math.abs(candidateOffset) == 10) {
                if (BoardUtils.rowDifference(candidateDestinationCoordinate, piecePosition) == 1 && BoardUtils.columnDifference(candidateDestinationCoordinate, piecePosition) == 2) {
                    return true;
                }
            }

            if (Math.abs(candidateOffset) == 15 || Math.abs(candidateOffset) == 17) {
                return BoardUtils.rowDifference(candidateDestinationCoordinate, piecePosition) == 2 && BoardUtils.columnDifference(candidateDestinationCoordinate, piecePosition) == 1;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return KNIGHT.toString();
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }

}
