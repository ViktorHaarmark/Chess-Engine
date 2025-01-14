package chess.engine.pieces;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.*;

import java.util.ArrayList;
import java.util.List;

import static chess.engine.pieces.PieceType.PAWN;

public class Pawn extends Piece {

    private final int[] DIRECTION = {8, 7, 9};

    private final String[] PromoList = {"Queen", "Rook", "Bishop", "Knight"};

    public Pawn(final int piecePosition, final Color color, final boolean isFirstMove) {
        super(piecePosition, color, PAWN, isFirstMove);
    }

    public Pawn(final int piecePosition, final Color color) {
        super(piecePosition, color, PAWN, false);
    }

    @Override
    public List<Move> calculatePossibleMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int direction : DIRECTION) {
            int candidateDestinationCoordinate = this.piecePosition + direction * this.getPieceColor().getDirection();

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if (direction == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                Move move = new PawnMove(board, this, candidateDestinationCoordinate);
                if (move.getMoveStatus().isDone()) {
                    if (this.getPieceColor().isPromotionRow(candidateDestinationCoordinate)) {
                        for (String piece : PromoList) {
                            legalMoves.add(new PawnPromotionMove(move, piece));
                        }
                    } else {
                        legalMoves.add(move);
                    }
                }
                if (this.color.isSecondRank(this.piecePosition)) {
                    candidateDestinationCoordinate = this.piecePosition + 2 * direction * this.getPieceColor().getDirection();
                    if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate) && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                        move = new PawnJump(board, this, candidateDestinationCoordinate);
                        if (move.getMoveStatus().isDone()) {
                            legalMoves.add(move);
                        }
                    }
                }
            } else if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                if (BoardUtils.columnDifference(candidateDestinationCoordinate, this.piecePosition) == 1 && BoardUtils.rowDifference(candidateDestinationCoordinate, this.piecePosition) == 1) {
                    final Piece pieceOnDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (pieceOnDestination.getPieceColor() != this.color) {
                        Move move = new PawnCaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate);
                        if (move.getMoveStatus().isDone()) {
                            if (this.getPieceColor().isPromotionRow(candidateDestinationCoordinate)) {
                                for (String piece : PromoList) {
                                    legalMoves.add(new PawnPromotionMove(move, piece));
                                }
                            } else {
                                legalMoves.add(move);
                            }
                        }
                    }
                }
            } else if (board.getEnPassantPawn() != null) {
                Pawn enPassantPawn = board.getEnPassantPawn();
                if (enPassantPawn.getPiecePosition() + this.color.getDirection() * BoardUtils.ROW_LENGTH == candidateDestinationCoordinate && BoardUtils.columnDifference(enPassantPawn.getPiecePosition(), this.piecePosition) == 1 && BoardUtils.rowDifference(enPassantPawn.getPiecePosition(), this.piecePosition) == 0) {
                    Move move = new PawnEnPassantMove(board, this, enPassantPawn, candidateDestinationCoordinate);
                    if (move.getMoveStatus().isDone()) {
                        legalMoves.add(move);
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public String toString() {
        return PAWN.toString();
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }
}
