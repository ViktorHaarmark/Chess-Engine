//package chess.engine.bitboard.movegeneration;
//
//import static chess.engine.bitboard.Board.whitePieces;
//import static chess.engine.bitboard.BitBoardUtils.BLACK;
//import static chess.engine.bitboard.BitBoardUtils.WHITE;
//
//public class PawnMoves {
//    private static final long RANK_2 = 120912;
//    private static final long RANK_7 = 20392903;
//
//    private static final int PAWN_MOVE_CONSTANT = 8;
//
//    private static final int BLACK_PAWN_SINGLE = PAWN_MOVE_CONSTANT;
//
//    private static final int WHITE_PAWN_SINGLE = -1 * PAWN_MOVE_CONSTANT;
//
//    private static final long[][] PAWN_ATTACK_MASK = new long[2][64];
////    private static final long[][] PAWN_MOVE_MASK = new long[2][64];
//
//    private static final long A_FILE_MASK = 0x0101010101010101L;
//    private static final long H_FILE_MASK = 0x8080808080808080L;
//
//    static {
//        for (int square = 0; square < 64; square++) {
//            PAWN_ATTACK_MASK[WHITE][square] = computePawnCaptures(square, true);
//            PAWN_ATTACK_MASK[BLACK][square] = computePawnCaptures(square, false);
////            PAWN_MOVE_MASK[WHITE][square] = computePawnMoves(square, true);
////            PAWN_MOVE_MASK[BLACK][square] = computePawnMoves(square, false);
//        }
//    }
//
//    private static long computePawnCaptures(int square, boolean isWhite) {
//        long position = 1L << square;
//        long moves = 0L;
////        int leftCapture = (isWhite) ? WHITE_PAWN_SINGLE - 1 : BLACK_PAWN_SINGLE - 1;
////        int rightCapture = (isWhite) ? WHITE_PAWN_SINGLE + 1 : BLACK_PAWN_SINGLE + 1;
//        int leftCapture = (isWhite) ? 9 : 7;
//        int rightCapture = (isWhite) ? 7 : 9;
//        if (isWhite) {
//            moves |= position >> leftCapture;
//            moves |= position >> rightCapture;
//        } else {
//            moves |= position << leftCapture; //& ~A_FILE_MASK;
//            moves |= position << rightCapture;// & ~H_FILE_MASK;
//        }
//        return moves;
//    }
//
//
//    static long getPawnMoves(int square, long friendlyPieces, long enemyPieces) {
//        long occupied = friendlyPieces | enemyPieces;
//        long position = 1L << square;
//        long moves = 0L;
//        int singleMove = PAWN_MOVE_CONSTANT;
//        boolean isWhite = friendlyPieces == whitePieces;
//        if (isWhite) {
//            moves |= position >> singleMove & ~occupied;
//            if (moves != 0) {
//                moves |= (position >> 2 * singleMove) & ~occupied;
//            }
//        } else {
//            moves |= (position << singleMove) & ~occupied;
//            if (moves != 0) {
//                moves |= (position << 2 * singleMove) & ~occupied;
//            }
//        }
//        long attacks = (friendlyPieces == whitePieces) ? PAWN_ATTACK_MASK[WHITE][square] : PAWN_ATTACK_MASK[BLACK][square];
//        moves |= attacks & enemyPieces;
//
//        return moves;
//    }
//
////    private static long computePawnMoves(int square, boolean isWhite) {
////        long position = 1L << square;
////        long moves = 0L;
////        int singleMove = (isWhite) ? WHITE_PAWN_SINGLE : BLACK_PAWN_SINGLE;
////        int doubleMove = (isWhite) ? WHITE_PAWN_DOUBLE : BLACK_PAWN_DOUBLE;
////        long secondRank = (isWhite) ? RANK_2 : RANK_7;
////
////        moves |= position << singleMove;
////        if ((position & secondRank) != 0) {
////            moves |= position << doubleMove;
////        }
////        return moves;
////    }
//
//}
