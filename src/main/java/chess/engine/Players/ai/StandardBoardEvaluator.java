package chess.engine.Players.ai;

import chess.engine.Players.Player;
import chess.engine.board.Board;
import chess.engine.pieces.Piece;

public final class StandardBoardEvaluator implements BoardEvaluator {

    private static final int CHECK_BONUS = 50;
    private static final int CHECKMATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLED_BONUS = 60;
    
        @Override
        public int evaluate(Board board, int depth) {
    
            return ScorePlayer(board, board.whitePlayer(), depth) - ScorePlayer(board, board.blackPlayer(), depth);
        }
    
        private int ScorePlayer(Board board,
                                Player player,
                                int depth) {
            return pieceValue(player)
                 + mobility(player)
                 + check(player)
                 + checkmate(player, depth)
                 + castled(player);
        }
    
        private static int pieceValue(final Player player) {
            int pieceValueScore = 0;
    
            for (Piece piece : player.getActivePieces()) {
                pieceValueScore += piece.getPieceValue();
            }
            
            return pieceValueScore;
        }
    
        private static int mobility(final Player player) {
            return player.getLegalMoves().size();
        }
    
        private static int check(final Player player) {
            return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
        }

        private static int checkmate(final Player player, final int depth) {
            return player.getOpponent().isInCheckMate() ? CHECKMATE_BONUS * depthBonus(depth) : 0;
        }

        private static int castled(final Player player) {
            return player.isCastled() ? CASTLED_BONUS : 0;
        }

        private static int depthBonus(final int depth) {
            return depth == 0 ? 1 : DEPTH_BONUS * depth;
        }
}
