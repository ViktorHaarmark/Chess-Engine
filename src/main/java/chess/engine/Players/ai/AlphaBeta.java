package chess.engine.Players.ai;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.MoveTransition;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class AlphaBeta implements MoveStrategy {
    private final StandardBoardEvaluator boardEvaluator;
    private final int searchDepth;
    private int numPosition;
    private final static MoveSorter MOVE_SORTER = new MoveSorter();
    private final static CaptureMoveSorter CAPTURE_MOVE_SORTER = new CaptureMoveSorter();
    private final static int QUISCENCE_SEARCH_DEPTH = 4;



    public AlphaBeta(final int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();

        Move bestMove = null;

        int highestSeenValue = -100000;
        int lowestSeenValue = 100000;
        int currentValue;

        System.out.println(board.getCurrentPlayer() + " thinking with alphaBeta search and depth = " + searchDepth);

        for (final Move move : board.getCurrentPlayer().getPossibleMoves()) {

            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.moveStatus().isDone()) {
                currentValue = board.getCurrentPlayer().getColor().isWhite() ?
                        -alphaBetaSearch(moveTransition.toBoard(), searchDepth - 1, -100000, 100000) :
                        alphaBetaSearch(moveTransition.toBoard(), searchDepth - 1, -100000, 100000);

                if (board.getCurrentPlayer().getColor().isWhite() && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.getCurrentPlayer().getColor().isBlack() && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        int bestMoveEval = (board.getCurrentPlayer().getColor().isWhite() ? highestSeenValue : lowestSeenValue);
        final double executionTime = (System.currentTimeMillis() - startTime) / 1000.0;
        System.out.println("Thinking time: " + executionTime);
        System.out.println("Number of positions evaluated: " + numPosition);
        System.out.println("Positions evaluated per second: " + round((numPosition / executionTime)));
        System.out.println("The evaluation of " + bestMove.toString() + " is: " + bestMoveEval);
        numPosition = 0;

        return bestMove;
    }

    public int alphaBetaSearch(final Board board, final int depth, int alpha, int beta) {
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            numPosition += 1;
            return quiescenceSearch(board, alpha, beta, QUISCENCE_SEARCH_DEPTH);
            //return boardEvaluator.evaluate(board);
        }


        int highestSeenValue = -100000;
        List<Move> possibleMoves = new ArrayList<>(board.getCurrentPlayer().getPossibleMoves());
        possibleMoves.sort(MOVE_SORTER);

        for (final Move move : board.getCurrentPlayer().getPossibleMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.moveStatus().isDone()) {
                final int currentValue = -alphaBetaSearch(moveTransition.toBoard(), depth - 1, -beta, -alpha);
                highestSeenValue = Math.max(highestSeenValue, currentValue);
                alpha = Math.max(alpha, currentValue);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return highestSeenValue;


    }

    private int quiescenceSearch(final Board board, int alpha, int beta, int depth) {
        int evaluation = boardEvaluator.evaluate(board); //TODO: Improve quiscence search
        if (depth <= 0) {
            return evaluation;
        }

        if (evaluation >= beta) {
            return beta;
        }

        if (evaluation >= alpha) {
            alpha = evaluation;
        }

        List<Move.CaptureMove> captureMoves = new ArrayList<>(board.getCurrentPlayer().getCaptureMoves());
        //captureMoves.sort(captureMoveSorter);

        for (Move move : captureMoves) {
            MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                int score = -quiescenceSearch(moveTransition.toBoard(), -beta, -alpha, depth-1);
                if (score >= beta) {
                    return beta;
                }
                if (score > alpha) {
                    alpha = score;
                }
            }
        }
        return alpha;
    }
}
