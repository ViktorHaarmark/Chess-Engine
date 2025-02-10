package chess.engine.board;

import chess.Color;
import chess.engine.Players.BlackPlayer;
import chess.engine.Players.Player;
import chess.engine.Players.WhitePlayer;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.util.*;
import java.util.stream.Stream;

@Getter
public class Board {

    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;

    private final Player currentPlayer;

    private final Pawn enPassantPawn;

    private final List<Tile> gameBoard;

    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Color.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Color.BLACK);
        this.enPassantPawn = builder.enPassantPawn;

        final List<Move> whiteStandardPossibleMoves = calculatePossibleMoves(this.whitePieces);
        final List<Move> blackStandardPossibleMoves = calculatePossibleMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardPossibleMoves, blackStandardPossibleMoves);

        this.blackPlayer = new BlackPlayer(this, whiteStandardPossibleMoves, blackStandardPossibleMoves);

        this.currentPlayer = builder.nextPlayer.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % BoardUtils.ROW_LENGTH == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public List<Piece> getAllPieces() {
        return getAllActivePieces();
    }

    public Piece getPiece(final int coordinate) {
        if (!this.getTile(coordinate).isTileOccupied()) {
            return null;
        }
        return this.getTile(coordinate).getPiece();
    }

    public List<Move> calculatePossibleMoves(List<Piece> pieceList) {
        final List<Move> possibleMoves = new ArrayList<>();
        for (final Piece piece : pieceList) {
            possibleMoves.addAll(piece.calculatePossibleMoves(this));
        }
        return ImmutableList.copyOf(possibleMoves);
    }

    private static List<Piece> calculateActivePieces(final List<Tile> gameBoard, final Color color) {
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

    private List<Piece> getAllActivePieces() {
        final List<Piece> activePieces = new ArrayList<>();
        activePieces.addAll(this.whitePieces);
        activePieces.addAll(this.blackPieces);
        return ImmutableList.copyOf(activePieces);
    }

    public Tile getTile(int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
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

        public void setMoveMaker(final Color nextMovemaker) {
            this.nextPlayer = nextMovemaker;
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

    public Iterable<Move> getAllPossibleMoves() {
        Stream<Move> combinedStream = Stream.concat(
                this.whitePlayer.getPossibleMoves().stream(),
                this.blackPlayer.getPossibleMoves().stream()
        );
        return combinedStream.toList();
    }


}
