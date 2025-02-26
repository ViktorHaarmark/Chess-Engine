package chess.engine.bitboard.movegeneration;

public class BishopMoves {
    static long[][] bishopsRays = new long[64][4];

    static {
        computeBishopRayMask();
    }

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

    private static long getBishopAttacks(int square, long allPieces) {
        long attacks = 0L;

        for (int i = 0; i < 4; i++) {
            long ray = bishopsRays[square][i];
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

    public static long getBishopMoves(int square, long friendlyPieces, long enemyPieces) {
        long occupancy = friendlyPieces | enemyPieces;
        long attacks = getBishopAttacks(square, occupancy);
        return attacks & ~friendlyPieces;
    }


}
