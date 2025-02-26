package chess.engine.bitboard.movegeneration;

public class MoveGeneration {

    public static long getKnightMoves(int square, long friendlyPieces, long enemyPieces) {
        return KnightMoves.getKnightMoves(square, friendlyPieces);
    }

    public static long getBishopMoves(int square, long friendlyPieces, long enemyPieces) {
        return BishopMoves.getBishopMoves(square, friendlyPieces, enemyPieces);
    }

    public static long getRookMoves(int square, long friendlyPieces, long enemyPieces) {
        return SlidingMoves.getRookMoves(square, friendlyPieces, enemyPieces);
    }

    public static long getQueenMoves(int square, long friendlyPieces, long enemyPieces) {
        return SlidingMoves.getQueenMoves(square, friendlyPieces, enemyPieces);
    }

    public static long getKingMoves(int square, long friendlyPieces, long enemyPieces) {
        return KingMoves.getKingMoves(square, friendlyPieces);
    }

    public static long getPawnMoves(int square, long friendlyPieces, long enemyPieces) {
        return PawnMoves.getPawnMoves(square, friendlyPieces, enemyPieces);
    }

//    public static List<Move> generateMovesFromSquareAndPieceType(int fromSquare, int pieceType, long enemyPieces, boolean isPawn) {
//        List<Move> moves = new ArrayList<>();
//
//        while (moveBitboard != 0) {
//            int toSquare = Long.numberOfTrailingZeros(moveBitboard);
//
//            boolean isCapture = (enemyPieces & (1L << toSquare)) != 0;
//            int capturedPiece = 0;
//            if (isCapture) {
//                capturedPiece = BitBoard.getPieceType(toSquare);
//            }
//
//            if (isPawn && isPromotion) {
//                int[] promotionPieces = {QUEENS, ROOKS, BISHOPS, KNIGHTS};
//                for (int promotionPiece : promotionPieces) {
//                    moves.add(new Move(fromSquare, toSquare, capturedPiece, promotionPiece));
//                }
//            } else {
//                moves.add(new Move(fromSquare, toSquare, capturedPiece));
//            }
//
//            // Clear the LSB
//            moveBitboard &= moveBitboard - 1;
//        }
//
//        return moves;
//    }
}
