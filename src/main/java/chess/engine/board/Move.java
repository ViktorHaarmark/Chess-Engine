package chess.engine.board;

import chess.Color;
import chess.engine.Players.Player;
import chess.engine.pieces.*;

import javax.swing.*;
import java.util.List;

public abstract class Move {

    protected Board board;
    protected Piece movedPiece;
    protected int destinationCoordinate;
    protected MoveStatus moveStatus;

    private static final Move NULL_MOVE = new NullMove(
    );

    Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.moveStatus = calculateMoveStatus();
    }

    protected Move() {
    }

    protected MoveStatus calculateMoveStatus() {
        updatePieceList();
        MoveStatus moveStatus = decideMoveStatus();
        undoMove();
        return moveStatus;

    }

    private MoveStatus decideMoveStatus() {
        Player player = movedPiece.getPieceColor().choosePlayer(board.whitePlayer(), board.blackPlayer());
        if (player.calculateAttackOnSquare(player.getPlayerKing().getPiecePosition())) {
            return MoveStatus.LEAVES_PLAYER_IN_CHECK;
        } else {
            return MoveStatus.DONE;
        }
    }

    Move(final Board board, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = null;
        this.destinationCoordinate = destinationCoordinate;
        this.moveStatus = null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move otherMove)) {
            return false;
        }

        return (getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece() == otherMove.getMovedPiece() &&
                getCurrentCoordinate() == otherMove.getCurrentCoordinate());
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Board getBoard() {
        return this.board;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public boolean isCapture() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public boolean isPromotionMove() {
        return false;
    }

    public Piece getCapturedPiece() {
        return null;
    }

    public Move getDecoratedMove() {
        return this;
    }

    public Piece getPromotedPiece() {
        return null;
    }

    public void addEnPassantPawn() {
        board.enPassantPawnList.add(null);
    }

    public void removeEnPassantPawn() {
        if (!board.enPassantPawnList.isEmpty()) {
            board.enPassantPawnList.remove(board.enPassantPawnList.size() - 1);
        }
    }

    abstract void updatePieceList();

    protected void updateMovedPiece() {
        Piece movedPiece = this.getMovedPiece();
        Piece newPiece = movedPiece.movePiece(this);
        if (movedPiece.getPieceColor().isWhite()) {
            board.whitePieces.remove(movedPiece);
            board.whitePieces.add(newPiece);
            if (newPiece.getPieceType().isKing()) {
                board.whitePlayer().setPlayerKing((King) newPiece);
            }

        } else {
            board.blackPieces.remove(movedPiece);
            board.blackPieces.add(newPiece);
            if (newPiece.getPieceType().isKing()) {
                board.blackPlayer().setPlayerKing((King) newPiece);
            }

        }
        board.gameBoard.set(movedPiece.getPiecePosition(), new Tile.EmptyTile(movedPiece.getPiecePosition()));
        board.gameBoard.set(newPiece.getPiecePosition(), new Tile.OccupiedTile(newPiece.getPiecePosition(), newPiece));
        configureCastlingRights(this.movedPiece.getPieceColor(), false);
        addEnPassantPawn();
    }

    abstract void undoMove();

    protected void undoMovedPiece() {
        Piece movedPiece = this.getMovedPiece();
        Piece newPiece = movedPiece.movePiece(this);
        if (movedPiece.getPieceColor().isWhite()) {
            board.whitePieces.remove(newPiece);
            board.whitePieces.add(movedPiece);
            if (newPiece.getPieceType().isKing()) {
                board.whitePlayer().setPlayerKing((King) movedPiece);
            }
        } else {
            board.blackPieces.remove(newPiece);
            board.blackPieces.add(movedPiece);
            if (newPiece.getPieceType().isKing()) {
                board.blackPlayer().setPlayerKing((King) movedPiece);
            }

        }
        board.gameBoard.set(movedPiece.getPiecePosition(), new Tile.OccupiedTile(movedPiece.getPiecePosition(), movedPiece));
        board.gameBoard.set(newPiece.getPiecePosition(), new Tile.EmptyTile(newPiece.getPiecePosition()));
        configureCastlingRights(movedPiece.getPieceColor(), true);
        removeEnPassantPawn();
    }

    private void configureCastlingRights(Color color, Boolean takeback) {
        PieceType pieceType = movedPiece.getPieceType();
        if (takeback) {
            if (pieceType.isKing() || pieceType.isRook()) {
                if (color.isWhite()) {
                    if (!board.whiteKingsideCastlingRight.isEmpty()) {
                        board.whiteKingsideCastlingRight.remove(board.whiteKingsideCastlingRight.size() - 1);
                        board.whiteQueensideCastlingRight.remove(board.whiteQueensideCastlingRight.size() - 1);
                    }
                } else {
                    if (!board.blackKingsideCastlingRight.isEmpty()) {
                        board.blackKingsideCastlingRight.remove(board.blackKingsideCastlingRight.size() - 1);
                        board.blackQueensideCastlingRight.remove(board.blackQueensideCastlingRight.size() - 1);
                    }
                }
            }
        } else {
            switch (pieceType) {
                case KING: {
                    if (color.isWhite()) {
                        board.whiteQueensideCastlingRight.add(false);
                        board.whiteKingsideCastlingRight.add(false);
                        break;
                    } else if (color.isBlack()) {
                        board.blackQueensideCastlingRight.add(false);
                        board.blackKingsideCastlingRight.add(false);
                        break;
                    }
                }
                case ROOK: {
                    if (color.isWhite()) {
                        if (movedPiece.getPiecePosition() == 63) {
                            board.whiteKingsideCastlingRight.add(false);
                            board.whiteQueensideCastlingRight.add(true);
                        } else if (movedPiece.getPiecePosition() == 56) {
                            board.whiteKingsideCastlingRight.add(true);
                            board.whiteQueensideCastlingRight.add(false);
                        } else {
                            board.whiteKingsideCastlingRight.add(true);
                            board.whiteQueensideCastlingRight.add(true);
                        }
                    } else {
                        if (movedPiece.getPiecePosition() == 7) {
                            board.blackKingsideCastlingRight.add(false);
                            board.blackQueensideCastlingRight.add(true);
                        } else if (movedPiece.getPiecePosition() == 0) {
                            board.blackKingsideCastlingRight.add(true);
                            board.blackQueensideCastlingRight.add(false);
                        } else {
                            board.blackKingsideCastlingRight.add(true);
                            board.blackQueensideCastlingRight.add(true);
                        }
                    }
                }
            }
        }
    }

    protected void removeCapturedPiece() {
        Piece capturedPiece = this.getCapturedPiece();
        if (capturedPiece.getPieceColor().isWhite()) {
            board.whitePieces.remove(capturedPiece);
        } else {
            board.blackPieces.remove(capturedPiece);
        }
    }

    protected void addCapturedPiece() {
        Piece capturedPiece = this.getCapturedPiece();
        int capturedPiecePosition = capturedPiece.getPiecePosition();
        if (capturedPiece.getPieceColor().isWhite()) {
            board.whitePieces.add(capturedPiece);
        } else {
            board.blackPieces.add(capturedPiece);
        }
        board.gameBoard.set(capturedPiecePosition, new Tile.OccupiedTile(capturedPiecePosition, capturedPiece));
    }

    public static final class MajorPieceMove extends Move {

        public MajorPieceMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return (this == other || other instanceof MajorPieceMove && super.equals(other));
        }

        @Override
        void updatePieceList() {
            updateMovedPiece();
        }

        @Override
        void undoMove() {
            undoMovedPiece();
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class CaptureMove extends Move {

        final Piece capturedPiece;

        public CaptureMove(final Board board,
                           final Piece movedPiece,
                           final Piece capturedPiece,
                           final int destinationCoordinate) {
            this.board = board;
            this.movedPiece = movedPiece;
            this.capturedPiece = capturedPiece;
            this.destinationCoordinate = destinationCoordinate;
            this.moveStatus = calculateMoveStatus();
        }


        @Override
        public int hashCode() {
            return this.capturedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CaptureMove otherCaptureMove)) {
                return false;
            }
            return (super.equals(otherCaptureMove) && getCapturedPiece().equals(otherCaptureMove.getCapturedPiece()));

        }

        @Override
        public boolean isCapture() {
            return true;
        }

        @Override
        public Piece getCapturedPiece() {
            return this.capturedPiece;
        }

        @Override
        public void updatePieceList() {
            updateMovedPiece();
            removeCapturedPiece();
        }

        @Override
        void undoMove() {
            undoMovedPiece();
            addCapturedPiece();
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + "x" + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class PawnMove extends Move {

        public PawnMove(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public void updatePieceList() {
            updateMovedPiece();
        }

        @Override
        void undoMove() {
            undoMovedPiece();
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.getDestinationCoordinate());
        }
    }

    public static class PawnCaptureMove extends CaptureMove {

        public PawnCaptureMove(final Board board,
                               final Piece movedPiece,
                               final Piece capturedPiece,
                               final int destinationCoordinate) {
            super(board, movedPiece, capturedPiece, destinationCoordinate);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(movedPiece.getPiecePosition()).charAt(0) + "x" + BoardUtils.getPositionAtCoordinate(this.getDestinationCoordinate());
        }
    }

    public static final class PawnEnPassantMove extends PawnCaptureMove {
        public PawnEnPassantMove(final Board board,
                                 final Piece movedPiece,
                                 final Piece capturedPiece,
                                 final int destinationCoordinate) {
            super(board, movedPiece, capturedPiece, destinationCoordinate);
        }

        protected void removeCapturedPiece() {
            super.removeCapturedPiece();
            board.gameBoard.set(capturedPiece.getPiecePosition(), new Tile.EmptyTile(capturedPiece.getPiecePosition()));
        }

    }


    public static final class PawnJump extends PawnMove {

        public PawnJump(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public void addEnPassantPawn() {
            board.enPassantPawnList.add((Pawn) movedPiece.movePiece(this));
        }

    }

    public static final class PawnPromotionMove extends Move {

        final Move decoratedMove;
        final Pawn promotedPawn;
        Piece promotedPiece;

        private Piece getPromotionPiece(String piece) {
            return switch (piece) {
                case "Queen" -> new Queen(this.destinationCoordinate, this.movedPiece.getPieceColor(), false);
                case "Rook" -> new Rook(this.destinationCoordinate, this.movedPiece.getPieceColor(), false);
                case "Bishop" -> new Bishop(this.destinationCoordinate, this.movedPiece.getPieceColor(), false);
                case "Knight" -> new Knight(this.destinationCoordinate, this.movedPiece.getPieceColor(), false);
                default -> null;
            };

        }

        public PawnPromotionMove(final Move decoratedMove, String promotedPiece) {
            this.board = decoratedMove.getBoard();
            this.movedPiece = decoratedMove.getMovedPiece();
            this.destinationCoordinate = decoratedMove.getDestinationCoordinate();
            this.moveStatus = decoratedMove.getMoveStatus();
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
            this.promotedPiece = getPromotionPiece(promotedPiece);
        }

        @Override
        public boolean isCapture() {
            return this.decoratedMove.isCapture();
        }

        @Override
        public Piece getCapturedPiece() {
            return this.decoratedMove.getCapturedPiece();
        }

        @Override
        public Piece getPromotedPiece() {
            return this.promotedPiece;
        }

        @Override
        void updatePieceList() {
            if (decoratedMove.isCapture()) {
                decoratedMove.removeCapturedPiece();
            }
            removePromotedPawn();
            addPromotedPiece();
        }

        @Override
        void undoMove() {
            decoratedMove.undoMove();

            if (promotedPiece.getPieceColor().isWhite()) {
                board.whitePieces.remove(promotedPiece);
            } else {
                board.blackPieces.remove(promotedPiece);
            }
        }

        private void removePromotedPawn() {
            if (promotedPawn.getPieceColor().isWhite()) {
                board.whitePieces.remove(promotedPawn);
            } else {
                board.blackPieces.remove(promotedPawn);
            }
            board.gameBoard.set(movedPiece.getPiecePosition(), new Tile.EmptyTile(movedPiece.getPiecePosition()));
        }

        private void addPromotedPiece() {
            if (promotedPiece.getPieceColor().isWhite()) {
                board.whitePieces.add(promotedPiece);
            } else {
                board.blackPieces.add(promotedPiece);
            }
            board.gameBoard.set(promotedPiece.getPiecePosition(), new Tile.OccupiedTile(promotedPiece.getPiecePosition(), promotedPiece));

        }

        public Move getDecoratedMove() {
            return this.decoratedMove;
        }

        @Override
        public boolean isPromotionMove() {
            return true;
        }

        @Override
        public String toString() {
            return decoratedMove.toString() + "=" + this.getPromotedPiece().toString();
        }


    }

    static abstract class CastlingMove extends Move {

        protected final Rook castlingRook;
        protected final int castlingRookStartPosition;
        protected final int castlingRookDestination;


        public CastlingMove(final Board board,
                            final King movedPiece,
                            final int destinationCoordinate,
                            final Rook castlingRook,
                            final int castlingRookStartPosition,
                            final int castlingRookDestination) {
            this.board = board;
            this.movedPiece = movedPiece;
            this.destinationCoordinate = destinationCoordinate;
            this.castlingRook = castlingRook;
            this.castlingRookStartPosition = castlingRookStartPosition;
            this.castlingRookDestination = castlingRookDestination;
            this.moveStatus = calculateMoveStatus();
        }

        @Override
        public void updatePieceList() {
            updateMovedPiece();
            updateRook();
        }

        @Override
        void undoMove() {
            undoMovedPiece();
            undoRookMove();

        }

        private void updateRook() {
            Rook newRook = new Rook(this.castlingRookDestination, this.castlingRook.getPieceColor(), false);
            if (this.castlingRook.getPieceColor().isWhite()) {
                board.whitePieces.remove(this.castlingRook);
                board.whitePieces.add(new Rook(this.castlingRookDestination, this.castlingRook.getPieceColor(), false));
            } else {
                board.blackPieces.remove(this.castlingRook);
                board.blackPieces.add(new Rook(this.castlingRookDestination, this.castlingRook.getPieceColor(), this.castlingRook.isFirstMove()));
            }
            board.gameBoard.set(castlingRook.getPiecePosition(), new Tile.EmptyTile(castlingRook.getPiecePosition()));
            board.gameBoard.set(newRook.getPiecePosition(), new Tile.OccupiedTile(newRook.getPiecePosition(), newRook));
        }

        private void undoRookMove() {
            Rook newRook = new Rook(this.castlingRookDestination, this.castlingRook.getPieceColor(), false);
            if (this.castlingRook.getPieceColor().isWhite()) {
                board.whitePieces.remove(newRook);
                board.whitePieces.add(this.castlingRook);
            } else {
                board.blackPieces.remove(newRook);
                board.blackPieces.add(this.castlingRook);
            }
            board.gameBoard.set(castlingRook.getPiecePosition(), new Tile.OccupiedTile(castlingRook.getPiecePosition(), castlingRook));
            board.gameBoard.set(newRook.getPiecePosition(), new Tile.EmptyTile(newRook.getPiecePosition()));
        }

        public Rook getCastlingRook() {
            return this.castlingRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }


    }

    public static final class KingsideCastlingMove extends CastlingMove {

        public KingsideCastlingMove(final Board board,
                                    final King movedPiece,
                                    final int destinationCoordinate,
                                    final Rook castlingRook,
                                    final int castlingRookStartPosition,
                                    final int castlingRookDestination) {
            super(board, movedPiece, destinationCoordinate, castlingRook, castlingRookStartPosition, castlingRookDestination);
        }

        @Override
        public String toString() {
            return "0-0";
        }

    }

    public static final class QueensideCastlingMove extends CastlingMove {

        public QueensideCastlingMove(final Board board,
                                     final King movedPiece,
                                     final int destinationCoordinate,
                                     final Rook castlingRook,
                                     final int castlingRookStartPosition,
                                     final int castlingRookDestination) {
            super(board, movedPiece, destinationCoordinate, castlingRook, castlingRookStartPosition, castlingRookDestination);
        }

        @Override
        public String toString() {
            return "0-0-0";
        }
    }

    public static final class NullMove extends Move {

        public NullMove() {
            super(null, 65);
            this.moveStatus = MoveStatus.ILLEGAL_MOVE;
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }

        @Override
        void updatePieceList() {
        }

        @Override
        void undoMove() {
        }
    }

    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("not instantiable");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
            for (final Move move : board.getLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    if (move instanceof PawnPromotionMove) {
                        return new PawnPromotionMove(move.getDecoratedMove(), getPromotionPiece());
                    }
                    return move;
                }

            }
            return NULL_MOVE;
        }
    }

    private static String getPromotionPiece() {
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

        if (choice != null) {
            return choice;
        }

        // Default to Queen if no valid choice is made
        System.out.println("No valid choice made. Defaulting to Queen.");
        return "Queen";
    }
}
