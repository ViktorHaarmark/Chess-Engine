package chess.engine.board;

import chess.engine.board.Board.Builder;
import chess.engine.pieces.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.swing.*;

@Getter
@EqualsAndHashCode
public abstract class Move implements ChessMove{

    @Getter
    protected Board board;
    @Getter
    protected Piece movedPiece;
    @Getter
    protected int destinationCoordinate;
    protected final boolean isFirstMove;

    private static final Move NULL_MOVE = new NullMove();

    Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    Move(final Board board, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = null;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = false;
    }


    public String getUCIMove() {
        return BoardUtils.getPositionAtCoordinate(
                movedPiece.getPiecePosition()) +
                BoardUtils.getPositionAtCoordinate(destinationCoordinate);
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public boolean isCapture() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public boolean isPromotionMove() {return false;}

    public Piece getCapturedPiece() {
        return null;
    }

    public Move getDecoratedMove() {
        return this;
    }

    public Piece getPromotedPiece() {
        return null;
    }

    public Board execute() {
        final Builder builder = new Builder();

        for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece); //TODO: This might be shitty, why not just copy the list?
            }
        }

        for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setCastlingRights(this.board.getCurrentPlayer().isKingSideCastleCapable(),
                this.board.getCurrentPlayer().isQueenSideCastleCapable(),
                this.board.getCurrentPlayer().getOpponent().isKingSideCastleCapable(),
                this.board.getCurrentPlayer().getOpponent().isQueenSideCastleCapable());

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getColor());

        return builder.build();
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
            super(board, movedPiece, destinationCoordinate);
            this.capturedPiece = capturedPiece;
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
        public Board execute() {
            return super.execute();
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

        @Override
        public Board execute() {
            final Builder builder = new Builder();

            for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                if (!piece.equals((this.getCapturedPiece()))) {
                    builder.setPiece(piece);
                }
            }

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getColor());

            return builder.build();
        }

    }


    public static final class PawnJump extends PawnMove {

        public PawnJump(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassant(movedPawn);
            builder.setMoveMaker((this.board.getCurrentPlayer().getOpponent().getColor()));
            return builder.build();
        }
    }

    public static final class PawnPromotionMove extends Move {

        @Getter
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
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
            this.promotedPiece = getPromotionPiece(promotedPiece);
        }

        @Override
        public Board execute() {

            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Board.Builder builder = new Builder();
            for (final Piece piece : pawnMovedBoard.getCurrentPlayer().getActivePieces()) {
                if (!this.promotedPawn.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : pawnMovedBoard.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(promotedPiece);

            builder.setMoveMaker((this.board.getCurrentPlayer().getOpponent().getColor()));
            return builder.build();

        }

        @Override
        public String getUCIMove() {
            return BoardUtils.getPositionAtCoordinate(
                    promotedPawn.getPiecePosition()) +
                    BoardUtils.getPositionAtCoordinate(destinationCoordinate) +
                    promotedPiece.toString().toLowerCase();
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
        public boolean isPromotionMove() {
            return true;
        }

        @Override
        public String toString() {
            return decoratedMove.toString() + "=" + this.getPromotedPiece().toString();
        }


    }

    static abstract class CastlingMove extends Move {

        @Getter
        protected final Rook castlingRook;
        protected final int castlingRookStartPosition;
        protected final int castlingRookDestination;


        public CastlingMove(final Board board,
                            final Piece movedPiece,
                            final int destinationCoordinate,
                            final Rook castlingRook,
                            final int castlingRookStartPosition,
                            final int castlingRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castlingRook = castlingRook;
            this.castlingRookStartPosition = castlingRookStartPosition;
            this.castlingRookDestination = castlingRookDestination;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece) && !this.castlingRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final King castlingKing = (King) this.movedPiece.movePiece(this);
            builder.setPiece(castlingKing);
            builder.setPiece(new Rook(castlingRookDestination, this.castlingRook.getPieceColor(), false));

            builder.setMoveMaker((this.board.getCurrentPlayer().getOpponent().getColor()));
            return builder.build();

        }

    }

    public static final class KingsideCastlingMove extends CastlingMove {

        public KingsideCastlingMove(final Board board,
                                    final Piece movedPiece,
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
                                     final Piece movedPiece,
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
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute the null move");
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }
    }

    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("not instantiable");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
            for (final Move move : board.getAllPossibleMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    if (move instanceof PawnPromotionMove) {
                        return new PawnPromotionMove(move.getDecoratedMove(), getPromotionPiece());
                    }
                    return move;
                }

            }
            return NULL_MOVE;
        }

        public static Move createLichessMove(final Board board, final int currentCoordinate, final int destinationCoordinate, final String promotionPiece) {
            for (final Move move : board.getAllPossibleMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    if (move instanceof PawnPromotionMove) {
                        return new PawnPromotionMove(move.getDecoratedMove(), getPromotionPiece(promotionPiece));
                    }
                    return move;
                }

            }
            throw new RuntimeException("Cannot make the move");
        }
    }

    private static String getPromotionPiece(String piece) {
        return switch (piece) {
            case "q" -> "Queen";
            case "r" -> "Rook";
            case "b" -> "Bishop";
            case "n" -> "Knight";
            default -> "Queen";
        };
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
