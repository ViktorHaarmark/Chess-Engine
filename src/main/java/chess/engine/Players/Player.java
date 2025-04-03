package chess.engine.Players;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.board.MoveStatus;
import chess.engine.board.MoveTransition;
import chess.engine.pieces.King;
import chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final List<Move> possibleMoves;
    private final boolean isInCheck;

    Player(final Board board,
           final List<Move> possibleMoves,
           final List<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
        this.possibleMoves = Stream.concat(
                possibleMoves.stream(),
                calculateKingCastlingCollection(possibleMoves, opponentMoves).stream()
        ).toList();
    }

    protected static List<Move> calculateAttacksOnTile(int tileCoordinate, List<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();

        for (final Move move : moves) {
            if (tileCoordinate == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    public List<Move.CaptureMove> getCaptureMoves() {
        List<Move.CaptureMove> captureMoves = new ArrayList<>();
        for (Move move : getPossibleMoves()) {
            if (move.isCapture()) {
                captureMoves.add((Move.CaptureMove) move);
            }
        }
        return ImmutableList.copyOf(captureMoves);
    }


    private King establishKing() {

        for (final Piece piece : getActivePieces()) {
            if (piece instanceof King) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Should not reach here! No king on this board");
    }

    public boolean isMoveLegal(final Move move) {
        return this.possibleMoves.contains(move);
    }

    public boolean isInCheck() {
        return this.isInCheck;
    }

    public boolean isInCheckMate() {
        return (this.isInCheck && hasNoLegalMove());
    }

    public boolean isInStaleMate() {
        return (!this.isInCheck && hasNoLegalMove());
    }

    public boolean isKingSideCastleCapable() {
        return this.playerKing.isKingSideCastleCapable();
    }

    public boolean isQueenSideCastleCapable() {
        return this.playerKing.isQueenSideCastleCapable();
    }

    protected boolean hasNoLegalMove() {
        for (final Move move : this.possibleMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.moveStatus().isDone()) {
                return false;
            }
        }
        return true;
    }

    public boolean isCastled() {
        return false;
    }

    public MoveTransition makeMove(final Move move) {

        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board boardAfterMove = board.execute(move);

        final List<Move> kingAttacks = Player.calculateAttacksOnTile(boardAfterMove.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                boardAfterMove.getCurrentPlayer().getPossibleMoves());

        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(this.board, boardAfterMove, move, MoveStatus.DONE);
    }

    public abstract List<Piece> getActivePieces();

    public abstract Color getColor();

    public abstract Player getOpponent();

    protected abstract List<Move> calculateKingCastlingCollection(List<Move> playerPossibleMoves, List<Move> opponentsPossibleMoves);

//    public List<Move> getLegalMoves() {
//
//        if (this.getLEGAL_MOVES_CACHE() != null) {
//            return getLEGAL_MOVES_CACHE();
//        } else {
//            List<Move> legalMoves = new ArrayList<>();
//            List<Move> possibleMoves = getPossibleMoves();
//            for (Move move : possibleMoves) {
//                if (this.makeMove(move).getMoveStatus().isDone()) {
//                    legalMoves.add(move);
//                    //TODO: Fill the capture move cache
//                }
//            }
//            setLEGAL_MOVES_CACHE(legalMoves);
//            return legalMoves;
//        }
}


