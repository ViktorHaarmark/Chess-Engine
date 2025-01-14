package chess.engine.Players.ai;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;

import java.util.ArrayList;
import java.util.List;

public class AlphaBeta implements MoveStrategy {
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;


    public AlphaBeta(final int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();

        Move bestMove = null;

        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.currentPlayer() + "THINKING with alphaBeta search and depth = " + searchDepth);

        for (final Move move : board.getLegalMoves()) {
            if (move.getMoveStatus().isDone()) {//Can probably delete, all moves should be legal anyway
                board.execute(move);
                currentValue = board.currentPlayer().getColor().isWhite() ?
                        alphaBetaSearch(board, searchDepth-1, -100000, 100000, false) :
                        alphaBetaSearch(board, searchDepth-1, -100000, 100000, true);
                board.takeback(move);

                if (board.currentPlayer().getColor().isWhite() && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.currentPlayer().getColor().isBlack() && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }

        final long executionTime = System.currentTimeMillis() - startTime;
        System.out.println("Thiking time: " + executionTime / 1000.0);

        return bestMove;
    }

    public int alphaBetaSearch(final Board board, final int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        if (!maximizingPlayer) {
            int lowestSeenValue = Integer.MAX_VALUE;
            List<Move> legalMoves = new ArrayList<>(board.getLegalMoves());
            legalMoves.sort(new MoveSorter());
            for (final Move move : legalMoves) {
                if (move.getMoveStatus().isDone()) {
                    board.execute(move);
                    final int currentValue = alphaBetaSearch(board, depth - 1, alpha, beta, true);
                    board.takeback(move);
                    lowestSeenValue = Math.min(lowestSeenValue, currentValue);
                    beta = Math.min(beta, currentValue);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return lowestSeenValue;
        }
        else {
            int highestSeenValue = Integer.MIN_VALUE;
            for (final Move move : board.getLegalMoves()) {
                if (move.getMoveStatus().isDone()) {
                    final int currentValue = alphaBetaSearch(board, depth - 1, alpha, beta, false );
                    board.takeback(move);
                    highestSeenValue = Math.max(highestSeenValue, currentValue);
                    alpha = Math.max(alpha, currentValue);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return highestSeenValue;
        }


    }

}
