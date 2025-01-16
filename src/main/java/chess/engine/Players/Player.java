package chess.engine.Players;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.board.MoveStatus;
import chess.engine.board.MoveTransition;
import chess.engine.pieces.King;
import chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.*;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;


    Player(final Board board) {
        this.board = board;
        this.playerKing = establishKing();
    }

    public King getPlayerKing() {
        return this.playerKing;
    }

    public List<Move> getLegalMoves() {
        if (this.equals(board.currentPlayer())) {
            return board.getLegalMoves();
        } else return null;
    }

    public HashSet<Integer> controlSquares() {
        HashSet<Integer> controlledSquares = new HashSet<>();
        List<Move> possibleMoves = this.calculatePossibleMoves();
        for (Move move : possibleMoves) {
            controlledSquares.add(move.getMovedPiece().getPiecePosition());
        }
        return controlledSquares;
    }

    public List<Move> calculatePossibleMoves() {
        List<Piece> pieceList = getActivePieces();
        final List<Move> possibleMoves = new ArrayList<>();
        for (final Piece piece : pieceList) {
            possibleMoves.addAll(piece.calculateLegalMoves(this.board));
        }
        return ImmutableList.copyOf(possibleMoves);
    }

    public List<Move> calculatePossibleMoves(List<Piece> pieceList, Color pieceColor) {
        final List<Move> possibleMoves = new ArrayList<>();
        for (final Piece piece : pieceList) {
            if (piece.getPieceColor().equals(pieceColor)) {
                possibleMoves.addAll(piece.calculateLegalMoves(this.board));
            }
        }
        return ImmutableList.copyOf(possibleMoves);
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
        return move.isLegal();
    }

    public boolean isInCheck() {
        return board.getOpponentBoardControl().contains(this.getPlayerKing().getPiecePosition());
    }

    public boolean isInCheckMate() {
        return (this.isInCheck() && hasNoLegalMove());
    }

    public boolean isInStaleMate() {
        return (!this.isInCheck() && hasNoLegalMove());
    }

    public boolean isKingSideCastleCapable() {
        return this.playerKing.isKingSideCastleCapable();
    }

    public boolean isQueenSideCastleCapable() {
        return this.playerKing.isQueenSideCastleCapable();
    }

    protected boolean hasNoLegalMove() {
        if (this.equals(board.currentPlayer())) {
            return board.getLegalMoves().isEmpty();
        }
        return false;
    }

    public boolean isCastled() { //TODO: not implemented
        return false;
    }

    public MoveTransition makeMove(final Move move) {

        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board boardAfterMove = move.execute();

        return new MoveTransition(this.board, boardAfterMove, move, MoveStatus.DONE);
    }

    public abstract List<Piece> getActivePieces();

    public abstract Color getColor();

    public abstract Player getOpponent();

    public abstract List<Move> getCastlingMoves();

    public List<Move> createLegalStandardMoves() { //Standard moves are non-castling moves
        List<Move> possibleMoves = this.calculatePossibleMoves();
        List<Move> legalStandardMoves = new ArrayList<>();
        for (Move move : possibleMoves) {
            if (move.isLegal()) {
                legalStandardMoves.add(move);
            }
        }
        return ImmutableList.copyOf(legalStandardMoves);
    }
}
