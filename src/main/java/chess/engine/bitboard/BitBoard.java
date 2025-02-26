package chess.engine.bitboard;

import chess.engine.board.BoardUtils;
import chess.engine.bitboard.movegeneration.MoveGeneration;

import java.util.List;

import static chess.engine.bitboard.BitBoardUtils.*;
import static chess.engine.bitboard.FenUtility.printBoard;

public class BitBoard {

    public static long wPawns, wKnights, wBishops, wRooks, wQueens, wKings, bPawns, bKnights, bBishops, bRooks, bQueens, bKings;
    public static long whitePieces, blackPieces, allPieces;

    public BitBoard(long[][] pieces) {
        BitBoard.wPawns = pieces[WHITE][PAWNS];
        BitBoard.bPawns = pieces[BLACK][PAWNS];
        BitBoard.wKnights = pieces[WHITE][KNIGHTS];
        BitBoard.bKnights = pieces[BLACK][KNIGHTS];
        BitBoard.wBishops = pieces[WHITE][BISHOPS];
        BitBoard.bBishops = pieces[BLACK][BISHOPS];
        BitBoard.wRooks = pieces[WHITE][ROOKS];
        BitBoard.bRooks = pieces[BLACK][ROOKS];
        BitBoard.wQueens = pieces[WHITE][QUEENS];
        BitBoard.bQueens = pieces[BLACK][QUEENS];
        BitBoard.wKings = pieces[WHITE][KINGS];
        BitBoard.bKings = pieces[BLACK][KINGS];

    }

    public static void main(String[] args) {
        BitBoard board = new BitBoard(new long[2][6]);
        //FenUtility.parseFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        FenUtility.parseFEN("r5qK/4Q2n/2r5/2B1r3/bbRRRRRR/3rr3/k1r1q1Q1/1R2R3 w - - 0 1");
        updateOccupancy();
        printBoard();
        System.out.println(board);
        System.out.println("finished");

    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            String tileText = (this.isAttacked(i, whitePieces, blackPieces)) ? "1" : "0";
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % BoardUtils.ROW_LENGTH == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    static void updateOccupancy() {
        whitePieces = wPawns | wKnights | wBishops | wRooks | wQueens | wKings;
        blackPieces = bPawns | bKnights | bBishops | bRooks | bQueens | bKings;
        allPieces = whitePieces | blackPieces;
    }

    public boolean isOccupied(int square) {
        return (allPieces & 1L << square) != 0;
    }

    private boolean isAttacked(int square, long friendlyPieces, long enemyPieces) {
        int pieceSquare = 53;
//        List<Move> moves = MoveGeneration.generateMovesFromSquareAndPieceType(pieceSquare, MoveGeneration.getPawnMoves(pieceSquare, friendlyPieces, enemyPieces), blackPieces, true, false);
        return (MoveGeneration.getPawnMoves(pieceSquare, friendlyPieces, enemyPieces) & 1L << square) != 0;
    }

    public static int getPieceType(int square) {
        return 0; //TODO: Not implemented yet
    }

}

