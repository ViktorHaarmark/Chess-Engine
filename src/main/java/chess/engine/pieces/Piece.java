package chess.engine.pieces;

import java.util.Collection;

import chess.Color;
import chess.engine.board.*;

public abstract class Piece {
    
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Color color;
    private boolean isFirstMove;
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
        if(this == other) {
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return (color == otherPiece.getPieceColor() && pieceType == otherPiece.getPieceType() && piecePosition == otherPiece.getPiecePosition());
    }

    @Override
    public int hashCode() {
        return cachedHashCode;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

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

    public enum PieceType {

        PAWN("P", 100),
        KNIGHT("N", 300),
        BISHOP("B", 300),
        ROOK("R", 500) {
            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q", 900),
        KING("K", 100000) {
            @Override
            public boolean isKing() {
                return true;
            }
        };


        private String pieceName;
        private int pieceValue;

        PieceType(final String pieceName, final int pieceValue) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public int getPieceValue() {
            return this.pieceValue;
        }

        public boolean isKing() {
            return false;
        }

        public boolean isRook() {
            return false;
        }

    }
    

}
