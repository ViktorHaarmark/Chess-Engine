package chess.engine.pieces;

import chess.Color;
import chess.engine.Players.ai.PieceSquareTables;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.*;

import java.util.ArrayList;
import java.util.HashSet;
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
        final List<Move> possibleMoves = new ArrayList<>();

        for (final int direction : DIRECTION) {
            int candidateDestinationCoordinate = this.piecePosition + direction * this.getPieceColor().getDirection();

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if (direction == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                if (this.getPieceColor().isPromotionRow(candidateDestinationCoordinate)) {
                    for (String piece : PromoList) {
                        possibleMoves.add(new PawnPromotionMove((new PawnMove(board, this, candidateDestinationCoordinate)), piece));
                    }
                } else {
                    possibleMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
                if (this.color.isSecondRank(this.piecePosition)) {
                    candidateDestinationCoordinate = this.piecePosition + 2 * direction * this.getPieceColor().getDirection();
                    if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate) && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                        possibleMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                    }
                }
            } else if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                if (BoardUtils.columnDifference(candidateDestinationCoordinate, this.piecePosition) == 1 && BoardUtils.rowDifference(candidateDestinationCoordinate, this.piecePosition) == 1) {
                    final Piece pieceOnDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (pieceOnDestination.getPieceColor() != this.color) {
                        if (this.getPieceColor().isPromotionRow(candidateDestinationCoordinate)) {
                            for (String piece : PromoList) {
                                possibleMoves.add(new PawnPromotionMove(new PawnCaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate), piece));
                            }
                        } else {
                            possibleMoves.add(new PawnCaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate));
                        }
                    }
                }
            } else if (board.getEnPassantPawn() != null) {
                if (board.getEnPassantPawn().getPiecePosition() + this.color.getDirection() * BoardUtils.ROW_LENGTH == candidateDestinationCoordinate && BoardUtils.columnDifference(board.getEnPassantPawn().getPiecePosition(), this.piecePosition) == 1 && BoardUtils.rowDifference(board.getEnPassantPawn().getPiecePosition(), this.piecePosition) == 0) {
                    final Pawn enPassantPawn = board.getEnPassantPawn();
                    possibleMoves.add(new PawnEnPassantMove(board, this, enPassantPawn, candidateDestinationCoordinate));
                }
            }

        }
        return possibleMoves;
    }

    public HashSet<Integer> controlSquares(final HashSet<Integer> nonEmptySquares) {
        HashSet<Integer> controlledSquares = new HashSet<>();
        int candidateDestinationCoordinate;

        for (final int direction : DIRECTION) {
            if (direction != 8) {
                candidateDestinationCoordinate = this.piecePosition + direction * this.color.getDirection();
                controlledSquares.add(candidateDestinationCoordinate);
            }
        }
        return controlledSquares;
    }

    @Override
    public String toString() {
        return PAWN.toString();
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }

    @Override
    protected int[] getPieceSquareTable() {
        return PieceSquareTables.PAWN_SQUARE_TABLE;
    }


}
