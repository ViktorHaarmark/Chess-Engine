package chess.engine.board.bitboard.movegeneration;

public class RookMoves {
    static long[][] rookRays = new long[64][4];

    static {
        computeRookRayMask();
    }

    private static long getRookMoves(int square) {
        return rookRays[square][0] | rookRays[square][1] | rookRays[square][2] | rookRays[square][3];
    }

    private static void computeRookRayMask() {
        for (int square = 0; square < 64; square++) {
            int rank = square / 8;
            int file = square % 8;

            // South ray
            for (int r = rank + 1; r < 8; r++) {
                rookRays[square][0] |= 1L << (r * 8 + file);
            }
            // North ray
            for (int r = rank - 1; r >= 0; r--) {
                rookRays[square][1] |= 1L << (r * 8 + file);
            }
            // East ray
            for (int f = file + 1; f < 8; f++) {
                rookRays[square][2] |= 1L << (rank * 8 + f);
            }
            // West ray
            for (int f = file - 1; f >= 0; f--) {
                rookRays[square][3] |= 1L << (rank * 8 + f);
            }
        }
    }

    private static long getRookAttacks(int square, long allPieces) {
        long attacks = 0L;

        for (int i = 0; i < 4; i++) {
            long ray = rookRays[square][i];
            long blockers = ray & allPieces;

            if (blockers != 0) {
                if (i == 0 | i == 2) { // North or East
                    long firstBlocker = Long.lowestOneBit(blockers);
                    attacks |= ray & (firstBlocker - 1) | firstBlocker;
                }
                else {
                    long firstBlocker = Long.highestOneBit(blockers);
                    attacks |= ray & -(firstBlocker - 1) | firstBlocker;
                }
            } else {
                attacks |= ray;
            }
        }

        return attacks;
    }

    public static long getRookMoves(int square, long friendlyPieces, long enemyPieces) {//TODO: This is identical to bishop rays, so combine them in a general moveGenerator-file
        long occupancy = friendlyPieces | enemyPieces;
        return getRookAttacks(square, occupancy) & ~friendlyPieces;
    }

}
