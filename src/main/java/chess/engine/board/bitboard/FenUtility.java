package chess.engine.board.bitboard;

import java.util.Arrays;

import static chess.engine.board.bitboard.BitBoard.*;

public class FenUtility {

    public static void parseFEN(final String fenString) {
        final String[] fenPartitions = fenString.trim().split(" ");

//        final boolean whiteKingSideCastle = whiteKingSideCastle(fenPartitions[2]);
//        final boolean whiteQueenSideCastle = whiteQueenSideCastle(fenPartitions[2]);
//        final boolean blackKingSideCastle = blackKingSideCastle(fenPartitions[2]);
//        final boolean blackQueenSideCastle = blackQueenSideCastle(fenPartitions[2]);
        final String gameConfiguration = fenPartitions[0];
        final char[] boardTiles = gameConfiguration.replaceAll("/", "")
                .replaceAll("8", "--------")
                .replaceAll("7", "-------")
                .replaceAll("6", "------")
                .replaceAll("5", "-----")
                .replaceAll("4", "----")
                .replaceAll("3", "---")
                .replaceAll("2", "--")
                .replaceAll("1", "-")
                .toCharArray();
        int i = 0;
        while (i < boardTiles.length) {
            switch (boardTiles[i]) {
                case 'r':
                    BitBoard.bRooks |= (1L << i);
                    i++;
                    break;
                case 'n':
                    BitBoard.bKnights |= (1L << i);
                    i++;
                    break;
                case 'b':
                    BitBoard.bBishops |= (1L << i);
                    i++;
                    break;
                case 'q':
                    BitBoard.bQueens |= (1L << i);
                    i++;
                    break;
                case 'k':
                    BitBoard.bKings |= (1L << i);
                    i++;
                    break;
                case 'p':
                    BitBoard.bPawns |= (1L << i);
                    i++;
                    break;
                case 'R':
                    BitBoard.wRooks |= (1L << i);
                    i++;
                    break;
                case 'N':
                    wKnights |= (1L << i);
                    i++;
                    break;
                case 'B':
                    BitBoard.wBishops |= (1L << i);
                    i++;
                    break;
                case 'Q':
                    BitBoard.wQueens |= (1L << i);
                    i++;
                    break;
                case 'K':
                    BitBoard.wKings |= (1L << i);
                    i++;
                    break;
                case 'P':
                    wPawns |= (1L << i);
                    i++;
                    break;
                case '-':
                    i++;
                    break;
                default:
                    throw new RuntimeException("Invalid FEN String " + gameConfiguration);
            }
        }
    }

    public static void printBoard() {
        char[] board = new char[64];
        Arrays.fill(board, '.'); // Fill board with empty squares

        // Set white pieces
        setBoardPieces(board, wPawns, 'P');
        setBoardPieces(board, wKnights, 'N');
        setBoardPieces(board, wBishops, 'B');
        setBoardPieces(board, wRooks, 'R');
        setBoardPieces(board, wQueens, 'Q');
        setBoardPieces(board, wKings, 'K');

        // Set black pieces
        setBoardPieces(board, bPawns, 'p');
        setBoardPieces(board, bKnights, 'n');
        setBoardPieces(board, bBishops, 'b');
        setBoardPieces(board, bRooks, 'r');
        setBoardPieces(board, bQueens, 'q');
        setBoardPieces(board, bKings, 'k');

        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                System.out.print(board[rank * 8 + file] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void setBoardPieces(char[] board, long bitboard, char piece) {
        for (int square = 0; square < 64; square++) {
            if ((bitboard & (1L << square)) != 0) {
                board[square] = piece;
            }
        }
    }
}
