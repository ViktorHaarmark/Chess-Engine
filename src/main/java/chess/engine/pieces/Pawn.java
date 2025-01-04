package chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.PawnCaptureMove;
import chess.engine.board.Move.PawnEnPassantMove;
import chess.engine.board.Move.PawnJump;
import chess.engine.board.Move.PawnMove;
import chess.engine.board.Move.PawnPromotionMove;

public class Pawn extends Piece {

    private final int[] DIRECTION = {8, 7, 9};

    private final String[] PromoList = {"Queen", "Rook", "Bishop", "Knight"};

    public Pawn(final int piecePosition, final Color color, final boolean isFirstMove) {
        super(piecePosition, color, PieceType.PAWN, isFirstMove);
    }
    
    public Pawn(final int piecePosition, final Color color) {
        super(piecePosition, color, PieceType.PAWN, false);
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<Move>();

        for (final int direction : DIRECTION) {
            int candidateDestinationCoordinate = this.piecePosition + direction * this.getPieceColor().getDirection();

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if (direction == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                if (this.getPieceColor().isPromotionRow(candidateDestinationCoordinate)) {
                    for (String piece : PromoList) {
                        legalMoves.add(new PawnPromotionMove((new PawnMove(board, this, candidateDestinationCoordinate)), piece));
                    }
                } else {
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
                if (this.color.isSecondRank(this.piecePosition)) {
                    candidateDestinationCoordinate = this.piecePosition + 2 * direction * this.getPieceColor().getDirection();
                    if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate) && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                        legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                    }
                } 
            }

            else if (board.getTile(candidateDestinationCoordinate).isTileOccupied() ) {
                if (BoardUtils.columnDifference(candidateDestinationCoordinate, this.piecePosition) == 1 && BoardUtils.rowDifference(candidateDestinationCoordinate, this.piecePosition) == 1) {
                    final Piece pieceOnDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (pieceOnDestination.getPieceColor() != this.color) {
                        if (this.getPieceColor().isPromotionRow(candidateDestinationCoordinate)) {
                            for (String piece : PromoList) {
                                legalMoves.add(new PawnPromotionMove(new PawnCaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate), piece));
                            }
                        } else {
                            legalMoves.add (new PawnCaptureMove(board, this, pieceOnDestination, candidateDestinationCoordinate) );
                        }
                    }
                }
            }

            else if (board.getEnPassantPawn() != null) {
                if(board.getEnPassantPawn().getPiecePosition() + this.color.getDirection() * BoardUtils.ROW_LENGTH == candidateDestinationCoordinate && BoardUtils.columnDifference(board.getEnPassantPawn().getPiecePosition(), this.piecePosition) == 1 && BoardUtils.rowDifference(board.getEnPassantPawn().getPiecePosition(), this.piecePosition) == 0 ) {
                    final Pawn enPassantPawn = board.getEnPassantPawn();
                    legalMoves.add (new PawnEnPassantMove(board, this, enPassantPawn, candidateDestinationCoordinate));
                }
            }
                
        }
        return legalMoves;
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }



    public Piece getPromotionPiece2() {
        // Options for promotion
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
    
        // Show dialog to the user
        String choice = (String) JOptionPane.showInputDialog(
            null,
            "Choose a piece for promotion:",
            "Pawn Promotion",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            "Queen" // Default selection
        );

    // Return the selected piece
    if (choice != null) {
        switch (choice) {
            case "Queen":
                return new Queen(this.piecePosition, this.color, false);
            case "Rook":
                return new Rook(this.piecePosition, this.color, false);
            case "Bishop":
                return new Bishop(this.piecePosition, this.color, false);
            case "Knight":
                return new Knight(this.piecePosition, this.color, false);
        }
    }

    // Default to Queen if no valid choice is made
    System.out.println("No valid choice made. Defaulting to Queen.");
    return new Queen(this.piecePosition, this.color, false);
    }
    
}
