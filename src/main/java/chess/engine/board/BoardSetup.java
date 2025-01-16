package chess.engine.board;

import chess.Color;
import chess.engine.board.Board.Builder;
import chess.engine.pieces.Bishop;
import chess.engine.pieces.King;
import chess.engine.pieces.Knight;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Queen;
import chess.engine.pieces.Rook;
import chess.pgn.FenUtility;

public class BoardSetup {

    public static Board createStandardBoard() { //Standard position
        final Builder builder = new Builder();

        // Black pieces
        builder.setPiece(new Rook(0, Color.BLACK, true));
        builder.setPiece(new Knight(1, Color.BLACK, true));
        builder.setPiece(new Bishop(2, Color.BLACK, true));
        builder.setPiece(new Queen(3, Color.BLACK, true));
        builder.setPiece(new King(4, Color.BLACK, true, true));
        builder.setPiece(new Bishop(5, Color.BLACK, true));
        builder.setPiece(new Knight(6, Color.BLACK, true));
        builder.setPiece(new Rook(7, Color.BLACK, true));
        builder.setPiece(new Pawn(8, Color.BLACK, true));
        builder.setPiece(new Pawn(9, Color.BLACK, true));
        builder.setPiece(new Pawn(10, Color.BLACK, true));
        builder.setPiece(new Pawn(11, Color.BLACK, true));
        builder.setPiece(new Pawn(12, Color.BLACK, true));
        builder.setPiece(new Pawn(13, Color.BLACK, true));
        builder.setPiece(new Pawn(14, Color.BLACK, true));
        builder.setPiece(new Pawn(15, Color.BLACK, true));


        //White pieces
        builder.setPiece(new Rook(56, Color.WHITE, true));
        builder.setPiece(new Knight(57, Color.WHITE, true));
        builder.setPiece(new Bishop(58, Color.WHITE, true));
        builder.setPiece(new Queen(59, Color.WHITE, true));
        builder.setPiece(new King(60, Color.WHITE, true, true));
        builder.setPiece(new Bishop(61, Color.WHITE, true));
        builder.setPiece(new Knight(62, Color.WHITE, true));
        builder.setPiece(new Rook(63, Color.WHITE, true));
        builder.setPiece(new Pawn(48, Color.WHITE, true));
        builder.setPiece(new Pawn(49, Color.WHITE, true));
        builder.setPiece(new Pawn(50, Color.WHITE, true));
        builder.setPiece(new Pawn(51, Color.WHITE, true));
        builder.setPiece(new Pawn(52, Color.WHITE, true));
        builder.setPiece(new Pawn(53, Color.WHITE, true));
        builder.setPiece(new Pawn(54, Color.WHITE, true));
        builder.setPiece(new Pawn(55, Color.WHITE, true));

        builder.setMoveMaker(Color.WHITE);

        return builder.build();
    }

    public static Board createTwoBishopCheckMate() {
        final Builder builder = new Builder();

        builder.setPiece(new King(7, Color.BLACK, true, true));
        builder.setPiece(new Pawn(8, Color.BLACK, false));
        builder.setPiece(new Pawn(10, Color.BLACK, false));
        builder.setPiece(new Pawn(15, Color.BLACK, false));
        builder.setPiece(new Pawn(17, Color.BLACK, false));
        // White Layout
        builder.setPiece(new Bishop(40, Color.WHITE, false));
        builder.setPiece(new Bishop(48, Color.WHITE, false));
        builder.setPiece(new King(53, Color.WHITE, true, true));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);

        return builder.build();
    }

    public static Board createAnastatsiaCheckmate() {
        final Builder builder = new Builder();

        // Black Layout
        builder.setPiece(new Rook(0, Color.BLACK, true));
        builder.setPiece(new Rook(5, Color.BLACK, false));
        builder.setPiece(new Pawn(8, Color.BLACK, false));
        builder.setPiece(new Pawn(9, Color.BLACK, false));
        builder.setPiece(new Pawn(10, Color.BLACK, false));
        builder.setPiece(new Pawn(13, Color.BLACK, false));
        builder.setPiece(new Pawn(14, Color.BLACK, false));
        builder.setPiece(new King(15, Color.BLACK, true, true));
        // White Layout
        builder.setPiece(new Knight(12, Color.WHITE, false));
        builder.setPiece(new Rook(27, Color.WHITE, false));
        builder.setPiece(new Pawn(41, Color.WHITE, false));
        builder.setPiece(new Pawn(48, Color.WHITE, false));
        builder.setPiece(new Pawn(53, Color.WHITE, false));
        builder.setPiece(new Pawn(54, Color.WHITE, false));
        builder.setPiece(new Pawn(55, Color.WHITE, false));
        builder.setPiece(new King(62, Color.WHITE, true, true));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);

        return builder.build();
    }

    public static Board createPromotionScenario() {
        final Builder builder = new Builder();

        builder.setPiece(new King(7, Color.BLACK, true, true));
        builder.setPiece(new Pawn(10, Color.BLACK, false));
        builder.setPiece(new Pawn(15, Color.BLACK, false));
        builder.setPiece(new Pawn(17, Color.BLACK, false));
        // White Layout
        builder.setPiece(new Bishop(40, Color.WHITE, false));
        builder.setPiece(new Pawn(8, Color.WHITE, false));
        builder.setPiece(new King(53, Color.WHITE, true, true));
        // Set the current player
        builder.setMoveMaker(Color.WHITE);

        return builder.build();
    }

    public static Board createBUGBoard() {
        return FenUtility.createGameFromFEN("R6R/3Q4/1Q4Q1/4Q3/2Q4Q/Q4Q2/pp1Q4/kBNN1KB1 w - - 0 1");
    }

    public static Board createBug2Board() {
        return FenUtility.createGameFromFEN("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 0 1");
    }

    public static Board InterestingPosition() {
        return FenUtility.createGameFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
    }

}
