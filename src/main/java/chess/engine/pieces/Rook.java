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
        final List<Move> possibleMoves = new ArrayList<>();

        for (final int direction : DIRECTION) {

            for (int i = 1; i < 8; i++) {
                candidateDestinationCoordinate = this.piecePosition + i * direction;
                if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate) || !isRookMove(candidateDestinationCoordinate)) {
                    break;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    possibleMoves.add(new MajorPieceMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceOnDestination = candidateDestinationTile.getPiece();
                    if (pieceOnDestination.getPieceColor() != this.color) {
                        possibleMoves.add(new CaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate));
                    }
                    break;
                }
            }
        }
        return ImmutableList.copyOf(possibleMoves);
    }

    public HashSet<Integer> controlSquares(final HashSet<Integer> nonEmptySquares) {
        HashSet<Integer> controlledSquares = new HashSet<>();
        int candidateDestinationCoordinate;

        for (final int direction : DIRECTION) {
            for (int i = 1; i < 8; i++) {
                candidateDestinationCoordinate = this.piecePosition + i * direction;
                if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate) ||
                        (!isRookMove(candidateDestinationCoordinate))) {
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
        return ROOK.toString();
    }

    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }

    @Override
    protected int[] getPieceSquareTable() {
        return PieceSquareTables.ROOK_SQUARE_TABLE;
    }
}
