package chess.engine.bitboard;

public class BitBoardUtils {
    public static int WHITE = 0;
    public static int BLACK = 1;
    public static int PAWNS = 0;
    public static int KNIGHTS = 1;
    public static int BISHOPS = 2;
    public static int ROOKS = 3;
    public static int QUEENS = 4;
    public static int KINGS = 5;

    public static long setBit(long bitboard, int square) {
        return bitboard | (1L << square);
    }

    public static long clearBit(long bitboard, int square) {
        return bitboard & ~(1L << square);
    }
}
