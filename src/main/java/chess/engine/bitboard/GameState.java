package chess.engine.bitboard;

public class GameState {
    public int capturedPieceType;
    public int enPassantFile;
    public int castlingRights;
    public int fiftyMoveCounter;

    public static final int clearWhiteKingsideMask = 0b1110;
    public static final int clearWhiteQueensideMask = 0b1101;
    public static final int clearBlackKingsideMask = 0b1011;
    public static final int clearBlackQueensideMask = 0b0111;

    public GameState(int capturedPieceType, int enPassantFile, int castlingRights, int fiftyMoveCounter) {
        this.capturedPieceType = capturedPieceType;
        this.enPassantFile = enPassantFile;
        this.castlingRights = castlingRights;
        this.fiftyMoveCounter = fiftyMoveCounter;
    }

    public boolean HasKingsideCastleRight(boolean isWhite) {
        int mask = isWhite ? 1 : 4;
        return (castlingRights & mask) != 0;
    }

    public boolean HasQueensideCastleRight(boolean isWhite) {
        int mask = isWhite ? 2 : 8;
        return (castlingRights & mask) != 0;
    }

}
