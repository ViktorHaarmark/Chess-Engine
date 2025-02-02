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

public abstract class Player {

    protected final Board board;
    @Getter
    protected final King playerKing;
    @Getter
    protected final List<Move> legalMoves;
    private final boolean isInCheck;


    Player(final Board board,
           final List<Move> possibleMoves,
           final List<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
        this.legalMoves = Stream.concat(
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

    public List<Move> getCaptureMoves() {
        List<Move> captureMoves = new ArrayList<>();
        for (Move move : getLegalMoves()) {
            if (move.isCapture()) {
                captureMoves.add(move);
            }
        }
        return ImmutableList.copyOf(captureMoves);
    }


    private King establishKing() {

        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Should not reach here! No king on this board");
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
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
        for (final Move move : this.legalMoves) {
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

        final Board boardAfterMove = move.execute();

        final List<Move> kingAttacks = Player.calculateAttacksOnTile(boardAfterMove.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                boardAfterMove.currentPlayer().getLegalMoves());

        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(this.board, boardAfterMove, move, MoveStatus.DONE);
    }

    public abstract List<Piece> getActivePieces();

    public abstract Color getColor();

    public abstract Player getOpponent();

    protected abstract List<Move> calculateKingCastlingCollection(List<Move> playerLegals, List<Move> opponentsLegals);

}
