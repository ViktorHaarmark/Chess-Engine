package chess.engine.bitboard.movegeneration;

public class KingMoves {
    private static final long[] KING_ATTACKS = new long[64];
    private static final long A_FILE_MASK = 0x0101010101010101L;
    private static final long H_FILE_MASK = 0x8080808080808080L;

    static {
        for (int square22 = 0; square22 < 64; square22++) {
            KING_ATTACKS[square22] = computeKingMoves(square22);
        }
    }

    private static long computeKingMoves(int square) {
        long position = 1L << square;
        long moves = 0L;

        moves |= (position >> 7) & ~A_FILE_MASK;  // To the right
        moves |= (position << 1) & ~A_FILE_MASK;
        moves |= (position << 9) & ~A_FILE_MASK;

        moves |= (position << 7) & ~H_FILE_MASK;  // To the left
        moves |= (position >> 1) & ~H_FILE_MASK;
        moves |= (position >> 9) & ~H_FILE_MASK;


        moves |= (position << 8); // Up and down
        moves |= (position >> 8);

        return moves;
    }

    public static long getKingMoves(int square, long friendlyPieces) {
        return KING_ATTACKS[square] & ~friendlyPieces;
    }
}
