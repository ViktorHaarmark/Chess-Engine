package chess.engine.Players;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Tile;
import chess.engine.pieces.King;
import chess.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static chess.engine.pieces.Knight.isValidKnightMove;
import static chess.engine.pieces.Piece.*;

public abstract class Player {
    int[] pawnThreat;

    public Board board;
    public King playerKing;
    public boolean isInCheck;


    Player(final Board board) {
        this.board = board;
        this.setPlayerKing( establishKing());
        this.isInCheck = false;
    }

    public King getPlayerKing() {
        return this.playerKing;
    }

    public void setPlayerKing(King king) {
        this.playerKing = king;
    }

    public boolean calculateAttackOnSquare(int targetSquare) {
        Color opponentColor = this.getOpponent().getColor();
        int[] knightMoves = {-17, -15, -10, -6, 6, 10, 15, 17};
        for (int knightMove : knightMoves) {
            if (isValidKnightMove(knightMove, targetSquare + knightMove)) {
                Tile tile = board.getTile(targetSquare + knightMove);
                if (tile.isTileOccupied()) {
                    if (tile.getPiece().getPieceColor() == opponentColor && tile.getPiece().getPieceType().isKnight()) {
                        return true;
                    }
                }
            }
        }
        int[] rookMoves = {1, -1, 8, -8};
        for (int rookMove : rookMoves) {
            for (int i = 1; i < 8; i++) {
                int targetCoordinate = targetSquare + i * rookMove;
                if (isValidRookMove(targetSquare, targetCoordinate)) {
                    Tile tile = board.getTile(targetCoordinate);
                    if (tile.isTileOccupied()) {
                        if (tile.getPiece().getPieceColor() == opponentColor && (tile.getPiece().getPieceType().isRook() || tile.getPiece().getPieceType().isQueen())) {
                            return true;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        int[] bishopMoves = {9, 7, -7, -9};
        for (int bishopMove : bishopMoves) {
            for (int i = 1; i < 8; i++) {
                int targetCoordinate = targetSquare + i * bishopMove;
                if (isValidBishopMove(targetSquare, targetCoordinate)) {
                    Tile tile = board.getTile(targetCoordinate);
                    if (tile.isTileOccupied()) {
                        if (tile.getPiece().getPieceColor() == opponentColor && (tile.getPiece().getPieceType().isBishop() || tile.getPiece().getPieceType().isQueen())) {
                            return true;
                        } else {
                            break;
                        }
                        //if (i == 1 && this.pawnThreat.contains(bishopMove)) TODO: add pawn check here
                    }
                }
            }
        }
        int[] kingMoves = {1, -1, 8, -8, 9, 7, -7, -9};
        for (int kingMove : kingMoves) {
            int targetCoordinate = targetSquare + kingMove;
            if (BoardUtils.isValidTileCoordinate(targetCoordinate) && BoardUtils.rowDifference(targetCoordinate, targetSquare) + BoardUtils.columnDifference(targetCoordinate, targetSquare) <= 2) {
                Tile tile = board.getTile(targetCoordinate);
                if (tile.isTileOccupied()) {
                    if (tile.getPiece().getPieceColor() != this.playerKing.getPieceColor() && tile.getPiece().getPieceType().isKing()) {
                        return true;
                    }
                }
            }
        }
        for (int pawnAttacks : this.pawnThreat) {
            int targetCoordinate = targetSquare + pawnAttacks;
            if (BoardUtils.isValidTileCoordinate(targetCoordinate)) {
                Tile tile = board.getTile(targetCoordinate);
                if (tile.isTileOccupied()) {
                    if (tile.getPiece().getPieceColor() != this.playerKing.getPieceColor() && tile.getPiece().getPieceType().isPawn()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public King establishKing() {

        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing() && piece.getPieceColor() == this.getColor()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Should not reach here! No king on this board");
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
        if (this.getColor().isWhite()) {
            return !this.board.getWhiteKingsideCastlingRight().contains(false);
        } else if (this.getColor().isBlack()) {
            return !this.board.getBlackKingsideCastlingRight().contains(false);
        }
        return true;
    };

    public boolean isQueenSideCastleCapable() {
        if (this.getColor().isWhite()) {
            return !this.board.getWhiteQueensideCastlingRight().contains(false);
        } else if (this.getColor().isBlack()) {
            return !this.board.getBlackQueensideCastlingRight().contains(false);
        }
        return true;
    }

    protected boolean hasNoLegalMove() {
        return board.getLegalMoves().isEmpty();
    }

    public boolean isCastled() {
        return false;
    }

    public abstract List<Piece> getActivePieces();

    public abstract Color getColor();

    public abstract Player getOpponent();

    public abstract List<Move> calculateCastlingMoves(List<Move> opponentsLegals);
}
