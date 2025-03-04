package chess.engine.bitboard;

public class Piece {
    // Piecetype
    public static final int NONE = 0;
    public static final int PAWN = 1;
    public static final int KNIGHT = 2;
    public static final int BISHOP = 3;
    public static final int ROOK = 4;
    public static final int QUEEN = 5;
    public static final int KING = 6;

    // Piece Color

    public static final int White = 0;
    public static final int Black = 8;

    // Pieces
    public static final int WhitePawn = PAWN | White; // 1
    public static final int WhiteKnight = KNIGHT | White; // 2
    public static final int WhiteBishop = BISHOP | White; // 3
    public static final int WhiteRook = ROOK | White; // 4
    public static final int WhiteQueen = QUEEN | White; // 5
    public static final int WhiteKing = KING | White; // 6

    public static final int BlackPawn = PAWN | Black; // 9
    public static final int BlackKnight = KNIGHT | Black; // 10
    public static final int BlackBishop = BISHOP | Black; // 11
    public static final int BlackRook = ROOK | Black; // 12
    public static final int BlackQueen = QUEEN | Black; // 13
    public static final int BlackKing = KING | Black; // 14

    public static final int MaxPieceIndex = BlackKing;
    public static int[] PieceIndices = {
            WhitePawn, WhiteKnight, WhiteBishop, WhiteRook, WhiteQueen, WhiteKing,
            BlackPawn, BlackKnight, BlackBishop, BlackRook, BlackQueen, BlackKing
    };

    // Bit Masks
    private static final int typeMask = 0b0111;
    private static final int colorMask = 0b1000;

    public static int makePiece(int pieceType, int pieceColor) {
        if (pieceType <= KING && (pieceColor == 0 || pieceColor == 8)) {
            return pieceType | pieceColor;
        }
        System.out.println("CANNOT MAKE THIS PIECE"); //TODO: Make some exceptions for bugfixing
        return 0;
    }

    public static int makePiece(int pieceType, boolean pieceIsWhite) {
        int color = pieceIsWhite ? 0 : 8;
        if (pieceType <= KING) {
            return pieceType | color;
        }
        System.out.println("CANNOT MAKE THIS PIECE"); //TODO: Make some exceptions for bugfixing
        return 0;
    }

    public static boolean isColor(int piece, int color) {
        return (piece & colorMask) == color && piece != 0;
    }

    public static boolean isWhite(int piece) {
        return isColor(piece, White);
    }

    public static int getPieceColor(int piece) {
        return (piece & colorMask);
    }

    public static int getPieceType(int piece) {
        return (piece & typeMask);
    }

    // Rook or Queen
    public static boolean isOrthogonalSlider(int piece) {
        int pieceType = getPieceType(piece);
        return (pieceType == ROOK || pieceType == QUEEN);
    }

    // Bishop or Queen
    public static boolean IsDiagonalSlider(int piece) {
        int pieceType = getPieceType(piece);
        return (pieceType == BISHOP || pieceType == QUEEN);
    }

    // Bishop, Rook, or Queen
    public static boolean IsSlidingPiece(int piece) {
        int pieceType = getPieceType(piece);
        return (pieceType == ROOK || pieceType == QUEEN || pieceType == BISHOP);
    }

    public static char getPieceSymbol(int piece) {
        int pieceType = getPieceType(piece);
        char symbol = switch (pieceType) {
            case ROOK -> 'R';
            case KNIGHT -> 'N';
            case BISHOP -> 'B';
            case QUEEN -> 'Q';
            case KING -> 'K';
            case PAWN -> 'P';
            default -> ' ';
        };
        symbol = isWhite(piece) ? symbol : Character.toLowerCase(symbol);
        return symbol;
    }

    public static int GetPieceTypeFromSymbol(char symbol) {
        symbol = Character.toUpperCase(symbol);
        return switch (symbol) {
            case 'R' -> ROOK;
            case 'N' -> KNIGHT;
            case 'B' -> BISHOP;
            case 'Q' -> QUEEN;
            case 'K' -> KING;
            case 'P' -> PAWN;
            case ' ' -> NONE;
            default -> NONE; //TODO: Should throw an exception
        };
    }

}

