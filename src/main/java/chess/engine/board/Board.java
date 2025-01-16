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

public class Board {

    private final List<Tile> gameBoard;
    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;

    private final HashSet<Integer> opponentBoardControl;
    private final List<Move> legalMoves;

    private final Player currentPlayer;

    private final Pawn enPassantPawn;

    @VisibleForTesting
    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Color.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Color.BLACK);
        this.enPassantPawn = builder.enPassantPawn;

        this.whitePlayer = new WhitePlayer(this);

        this.blackPlayer = new BlackPlayer(this);

        this.currentPlayer = builder.nextPlayer.choosePlayer(this.whitePlayer, this.blackPlayer);

        this.opponentBoardControl = currentPlayer.getOpponent().controlSquares();
        this.legalMoves = createLegalMoves();
    }

    private List<Move> createLegalMoves() {
        List<Move> legalStandardMoves = currentPlayer().createLegalStandardMoves();
        List<Move> castlingMoves = currentPlayer().getCastlingMoves();
        List<Move> legalMoves = new ArrayList<>(legalStandardMoves);
        for (Move move : castlingMoves) {
            if (move.isLegal()) {
                legalMoves.add(move);
            }
        }
        return ImmutableList.copyOf(legalMoves);
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

    public List<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public List<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }

    public List<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public HashSet<Integer> getOpponentBoardControl() {
        return this.opponentBoardControl;
    }

    public Piece getPiece(final int coordinate) {
        if (!this.getTile(coordinate).isTileOccupied()) {
            return null;
        }
        return this.getTile(coordinate).getPiece();
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

    public List<Piece> getAllActivePieces() {
        final List<Piece> activePieces = new ArrayList<>();
        activePieces.addAll(this.whitePieces);
        activePieces.addAll(this.blackPieces);
        return ImmutableList.copyOf(activePieces);
    }

    public static boolean isSquareAttackedByPieceList(int square, List<Piece> pieceList, HashSet<Integer> nonEmptySquares) {
        HashSet<Integer> controlledSquares = new HashSet<>();
        for (Piece piece : pieceList) {
            controlledSquares.addAll(piece.controlSquares(nonEmptySquares));
        }
        return controlledSquares.contains(square);
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


}
