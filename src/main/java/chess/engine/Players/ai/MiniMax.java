package chess.engine.Players.ai;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.MoveTransition;

public class MiniMax implements MoveStrategy {

    private final StandardBoardEvaluator boardEvaluator;
    private final int searchDepth;

    public MiniMax(final int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();

        Move bestMove = null;

        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.getCurrentPlayer() + " THINKING with depth = " + searchDepth);

        for (final Move move : board.getCurrentPlayer().getPossibleMoves()) {

            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.moveStatus().isDone()) {
                currentValue = board.getCurrentPlayer().getColor().isWhite() ?
                        -miniMax(moveTransition.getToBoard(), searchDepth - 1) :
                        miniMax(moveTransition.getToBoard(), searchDepth - 1);

                if (board.getCurrentPlayer().getColor().isWhite() && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.getCurrentPlayer().getColor().isBlack() && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }

        final long executionTime = System.currentTimeMillis() - startTime;
        System.out.println("Thinking time: " + executionTime / 1000.0);

        return bestMove;
    }

    private int miniMax(final Board board, final int depth) {
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            return this.boardEvaluator.evaluate(board);
        }

        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.getCurrentPlayer().getPossibleMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                int currentValue;

                currentValue = -miniMax(moveTransition.getToBoard(), depth - 1);

                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }
}
