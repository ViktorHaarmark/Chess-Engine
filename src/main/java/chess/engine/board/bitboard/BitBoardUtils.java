package chess.engine.board.bitboard;

public class BitBoardUtils {
    public static long setBit(long bitboard, int square) {
        return bitboard | (1L << square);
    }

    public static long clearBit(long bitboard, int square) {
        return bitboard & ~(1L << square);
    }
}
