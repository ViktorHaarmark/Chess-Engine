package chess.pgn;

import chess.Color;
import chess.engine.Players.Player;
import chess.engine.board.Board;
import chess.engine.board.Board.Builder;
import chess.engine.board.BoardUtils;
import chess.engine.pieces.Bishop;
import chess.engine.pieces.King;
import chess.engine.pieces.Knight;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Queen;
import chess.engine.pieces.Rook;

public class FenUtility { //TODO: When rook moves, remove castling rights

    FenUtility() {
        throw new RuntimeException("Not instantiable");
    }

    public static String createFENFromGame(final Board board) {
        return calculateBoardText(board) + " " +
                calculateCurrentPlayerText(board) + " " +
                calculateCastlingInformation(board) + " " +
                calculateEnPassantSquare(board) + " " +
                "0 1";
    }

    public static Board createGameFromFEN(final String fenString) {
        return parseFEN(fenString);
    }

    // Region createFENFromGame
    private static String calculateBoardText(final Board board) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = board.getPiece(i) == null ? "-" :
                    board.getPiece(i).getPieceColor().isWhite() ? board.getPiece(i).toString() :
                            board.getPiece(i).toString().toLowerCase();
            builder.append(tileText);
        }
        builder.insert(8, "/");
        builder.insert(17, "/");
        builder.insert(26, "/");
        builder.insert(35, "/");
        builder.insert(44, "/");
        builder.insert(53, "/");
        builder.insert(62, "/");
        return builder.toString()
                .replaceAll("--------", "8")
                .replaceAll("-------", "7")
                .replaceAll("------", "6")
                .replaceAll("-----", "5")
                .replaceAll("----", "4")
                .replaceAll("---", "3")
                .replaceAll("--", "2")
                .replaceAll("-", "1");
    }

    private static String calculateCurrentPlayerText(final Board board) {
        return board.currentPlayer().toString().substring(0, 1).toLowerCase();

    }

    private static String calculateCastlingInformation(final Board board) {
        final StringBuilder builder = new StringBuilder();

        if (board.whitePlayer().isKingSideCastleCapable()) {
            builder.append("K");
        }
        if (board.whitePlayer().isQueenSideCastleCapable()) {
            builder.append("Q");
        }
        if (board.blackPlayer().isKingSideCastleCapable()) {
            builder.append("k");
        }
        if (board.blackPlayer().isQueenSideCastleCapable()) {
            builder.append("q");
        }

        final String result = builder.toString();
        return result.isEmpty() ? "-" : result;
    }

    private static String calculateEnPassantSquare(final Board board) {
        final Player player = board.currentPlayer();
        if (board.getEnPassantPawn() != null) {
            return BoardUtils.getPositionAtCoordinate(board.getEnPassantPawn().getPiecePosition() - (8 * board.getEnPassantPawn().getPieceColor().getDirection()));
        }

        return "-";
    }

    //Region end createFENFromGame

    //Region createGameFromFEN


    private static Board parseFEN(final String fenString) {
        final String[] fenPartitions = fenString.trim().split(" ");
        final Builder builder = new Builder();
        final boolean whiteKingSideCastle = whiteKingSideCastle(fenPartitions[2]);
        final boolean whiteQueenSideCastle = whiteQueenSideCastle(fenPartitions[2]);
        final boolean blackKingSideCastle = blackKingSideCastle(fenPartitions[2]);
        final boolean blackQueenSideCastle = blackQueenSideCastle(fenPartitions[2]);
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
                    builder.setPiece(new Rook(i, Color.BLACK, true));
                    i++;
                    break;
                case 'n':
                    builder.setPiece(new Knight(i, Color.BLACK, true));
                    i++;
                    break;
                case 'b':
                    builder.setPiece(new Bishop(i, Color.BLACK, true));
                    i++;
                    break;
                case 'q':
                    builder.setPiece(new Queen(i, Color.BLACK, true));
                    i++;
                    break;
                case 'k':
                    final boolean isCastled = !blackKingSideCastle && !blackQueenSideCastle;
                    builder.setPiece(new King(i, Color.BLACK, blackKingSideCastle, blackQueenSideCastle));
                    i++;
                    break;
                case 'p':
                    builder.setPiece(new Pawn(i, Color.BLACK));
                    i++;
                    break;
                case 'R':
                    builder.setPiece(new Rook(i, Color.WHITE, true));
                    i++;
                    break;
                case 'N':
                    builder.setPiece(new Knight(i, Color.WHITE));
                    i++;
                    break;
                case 'B':
                    builder.setPiece(new Bishop(i, Color.WHITE));
                    i++;
                    break;
                case 'Q':
                    builder.setPiece(new Queen(i, Color.WHITE));
                    i++;
                    break;
                case 'K':
                    builder.setPiece(new King(i, Color.WHITE, whiteKingSideCastle, whiteQueenSideCastle));
                    i++;
                    break;
                case 'P':
                    builder.setPiece(new Pawn(i, Color.WHITE));
                    i++;
                    break;
                case '-':
                    i++;
                    break;
                default:
                    throw new RuntimeException("Invalid FEN String " + gameConfiguration);
            }
        }
        builder.setMoveMaker(moveMaker(fenPartitions[1]));
        return builder.build();
    }

    private static boolean whiteKingSideCastle(final String fenCastle) {
        return fenCastle.contains("K");
    }

    private static boolean whiteQueenSideCastle(final String fenCastle) {
        return fenCastle.contains("Q");
    }

    private static boolean blackKingSideCastle(final String fenCastle) {
        return fenCastle.contains("k");
    }

    private static boolean blackQueenSideCastle(final String fenCastle) {
        return fenCastle.contains("q");
    }

    private static Color moveMaker(final String moveMaker) {
        return moveMaker.equals("w") ? Color.WHITE : Color.BLACK;
    }

    // Region end 

}
