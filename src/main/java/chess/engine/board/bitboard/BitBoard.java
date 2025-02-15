package chess.engine.board.bitboard;

public class BitBoard {
    public static long pawns, knights, bishops, rooks, queens, kings;
    public static long whitePieces, blackPieces, allPieces;

    public BitBoard(long pawns, long knights, long bishops, long rooks, long queens, long kings) {
        BitBoard.pawns = pawns;
        BitBoard.knights = knights;
        BitBoard.bishops = bishops;
        BitBoard.rooks = rooks;
        BitBoard.queens = queens;
        BitBoard.kings = kings;
        updateOccupancy();
    }

    private void updateOccupancy() {
        whitePieces = pawns | knights | bishops | rooks | queens | kings;
        blackPieces = 0;
        allPieces = whitePieces | blackPieces;
    }

    public boolean isOccupied(int square) {
        return (allPieces & 1L << square) != 0;
    }

}

