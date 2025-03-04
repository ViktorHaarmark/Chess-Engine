package chess.engine.bitboard;

public class BoardHelper {

    public final static Coordinate[] ROOK_DIRECTIONS = {
            new Coordinate(-1, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(0, -1)
    };
    public final static Coordinate[] BISHOP_DIRECTIONS = {
            new Coordinate(-1, 1), new Coordinate(1, 1), new Coordinate(1, -1), new Coordinate(-1, -1)
    };

    public final static String FILE_NAMES = "abcdefgh";
    public final static String RANK_NAMES = "12345678";

    public final static int a1 = 0;
    public final static int b1 = 1;
    public final static int c1 = 2;
    public final static int d1 = 3;
    public final static int e1 = 4;
    public final static int f1 = 5;
    public final static int g1 = 6;
    public final static int h1 = 7;

    public final static int a8 = 56;
    public final static int b8 = 57;
    public final static int c8 = 58;
    public final static int d8 = 59;
    public final static int e8 = 60;
    public final static int f8 = 61;
    public final static int g8 = 62;
    public final static int h8 = 63;


    // Rank (0 to 7) of square
    public static int rankIndex(int squareIndex) {
        return squareIndex >> 3;
    }

    // File (0 to 7) of square
    public static int fileIndex(int squareIndex) {
        return squareIndex & 0b000111;
    }

    public static int indexFromCoord(int fileIndex, int rankIndex) {
        return rankIndex * 8 + fileIndex;
    }

    public static int indexFromCoord(Coordinate coordinate) {
        return indexFromCoord(coordinate.fileIndex, coordinate.rankIndex);
    }

    public static Coordinate coordFromIndex(int squareIndex) {
        return new Coordinate(fileIndex(squareIndex), rankIndex(squareIndex));
    }

    public static boolean lightSquare(int fileIndex, int rankIndex) {
        return (fileIndex + rankIndex) % 2 != 0;
    }

    public static boolean lightSquare(int squareIndex) {
        return lightSquare(fileIndex(squareIndex), rankIndex(squareIndex));
    }

    public static String squareNameFromCoordinate(int fileIndex, int rankIndex) {
        return FILE_NAMES.charAt(fileIndex) + "" + (rankIndex + 1);
    }

    public static String squareNameFromIndex(int squareIndex) {
        return squareNameFromCoordinate(coordFromIndex(squareIndex));
    }

    public static String squareNameFromCoordinate(Coordinate coordinate) {
        return squareNameFromCoordinate(coordinate.fileIndex, coordinate.rankIndex);
    }

    public static int squareIndexFromName(String name) {
        char fileName = name.charAt(0);
        char rankName = name.charAt(1);
        int fileIndex = FILE_NAMES.indexOf(fileName);
        int rankIndex = RANK_NAMES.indexOf(rankName);
        return indexFromCoord(fileIndex, rankIndex);
    }

    public static boolean isValidCoordinate(int x, int y) {
        return (x >= 0 && x < 8 && y >= 0 && y < 8);
    }

//    /// <summary>
//    /// Creates an ASCII-diagram of the current position.
//    /// </summary>
//    public static String CreateDiagram(Board board, bool blackAtTop =true, bool includeFen =true, bool includeZobristKey =true) {
//        // Thanks to ernestoyaquello
//        System.Text.StringBuilder result = new ();
//        int lastMoveSquare = board.AllGameMoves.Count > 0 ? board.AllGameMoves[ ^ 1].TargetSquare:
//        -1;
//
//        for (int y = 0; y < 8; y++) {
//            int rankIndex = blackAtTop ? 7 - y : y;
//            result.AppendLine("+---+---+---+---+---+---+---+---+");
//
//            for (int x = 0; x < 8; x++) {
//                int fileIndex = blackAtTop ? x : 7 - x;
//                int squareIndex = indexFromCoord(fileIndex, rankIndex);
//                bool highlight = squareIndex == lastMoveSquare;
//                int piece = board.Square[squareIndex];
//                if (highlight) {
//                    result.Append($"|({Piece.GetSymbol(piece)})");
//                } else {
//                    result.Append($"| {Piece.GetSymbol(piece)} ");
//                }
//
//
//                if (x == 7) {
//                    // Show rank number
//                    result.AppendLine($"| {rankIndex + 1}");
//                }
//            }
//
//            if (y == 7) {
//                // Show file names
//                result.AppendLine("+---+---+---+---+---+---+---+---+");
//					final static string fileNames = "  a   b   c   d   e   f   g   h  ";
//					final static string fileNamesRev = "  h   g   f   e   d   c   b   a  ";
//                result.AppendLine(blackAtTop ? fileNames : fileNamesRev);
//                result.AppendLine();
//
//                if (includeFen) {
//                    result.AppendLine($"Fen         : {FenUtility.CurrentFen(board)}");
//                }
//                if (includeZobristKey) {
//                    result.AppendLine($"Zobrist Key : {board.ZobristKey}");
//                }
//            }
//        }
//
//        return result.ToString();
//    }
}
