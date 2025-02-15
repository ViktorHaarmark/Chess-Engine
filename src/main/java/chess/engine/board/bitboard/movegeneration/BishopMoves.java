package chess.engine.board.bitboard.movegeneration;

import java.util.Random;

public class BishopMoves {
    private static final long[] BISHOP_ATTACK_MASKS = new long[64];
    private static final long[][] BISHOP_LOOKUP_TABLE = new long[64][];
    private static final long[] BISHOP_MAGIC_NUMBERS = new long[64];
    private static final int[] BISHOP_SHIFTS = new int[64];
    private static final int SEED = 1000;

    static {
        for (int square = 0; square < 64; square++) {
            BISHOP_ATTACK_MASKS[square] = computeBishopAttackMask(square);
            BISHOP_MAGIC_NUMBERS[square] = findMagicNumber(square);

        }
        calculateBishopShifts();
        generateBishopLookupTable();
    }

    private static void calculateBishopShifts() {
        for (int square = 0; square < 64; square++) {
            long attackMask = BISHOP_ATTACK_MASKS[square];
            BISHOP_SHIFTS[square] = 64 - Long.bitCount(attackMask);
        }
    }

    private static long computeBishopAttackMask(int square) {
        long attacks = 0L;
        int[] directions = {9, 7, -9, -7};  // Diagonal directions

        for (int direction : directions) {
            int targetSquare = square;
            while (true) {
                targetSquare += direction;
                if (targetSquare < 0 || targetSquare >= 64 || isEdgeWraparound(square, targetSquare)) {
                    break;  // Stop at board edges
                }
                attacks |= (1L << targetSquare);
            }
        }
        return attacks;
    }

    private static boolean isEdgeWraparound(int from, int to) {
        int fromFile = from % 8, toFile = to % 8;
        return Math.abs(fromFile - toFile) > 1;
    }

    private static long getBlockersFromIndex(int index, long attackMask) {
        long blockers = 0L;
        int bitPosition = 0;
        for (long bit = attackMask; bit != 0; bit &= (bit - 1)) {
            if ((index & (1 << bitPosition)) != 0) {
                blockers |= (bit & -bit);
            }
            bitPosition++;
        }
        return blockers;
    }

    private static int computeMagicIndex(long blockers, long magicNumber, int shift) {
        return (int) ((blockers * magicNumber)) >>> shift;
    }

    private static void generateBishopLookupTable() {
        for (int square = 0; square < 64; square++) {
            long attackMask = computeBishopAttackMask(square);
            int numBlockers = Long.bitCount(attackMask);
            int tableSize = 1 << numBlockers; // 2^N possible configurations
            BISHOP_LOOKUP_TABLE[square] = new long[tableSize];

            for (int i = 0; i < tableSize; i++) {
                long blockers = getBlockersFromIndex(i, attackMask);
                int index = computeMagicIndex(blockers, BISHOP_MAGIC_NUMBERS[square], BISHOP_SHIFTS[square]);
                BISHOP_LOOKUP_TABLE[square][index] = computeBishopMoves(square, blockers);
            }
        }
    }

    public static long getBishopMoves(int square, long occupied) {
        long blockers = occupied & BISHOP_ATTACK_MASKS[square]; // Extract blockers
        int index = computeMagicIndex(blockers, BISHOP_MAGIC_NUMBERS[square], BISHOP_SHIFTS[square]); // Compute lookup index
        return BISHOP_LOOKUP_TABLE[square][index];
    }

    private static long findMagicNumber(int square) {
        long attackMask = BISHOP_ATTACK_MASKS[square];
        int numBlockers = Long.bitCount(attackMask);
        int tableSize = 1 << numBlockers;
        Random random = new Random(SEED);

        while (true) {
            long candidate = random.nextLong() & random.nextLong() & random.nextLong();
            boolean[] used = new boolean[tableSize];
            boolean valid = true;

            for (int i = 0; i < tableSize; i++) {
                long blockers = getBlockersFromIndex(i, attackMask);
                int index = computeMagicIndex(blockers, candidate, 64 - numBlockers);
                if (used[index]) {
                    valid = false;
                    break;
                }
                used[index] = true;
            }
            if (valid) return candidate;
        }
    }

    private static long computeBishopMoves(int square, long blockers) {
        long moves = 0L;
        int[] directions = {9, 7, -9, -7};  // Diagonal directions

        for (int direction : directions) {
            int targetSquare = square;
            while (true) {
                targetSquare += direction;
                if (targetSquare < 0 || targetSquare >= 64) break; // Stop if out of bounds
                moves |= (1L << targetSquare);
                if ((blockers & (1L << targetSquare)) != 0) break; // Stop if blocked
            }
        }
        return moves;
    }


}
