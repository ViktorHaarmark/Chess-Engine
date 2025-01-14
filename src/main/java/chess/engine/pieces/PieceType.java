package chess.engine.pieces;


public enum PieceType {

    PAWN("P", 100) {
        @Override
        public boolean isPawn() {
            return true;
        }
    },
    KNIGHT("N", 300) {
        @Override
        public boolean isKnight() {
            return true;
        }
    },
    BISHOP("B", 300) {
        @Override
        public boolean isBishop() {
            return true;
        }
    },
    ROOK("R", 500) {
        @Override
        public boolean isRook() {
            return true;
        }
    },
    QUEEN("Q", 900) {
        @Override
        public boolean isQueen() {
            return true;
        }
    }
    ,
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

    public boolean isKnight() {
        return false;
    }

    public boolean isBishop() {
        return false;
    }

    public boolean isQueen() {
        return false;
    }

    public boolean isPawn() {
        return false;
    }

}

