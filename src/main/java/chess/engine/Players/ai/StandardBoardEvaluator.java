package chess.engine.Players.ai;

import chess.engine.Players.Player;
import chess.engine.board.Board;
import chess.engine.pieces.Piece;

public final class StandardBoardEvaluator implements BoardEvaluator {

    private static final int CHECK_BONUS = 50;
    private static final int CHECKMATE_BONUS = 10000;
    private static final int CASTLED_BONUS = 60;


    @Override
    public int evaluate(Board board) {
        int score = ScorePlayer(board, board.getWhitePlayer()) - ScorePlayer(board, board.getBlackPlayer());

        return (board.getCurrentPlayer().getColor().isWhite() ? score : -score);
    }

    private int ScorePlayer(Board board,
                            Player player
    ) {
        return pieceValue(player)
                + mobility(player)
                + check(player)
                + checkmate(player)
                + castled(player);
    }

    private static int pieceValue(final Player player) {
        int pieceValueScore = 0;

        for (Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPieceValue();
            pieceValueScore += piece.getPiecePositionValue();
        }

        return pieceValueScore;
    }

    private static int mobility(final Player player) {
        return player.getPossibleMoves().size();
    }

    private static int check(final Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int checkmate(final Player player) {
        return player.getOpponent().isInCheckMate() ? CHECKMATE_BONUS : 0;
    }

    private static int castled(final Player player) {
        return player.isCastled() ? CASTLED_BONUS : 0;
    }

}
