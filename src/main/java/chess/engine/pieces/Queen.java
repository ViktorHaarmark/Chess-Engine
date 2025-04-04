package chess.engine.pieces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import chess.engine.Players.ai.PieceSquareTables;
import com.google.common.collect.ImmutableList;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.CaptureMove;
import chess.engine.board.Move.MajorPieceMove;
import chess.engine.board.Tile;

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
    public List<Move> calculateLegalMoves(final Board board) {

        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int direction : DIRECTION) {

            for (int i = 1; i < 8; i++) {
                candidateDestinationCoordinate = this.piecePosition + i * direction;
                if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    break;
                }
                if (isRookDirection(direction) && !isRookMove(candidateDestinationCoordinate)) {
                    break;
                }
                if (isBishopDirection(direction) && !isBishopMove(candidateDestinationCoordinate)) {
                    break;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorPieceMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceOnDestination = candidateDestinationTile.getPiece();
                    if (pieceOnDestination.getPieceColor() != this.color) {
                        legalMoves.add(new CaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate));
                    }
                    break;
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    public HashSet<Integer> controlSquares(final HashSet<Integer> nonEmptySquares) {
        HashSet<Integer> controlledSquares = new HashSet<>();
        int candidateDestinationCoordinate;

        for (final int direction : DIRECTION) {
            for (int i = 1; i < 8; i++) {
                candidateDestinationCoordinate = this.piecePosition + i * direction;
                if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate) ||
                        (isRookDirection(direction) && !isRookMove(candidateDestinationCoordinate)) ||
                        (isBishopDirection(direction) && !isBishopMove(candidateDestinationCoordinate))) {
                    break;
                }
                controlledSquares.add(candidateDestinationCoordinate);
                if (nonEmptySquares.contains(candidateDestinationCoordinate)) {
                    break;
                }
            }
        }
        return controlledSquares;
    }

    @Override
    public String toString() {
        return QUEEN.toString();
    }

    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }

    @Override
    protected int[] getPieceSquareTable() {
        return PieceSquareTables.QUEEN_SQUARE_TABLE;
    }


}
