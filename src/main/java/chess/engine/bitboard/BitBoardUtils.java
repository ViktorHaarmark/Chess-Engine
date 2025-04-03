//package chess.engine.bitboard;
//
//public class BitBoardUtils {
//    public final static long FileA = 0x10101010; //TODO: Check if this is corret
//
//    public final static long RANK_1 = 0b11111111;
//    public final static long RANK_2 = RANK_1 << 8;
//    public final static long RANK_3 = RANK_2 << 8;
//    public final static long RANK_4 = RANK_3 << 8;
//    public final static long RANK_5 = RANK_4 << 8;
//    public final static long RANK_6 = RANK_5 << 8;
//    public final static long RANK_7 = RANK_6 << 8;
//    public final static long RANK_8 = RANK_7 << 8;
//
//    public final static long notAFile = ~FileA;
//    public final static long notHFile = ~(FileA << 7);
//
//    public static long[] KnightAttacks; //TODO: readonly?
//    public static long[] KingMoves;
//    public static long[] WhitePawnAttacks;
//    public static long[] BlackPawnAttacks;
//
//    // Get index of least significant set bit in given 64bit value. Also clears the bit to zero.
//    public static int popLSB(long[] b) {
//        int index = Long.numberOfTrailingZeros(b[0]);
//        b[0] &= (b[0] - 1); // This modifies the original b[0]
//        return index;
//    }
//
//    public static long pawnAttacks(long pawnBitboard, boolean isWhite) {
//        // Pawn attacks are calculated like so: (example given with white to move)
//
//        // The first half of the attacks are calculated by shifting all pawns north-east: northEastAttacks = pawnBitboard << 9
//        // Note that pawns on the h file will be wrapped around to the a file, so then mask out the a file: northEastAttacks &= notAFile
//        // (Any pawns that were originally on the a file will have been shifted to the b file, so a file should be empty).
//
//        // The other half of the attacks are calculated by shifting all pawns north-west. This time the h file must be masked out.
//        // Combine the two halves to get a bitboard with all the pawn attacks: northEastAttacks | northWestAttacks
//
//        if (isWhite) {
//            return ((pawnBitboard << 9) & notAFile) | ((pawnBitboard << 7) & notHFile);
//        }
//
//        return ((pawnBitboard >> 7) & notAFile) | ((pawnBitboard >> 9) & notHFile);
//    }
//
//
//    public static long shift(long bitboard, int numSquaresToShift) {
//        if (numSquaresToShift > 0) {
//            return bitboard << numSquaresToShift;
//        } else {
//            return bitboard >> -numSquaresToShift;
//        }
//
//    }
//
////    BitBoardUtils() {
////        KnightAttacks = new long[64];
////        KingMoves = new long[64];
////        WhitePawnAttacks = new long[64];
////        BlackPawnAttacks = new long[64];
////
////        ( int x, int y)[]orthoDir = {(-1, 0),(0, 1),(1, 0),(0, -1) };
////        ( int x, int y)[]diagDir = {(-1, -1),(-1, 1),(1, 1),(1, -1) };
////        ( int x, int y)[]knightJumps = {(-2, -1),(-2, 1),(-1, 2),(1, 2),(2, 1),(2, -1),(1, -2),(-1, -2) };
////
////        for (int y = 0; y < 8; y++) {
////            for (int x = 0; x < 8; x++) {
////                processSquare(x, y);
////            }
////        }
////    }
////
////    private void processSquare(int x, int y) {
////        int squareIndex = y * 8 + x;
////
////        for (int dirIndex = 0; dirIndex < 4; dirIndex++) {
////            // Orthogonal and diagonal directions
////            for (int dst = 1; dst < 8; dst++) {
////                int orthoX = x + orthoDir[dirIndex].x * dst;
////                int orthoY = y + orthoDir[dirIndex].y * dst;
////                int diagX = x + diagDir[dirIndex].x * dst;
////                int diagY = y + diagDir[dirIndex].y * dst;
////
////                if (ValidSquareIndex(orthoX, orthoY, out int orthoTargetIndex))
////                {
////                    if (dst == 1) {
////                        KingMoves[squareIndex] |= 1 ul << orthoTargetIndex;
////                    }
////                }
////
////                if (ValidSquareIndex(diagX, diagY, out int diagTargetIndex))
////                {
////                    if (dst == 1) {
////                        KingMoves[squareIndex] |= 1 ul << diagTargetIndex;
////                    }
////                }
////            }
////
////            // Knight jumps
////            for (int i = 0; i < knightJumps.Length; i++) {
////                int knightX = x + knightJumps[i].x;
////                int knightY = y + knightJumps[i].y;
////                if (ValidSquareIndex(knightX, knightY, out int knightTargetSquare))
////                {
////                    KnightAttacks[squareIndex] |= 1 ul << knightTargetSquare;
////                }
////            }
////
////            // Pawn attacks
////
////            if (ValidSquareIndex(x + 1, y + 1, out int whitePawnRight))
////            {
////                WhitePawnAttacks[squareIndex] |= 1 ul << whitePawnRight;
////            }
////            if (ValidSquareIndex(x - 1, y + 1, out int whitePawnLeft))
////            {
////                WhitePawnAttacks[squareIndex] |= 1 ul << whitePawnLeft;
////            }
////
////
////            if (ValidSquareIndex(x + 1, y - 1, out int blackPawnAttackRight))
////            {
////                BlackPawnAttacks[squareIndex] |= 1 ul << blackPawnAttackRight;
////            }
////            if (ValidSquareIndex(x - 1, y - 1, out int blackPawnAttackLeft))
////            {
////                BlackPawnAttacks[squareIndex] |= 1 ul << blackPawnAttackLeft;
////            }
////
////
////        }
////    }
////
////    boolean validSquareIndex(int x, int y, out int index) {
////        index = y * 8 + x;
////        return x >= 0 && x < 8 && y >= 0 && y < 8;
////    }
//}
//
//
//}
