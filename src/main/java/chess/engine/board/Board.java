package chess.engine.board;

import chess.Color;
import chess.engine.Players.BlackPlayer;
import chess.engine.Players.Player;
import chess.engine.Players.WhitePlayer;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.stream.Stream;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final Collection<Piece> allPieces;

    private final WhitePlayer whitePlayer;
    private final boolean whiteKingSideCastle;
    private final boolean whiteQueenSideCastle;
    private final BlackPlayer blackPlayer;
    private final boolean blackKingSideCastle;
    private final boolean blackQueenSideCastle;
    private final Player currentPlayer;

    private final Pawn enPassantPawn;

    @VisibleForTesting
    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Color.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Color.BLACK);
        this.allPieces = calculateAllActivePieces(this.gameBoard);
        this.enPassantPawn = builder.enPassantPawn;

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.whiteKingSideCastle = builder.whiteKingSideCastle;
        this.whiteQueenSideCastle = builder.whiteQueenSideCastle;
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackKingSideCastle = builder.blackKingSideCastle;
        this.blackQueenSideCastle = builder.blackQueenSideCastle;

        this.currentPlayer = builder.nextPlayer.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i+1) % BoardUtils.ROW_LENGTH == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Player blackPlayer() {
        return this.blackPlayer;
    }

    public Player whitePlayer() {
        return this.whitePlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public Collection<Piece> getAllPieces() {
        return this.allPieces;
    }

    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }

    public Piece getPiece(final int coordinate) {
        if (!this.getTile(coordinate).isTileOccupied()) {
            return null;
        }
        return this.getTile(coordinate).getPiece();
    }

    public Collection<Move> calculateLegalMoves(Collection<Piece> pieceList) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final Piece piece : pieceList) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Color color) {
        final List<Piece> activePieces = new ArrayList<>();
        for (final Tile tile : gameBoard) {
            if (tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if (piece.getPieceColor() == color) {
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    private static Collection<Piece> calculateAllActivePieces(final List<Tile> gameBoard) {
        final List<Piece> activePieces = new ArrayList<>();
        for (final Tile tile : gameBoard) {
            if (tile.isTileOccupied()) {
                activePieces.add(tile.getPiece());
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    public Tile getTile(int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles); 
    }
    
    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Color nextPlayer;
        Pawn enPassantPawn;
        boolean whiteKingSideCastle;
        boolean whiteQueenSideCastle;
        boolean blackKingSideCastle;
        boolean blackQueenSideCastle;


        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final Color nextMovemaker) {
            this.nextPlayer = nextMovemaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }

		public void setEnPassant(Pawn enPassantPawn) {
			this.enPassantPawn = enPassantPawn;
		}

        public void setCastlingRights(boolean whiteKingSideCastle, boolean whiteQueenSideCastle, boolean blackKingSideCastle, boolean blackQueenSideCastle) {
            this.whiteKingSideCastle = whiteKingSideCastle;
            this.whiteQueenSideCastle = whiteQueenSideCastle;
            this.blackKingSideCastle = blackKingSideCastle;
            this.blackQueenSideCastle = blackQueenSideCastle;
        }


    }

    public Iterable<Move> getAllLegalMoves() {

    Stream<Move> combinedStream = Stream.concat(
            this.whitePlayer.getLegalMoves().stream(),
            this.blackPlayer.getLegalMoves().stream()
    );
    
    return combinedStream.toList();
}


}
