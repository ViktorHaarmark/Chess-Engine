package chess.engine.bitboard.movegeneration;

public class SlidingMoves {
    static long[][] rookRays = new long[64][4];
    static long[][] bishopsRays = new long[64][4];

    static {
        computeBishopRayMask();
        computeRookRayMask();
    }

    // region BishopMoves

    private static void computeBishopRayMask() {
        for (int square = 0; square < 64; square++) {
            int rank = square / 8;
            int file = square % 8;

            // Northeast ray
            for (int r = rank + 1, f = file + 1; r < 8 && f < 8; r++, f++) {
                bishopsRays[square][0] |= 1L << (r * 8 + f);
            }
            // Northwest ray
            for (int r = rank + 1, f = file - 1; r < 8 && f >= 0; r++, f--) {
                bishopsRays[square][1] |= 1L << (r * 8 + f);
            }
            // Southeast ray
            for (int r = rank - 1, f = file + 1; r >= 0 && f < 8; r--, f++) {
                bishopsRays[square][2] |= 1L << (r * 8 + f);
            }
            // Southwest ray
            for (int r = rank - 1, f = file - 1; r >= 0 && f >= 0; r--, f--) {
                bishopsRays[square][3] |= 1L << (r * 8 + f);
            }
        }
    }

    public static long getBishopMoves(int square, long friendlyPieces, long enemyPieces) {
        long occupancy = friendlyPieces | enemyPieces;
        long attacks = getSlidingAttacks(square, occupancy, true, false);
        return attacks & ~friendlyPieces;
    }

    // endregion BishopMoves

    // region RookMoves

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

    public static long getRookMoves(int square, long friendlyPieces, long enemyPieces) {
        long occupancy = friendlyPieces | enemyPieces;
        return getSlidingAttacks(square, occupancy, false, true) & ~friendlyPieces;
    }
// endregion RookMoves

    public static long getQueenMoves(int square, long friendlyPieces, long enemyPieces) {
        long occupancy = friendlyPieces | enemyPieces;
        return getSlidingAttacks(square, occupancy, true, true);
    }

    private static long getSlidingAttacks(int square, long allPieces, boolean bishopMoves, boolean rookMoves) {
        long attacks = 0L;

        for (int i = 0; i < 4; i++) {
            if (bishopMoves) {
                long ray = bishopsRays[square][i];
                long blockers = ray & allPieces;

                if (blockers != 0) {
                    if (i == 0 | i == 2) { // North or East
                        long firstBlocker = Long.lowestOneBit(blockers);
                        attacks |= ray & (firstBlocker - 1) | firstBlocker;
                    } else {
                        long firstBlocker = Long.highestOneBit(blockers);
                        attacks |= ray & -(firstBlocker - 1) | firstBlocker;
                    }
                } else {
                    attacks |= ray;
                }
            }
            if (rookMoves) {
                long ray = rookRays[square][i];
                long blockers = ray & allPieces;

                if (blockers != 0) {
                    if (i == 0 | i == 2) { // North or East
                        long firstBlocker = Long.lowestOneBit(blockers);
                        attacks |= ray & (firstBlocker - 1) | firstBlocker;
                    } else {
                        long firstBlocker = Long.highestOneBit(blockers);
                        attacks |= ray & -(firstBlocker - 1) | firstBlocker;
                    }
                } else {
                    attacks |= ray;
                }
            }
        }

        return attacks;
    }
}
