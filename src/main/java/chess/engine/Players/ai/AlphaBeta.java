package chess.engine.Players.ai;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.MoveTransition;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class AlphaBeta implements MoveStrategy {
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    private int numPosition;
    private final MoveSorter sorter;


    public AlphaBeta(final int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
        this.sorter = new MoveSorter();
    }

    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();

        Move bestMove = null;

        int highestSeenValue = -100000;
        int lowestSeenValue = 100000;
        int currentValue;

        System.out.println(board.currentPlayer() + " thinking with alphaBeta search and depth = " + searchDepth);

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
        int bestMoveEval = (board.currentPlayer().getColor().isWhite() ? highestSeenValue : lowestSeenValue);
        final double executionTime = (System.currentTimeMillis() - startTime)/1000.0;
        System.out.println("Thinking time: " + executionTime);
        System.out.println("Number of positions evaluated: " + numPosition);
        System.out.println("Positions evaluated per second: " + round((numPosition/executionTime)));
        System.out.println("The evaluation of " + bestMove.toString() + " is: " + bestMoveEval);
        numPosition = 0;

        return bestMove;
    }

    public int alphaBetaSearch(final Board board, final int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            numPosition += 1;
            return this.boardEvaluator.evaluate(board);
        }
        if (!maximizingPlayer) {
            int lowestSeenValue = 100000;
            List<Move> legalMoves = new ArrayList<>(board.currentPlayer().getLegalMoves());
            legalMoves.sort(sorter); //TODO: Why is this so slow?
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
            int highestSeenValue = -100000;
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

    private int searchAllCaptures(Board board, int alpha, int beta) {
        int evaluation = boardEvaluator.evaluate(board);
        if (evaluation > beta) {
            return beta;
        }
        alpha = Math.max(alpha, evaluation);
        List<Move> captureMoves = new ArrayList<>(board.currentPlayer().getCaptureMoves());
        captureMoves.sort(new MoveSorter());
        for (Move move : captureMoves) {
            MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                evaluation = -searchAllCaptures(moveTransition.toBoard(), -beta, -alpha);
            }
            if (evaluation >= beta) {
                return beta;
            }
            alpha = Math.max(alpha, evaluation);
        }
        return alpha;
    }

}
