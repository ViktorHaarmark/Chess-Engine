package chess.engine.bitboard.movegeneration;

public class KnightMoves {
    private static final long[] KNIGHT_ATTACKS = new long[64];
    private static final long LEFT_2_MASK = 0x0303030303030303L;
    private static final long LEFT_1_MASK = 0x0101010101010101L;
    private static final long RIGHT_1_MASK = 0xC0C0C0C0C0C0C0C0L;
    private static final long RIGHT_2_MASK = 0x8080808080808080L;

    static {
        for (int square = 0; square < 64; square++) {
            KNIGHT_ATTACKS[square] = computeKnightMoves(square);
        }
    }

    private static long computeKnightMoves(int square) {
        long bitboard = 1L << square;
        long moves = 0L;

        moves |= (bitboard << 6) & ~RIGHT_1_MASK;  // Up-left
        moves |= (bitboard << 15) & ~RIGHT_2_MASK; // Up-right
        moves |= (bitboard << 17) & ~LEFT_1_MASK; // Right-up
        moves |= (bitboard << 10) & ~LEFT_2_MASK; // Right-down
        moves |= (bitboard >> 6) & ~LEFT_2_MASK;  // Down-right
        moves |= (bitboard >> 15) & ~LEFT_1_MASK; // Down-left
        moves |= (bitboard >> 17) & ~RIGHT_2_MASK; // Left-down
        moves |= (bitboard >> 10) & ~RIGHT_1_MASK; // Left-up

        return moves;
    }

    public static long getKnightMoves(int square, long friendlyPieces) {
        return KNIGHT_ATTACKS[square] & ~friendlyPieces;
    }
}

