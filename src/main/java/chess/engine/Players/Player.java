package chess.engine.Players;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.ImmutableList;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.board.MoveStatus;
import chess.engine.board.MoveTransition;
import chess.engine.pieces.King;
import chess.engine.pieces.Piece;

public abstract class Player {
    
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    
    Player(final Board board,
            final Collection<Move> legalMoves,
            final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = Collections.unmodifiableList(Stream.concat(
            StreamSupport.stream(legalMoves.spliterator(), false),
            StreamSupport.stream(calculateKingCastlingCollection(legalMoves, opponentMoves).spliterator(), false)
            ).toList());
        this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }
    
    protected static Collection<Move> calculateAttacksOnTile(int tileCoordinate, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();

        for (final Move move : moves) {
            if (tileCoordinate == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }
    
    public King getPlayerKing() {
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }


    private King establishKing() {

        for(final Piece piece : getActivePieces()) {
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
        return (this.isInCheck && !hasLegalMove());
    }

    public boolean isInStaleMate() {
        return (!this.isInCheck && !hasLegalMove());
    }

    public boolean isKingSideCastleCapable() {
        return this.playerKing.isKingSideCastleCapable();
    }

    public boolean isQueenSideCastleCapable() {
        return this.playerKing.isQueenSideCastleCapable();
    }

    protected boolean hasLegalMove() {
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    public boolean isCastled() {
        return false;
    }

    public MoveTransition makeMove(final Move move) {

        if(!isMoveLegal(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board boardAfterMove = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(boardAfterMove.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                boardAfterMove.currentPlayer().getLegalMoves());

        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(this.board, boardAfterMove, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Color getColor();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastlingCollection(Collection<Move> playerLegals, Collection<Move> opponentsLegals); 
}
