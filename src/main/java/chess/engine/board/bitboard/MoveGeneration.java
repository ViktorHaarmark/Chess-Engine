package chess.engine.board.bitboard;

public class MoveGeneration {
    private static final long[] knightAttacks = new long[64];

    static {
        for (int square = 0; square < 64; square++) {
            knightAttacks[square] = computeKnightMoves(square);
        }
    }
    private static long computeKnightMoves(int square) {
        long bitboard = 1L << square;
        long moves = 0L;

        moves |= (bitboard << 6) & ~0x0303030303030303L;  // Up-left
        moves |= (bitboard << 15) & ~0x0101010101010101L; // Up-right
        moves |= (bitboard << 17) & ~0x8080808080808080L; // Right-up
        moves |= (bitboard << 10) & ~0xC0C0C0C0C0C0C0C0L; // Right-down
        moves |= (bitboard >> 6) & ~0xC0C0C0C0C0C0C0C0L;  // Down-right
        moves |= (bitboard >> 15) & ~0x8080808080808080L; // Down-left
        moves |= (bitboard >> 17) & ~0x0101010101010101L; // Left-down
        moves |= (bitboard >> 10) & ~0x0303030303030303L; // Left-up

        return moves;
    }

    public static long getMoves(int square) {
        return knightAttacks[square];
    }
}
