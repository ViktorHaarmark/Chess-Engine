package chess.engine.board;

import javax.swing.JOptionPane;

import chess.engine.board.Board.Builder;
import chess.engine.pieces.Bishop;
import chess.engine.pieces.King;
import chess.engine.pieces.Knight;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Queen;
import chess.engine.pieces.Rook;

public abstract class Move {

    protected Board board;
    protected Piece movedPiece;
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

    public Piece getCapturedPiece() {
        return null;
    }

    public Move getDecoratedMove() {
        return this;
    }

    public Board execute() {
        final Builder builder = new Builder();

        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setCastlingRights(this.board.currentPlayer().isKingSideCastleCapable(),
                this.board.currentPlayer().isQueenSideCastleCapable(),
                this.board.currentPlayer().getOpponent().isKingSideCastleCapable(),
                this.board.currentPlayer().getOpponent().isQueenSideCastleCapable());

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());

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

            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                if (!piece.equals((this.getCapturedPiece()))) {
                    builder.setPiece(piece);
                }
            }

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());

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
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassant(movedPawn);
            builder.setMoveMaker((this.board.currentPlayer().getOpponent().getColor()));
            return builder.build();
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
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
            this.promotedPiece = getPromotionPiece(promotedPiece);
        }

        @Override
        public Board execute() {

            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Board.Builder builder = new Builder();
            for (final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()) {
                if (!this.promotedPawn.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(promotedPiece);

            builder.setMoveMaker((this.board.currentPlayer().getOpponent().getColor()));
            return builder.build();
        }

        @Override
        public boolean isCapture() {
            return this.decoratedMove.isCapture();
        }

        @Override
        public Piece getCapturedPiece() {
            return this.decoratedMove.getCapturedPiece();
        }

        public Piece getPromotedPiece() {
            return this.promotedPiece;
        }

        public Move getDecoratedMove() {
            return this.decoratedMove;
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

        public Rook getCastlingRook() {
            return this.castlingRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece) && !this.castlingRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final King castlingKing = (King) this.movedPiece.movePiece(this);
            builder.setPiece(castlingKing);
            builder.setPiece(new Rook(castlingRookDestination, this.castlingRook.getPieceColor(), false));

            builder.setMoveMaker((this.board.currentPlayer().getOpponent().getColor()));
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
            for (final Move move : board.getAllLegalMoves()) {
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
