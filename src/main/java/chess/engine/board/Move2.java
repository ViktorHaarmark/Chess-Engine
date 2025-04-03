package chess.engine.board;

import chess.engine.pieces.Piece;

public class Move2  {
    private Board board;
    private int fromSquare;
    private int toSquare;
    private Piece movedPiece;

    private boolean isCapture;
    private Piece capturedPiece;

    private boolean isPromotion;
    private Piece promotionPiece;

    private boolean isCastle;
    private boolean isEnPassant;
    private boolean isDoublePawnMove;

    public int fromSquare() {
        return fromSquare;
    }


    public int toSquare() {
        return toSquare;
    }


    public boolean isCapture() {
        return isCapture;
    }


    public Piece movedPiece() {
        return movedPiece;
    }


    public boolean isPromotion() {
        return isPromotion;
    }


    public Piece getPromotionPiece() {
        return this.promotionPiece;
    }


    public boolean isCastle() {
        return isCastle;
    }


    public boolean isEnPassant() {
        return false;
    }


    public boolean isDoublePawnMove() {
        return false;
    }


    public String getUCIMove() {
        return BoardUtils.getPositionAtCoordinate(fromSquare) + BoardUtils.getPositionAtCoordinate(toSquare); //TODO: Add promotion piece?
    }

}
