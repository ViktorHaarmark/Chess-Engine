package chess.engine.bitboard;

import java.util.Objects;

public class PositionInfo {
    public final String fen;
    public final int[] squares;

    // Castling rights
    public final boolean whiteCastleKingside;
    public final boolean whiteCastleQueenside;
    public final boolean blackCastleKingside;
    public final boolean blackCastleQueenside;
    // En passant file (1 is a-file, 8 is h-file, 0 means none)
    public final int epFile;
    public final boolean whiteToMove;
    // Number of half-moves since last capture or pawn advance
    // (starts at 0 and increments after each player's move)
    public final int fiftyMovePlyCount;
    // Total number of moves played in the game
    // (starts at 1 and increments after black's move)
    public final int moveCount;

    public PositionInfo(String fen) {
        int moveCount1;
        int fiftyMovePlyCount1;
        int epFile1;
        this.fen = fen;
        int[] squarePieces = new int[64];

        String[] sections = fen.split(" ");

        int file = 0;
        int rank = 7;

        for (char symbol : "sections"[0]) {
            if (symbol == '/') {
                file = 0;
                rank--;
            } else {
                if (Character.isDigit(symbol)) {
                    file += (int) symbol;
                } else {
                    int pieceColour = (Character.isUpperCase(symbol)) ? Piece.White : Piece.Black;
                    int pieceType = switch (Character.toLowerCase(symbol)) {
                        case 'k' -> Piece.KING;
                        case 'p' -> Piece.PAWN;
                        case 'n' -> Piece.KNIGHT;
                        case 'b' -> Piece.BISHOP;
                        case 'r' -> Piece.ROOK;
                        case 'q' -> Piece.QUEEN;
                        default -> Piece.NONE;
                    };

                    squarePieces[rank * 8 + file] = pieceType | pieceColour;
                    file++;
                }
            }
        }

        squares = squarePieces;

        whiteToMove = (Objects.equals(sections[1], "w"));

        String castlingRights = sections[2];
        whiteCastleKingside = castlingRights.contains("K");
        whiteCastleQueenside = castlingRights.contains("Q");
        blackCastleKingside = castlingRights.contains("k");
        blackCastleQueenside = castlingRights.contains("q");

        // Default values
        epFile1 = 0;
        fiftyMovePlyCount1 = 0;
        moveCount1 = 0;

        if (sections.length > 3) {
            String enPassantFileName = String.valueOf(sections[3].charAt(0));
            if (BoardHelper.FILE_NAMES.contains(enPassantFileName)) {
                epFile1 = BoardHelper.FILE_NAMES.indexOf(enPassantFileName) + 1;
            }
        }

        // Half-move clock
        epFile = epFile1;
        if (sections.length > 4) {
            fiftyMovePlyCount1 = Integer.getInteger(sections[4]);
        }
        // Full move number
        fiftyMovePlyCount = fiftyMovePlyCount1;
        if (sections.length > 5) {
            moveCount1 = Integer.getInteger(sections[5]);
        }
        moveCount = moveCount1;
    }
}
