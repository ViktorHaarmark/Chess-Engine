package chess.engine.pieces;


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


    private final String pieceName;
    private final int pieceValue;

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

