package chess.engine.board.bitboard.movegeneration;

public class MoveGeneration {

    public static long getKnightMoves(int square, long friendlyPieces, long enemyPieces) {
        return KnightMoves.getKnightMoves(square, friendlyPieces);
    }

    public static long getBishopMoves(int square, long friendlyPieces, long enemyPieces) {
        return BishopMoves.getBishopMoves(square, friendlyPieces, enemyPieces);
    }

    public static long getRookMoves(int square, long friendlyPieces, long enemyPieces) {
        return RookMoves.getRookMoves(square, friendlyPieces, enemyPieces);
    }

    public static long getQueenMoves(int square, long friendlyPieces, long enemyPieces) {
        return RookMoves.getRookMoves(square, friendlyPieces, enemyPieces) | BishopMoves.getBishopMoves(square, friendlyPieces, enemyPieces);
    }

    public static long getKingMoves(int square, long friendlyPieces, long enemyPieces) {
        return 0;
    }

    public static long getPawnMoves(int square, long friendlyPieces, long enemyPieces) {
        return 0;
    }
}
