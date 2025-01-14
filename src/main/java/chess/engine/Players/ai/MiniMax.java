package chess.engine.Players.ai;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;

public class MiniMax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
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

        System.out.println(board.currentPlayer() + "THINKING with depth = " + searchDepth);

        for (final Move move : board.getLegalMoves()) {
            if (move.getMoveStatus().isDone()) {
                board.execute(move);
                currentValue = board.currentPlayer().getColor().isWhite() ?
                        min(board, searchDepth - 1) :
                        max(board, searchDepth - 1);
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

    public int min(final Board board, final int depth) {
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }

        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.getLegalMoves()) {
            if (move.getMoveStatus().isDone()) {
                board.execute(move);
                final int currentValue = max(board, depth - 1);
                board.takeback(move);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;


    }

    public int max(final Board board, final int depth) {
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }

        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.getLegalMoves()) {
            if (move.getMoveStatus().isDone()) {
                board.execute(move);
                final int currentValue = min(board, depth - 1);
                board.takeback(move);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;

    }
}
