package chess.engine.Players.ai;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.MoveTransition;

import java.util.ArrayList;
import java.util.List;

public class AlphaBeta implements MoveStrategy {
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    private int numPosition;


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

        for (final Move move : board.currentPlayer().getLegalMoves()) {

            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.moveStatus().isDone()) {
                currentValue = board.currentPlayer().getColor().isWhite() ?
                        alphaBetaSearch(moveTransition.toBoard(), searchDepth-1, -100000, 100000, false) :
                        alphaBetaSearch(moveTransition.toBoard(), searchDepth-1, -100000, 100000, true);

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
        System.out.println("Number of positions evaluated: " + numPosition);
        numPosition = 0;

        return bestMove;
    }

    public int alphaBetaSearch(final Board board, final int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            numPosition += 1;
            return this.boardEvaluator.evaluate(board);
        }
        if (!maximizingPlayer) {
            int lowestSeenValue = Integer.MAX_VALUE;
            List<Move> legalMoves = new ArrayList<>(board.currentPlayer().getLegalMoves());
            //legalMoves.sort(new MoveSorter());
            for (final Move move : legalMoves) {
                final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
                if (moveTransition.moveStatus().isDone()) {
                    final int currentValue = alphaBetaSearch(moveTransition.toBoard(), depth - 1, alpha, beta, true);
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
            for (final Move move : board.currentPlayer().getLegalMoves()) {
                final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
                if (moveTransition.moveStatus().isDone()) {
                    final int currentValue = alphaBetaSearch(moveTransition.toBoard(), depth - 1, alpha, beta, false );
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

//    private int searchAllCaptures(Board board, int alpha, int beta) {
//        int evaluation = boardEvaluator.evaluate(board);
//        if (evaluation > beta) {
//            return beta;
//        }
//        alpha = Math.max(alpha, evaluation);
//        List<Move> captureMoves =
//    }

}
