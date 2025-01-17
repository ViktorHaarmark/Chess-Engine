package chess.engine.pieces;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;

import java.util.HashSet;
import java.util.List;

public abstract class Piece {

    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Color color;
    private final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final int piecePosition, final Color color, final PieceType pieceType, final boolean isFirstMove) {
        this.piecePosition = piecePosition;
        this.color = color;
        this.pieceType = pieceType;
        this.isFirstMove = isFirstMove;

        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + color.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + pieceType.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece otherPiece)) {
            return false;
        }
        return (color == otherPiece.getPieceColor() && pieceType == otherPiece.getPieceType() && piecePosition == otherPiece.getPiecePosition());
    }

    @Override
    public int hashCode() {
        return cachedHashCode;
    }

    public abstract List<Move> calculateLegalMoves(final Board board);

    public abstract HashSet<Integer> controlSquares(final HashSet<Integer> nonEmptySquares);

    public abstract Piece movePiece(Move move);

    public Color getPieceColor() {
        return this.color;
    }

    protected boolean isBishopMove(int tileCoordinate) {
        return BoardUtils.rowDifference(tileCoordinate, this.piecePosition) == BoardUtils.columnDifference(tileCoordinate, this.piecePosition);
    }

    protected boolean isRookMove(int tileCoordinate) {
        return (BoardUtils.rowDifference(tileCoordinate, this.piecePosition) == 0 || BoardUtils.columnDifference(tileCoordinate, this.piecePosition) == 0);
    }

    public int getPiecePosition() {
        return piecePosition;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public int getPieceValue() {
        return this.getPieceType().getPieceValue();
    }

    protected boolean isRookDirection(int direction) {
        return direction == -8 || direction == -1 || direction == 1 || direction == 8;
    }

    protected boolean isBishopDirection(int direction) {
        return direction == -9 || direction == -7 || direction == 7 || direction == 9;
    }

    protected abstract int[] getPieceSquareTable();

    public int getPiecePositionValue() {
        if (this.getPieceColor().isWhite()) {
            return getPieceSquareTable()[this.getPiecePosition()];
        } else {
            return getPieceSquareTable()[63 - this.getPiecePosition()]; //TODO: spejlvendt lige nu, er det et problem?
        }
    }
}
