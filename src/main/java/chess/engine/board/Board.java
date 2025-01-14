package chess.engine.board;

import chess.Color;
import chess.engine.Players.BlackPlayer;
import chess.engine.Players.Player;
import chess.engine.Players.WhitePlayer;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import com.google.common.annotations.VisibleForTesting;

import java.util.*;

public class Board {

    List<Tile> gameBoard;
    List<Piece> whitePieces;
    List<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    public List<Move> whiteMoves = null;
    public List<Move> blackMoves = null;

    private Player currentPlayer;

    List<Pawn> enPassantPawnList;
    List<Boolean> whiteKingsideCastlingRight = new ArrayList<>();
    List<Boolean> whiteQueensideCastlingRight = new ArrayList<>();
    List<Boolean> blackKingsideCastlingRight = new ArrayList<>();
    List<Boolean> blackQueensideCastlingRight = new ArrayList<>();

    @VisibleForTesting
    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Color.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Color.BLACK);
        this.enPassantPawnList = builder.enPassantPawnList;

        this.whitePlayer = new WhitePlayer(this);
        this.blackPlayer = new BlackPlayer(this);
        this.currentPlayer = builder.nextPlayer.choosePlayer(this.whitePlayer, this.blackPlayer);
        this.whiteKingsideCastlingRight.add(builder.whiteKingSideCastle);
        this.whiteQueensideCastlingRight.add(builder.whiteQueenSideCastle);
        this.blackKingsideCastlingRight.add(builder.blackKingSideCastle);
        this.blackQueensideCastlingRight.add(builder.blackQueenSideCastle);

        this.whiteMoves = calculateLegalMoves(Color.WHITE);
        this.blackMoves = calculateLegalMoves(Color.BLACK);
        blackMoves.addAll(blackPlayer.calculateCastlingMoves(whiteMoves));
        whiteMoves.addAll(whitePlayer.calculateCastlingMoves(blackMoves));
    }

    public List<Boolean> getWhiteKingsideCastlingRight() {
        return this.whiteKingsideCastlingRight;
    }

    public List<Boolean> getWhiteQueensideCastlingRight() {
        return this.whiteQueensideCastlingRight;
    }

    public List<Boolean> getBlackKingsideCastlingRight() {
        return this.blackKingsideCastlingRight;
    }

    public List<Boolean> getBlackQueensideCastlingRight() {
        return this.blackQueensideCastlingRight;
    }

    private List<Move> calculateLegalMoves(Color color) {
        List<Piece> pieces = new ArrayList<>();
        if (color.isWhite()) {
            pieces.addAll(this.whitePieces);
        } else {
            pieces.addAll(this.blackPieces);
        }
        final List<Move> legalMoves = new ArrayList<>();
        for (final Piece piece : pieces) {
            List<Move> pieceMoves = piece.calculatePossibleMoves(this);
            if (pieceMoves != null) {
                legalMoves.addAll(pieceMoves);
            }
        }
        return legalMoves;
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

    public void execute(Move move) {
        updatePieceListsAndGameState(move);
        updatePlayers(false);
        changeCurrentPlayer();
        updateLegalMoves();
    }

    public void takeback(Move move) {
        takebackPieceListsAndGameState(move);
        updatePlayers(true);
        changeCurrentPlayer();
        updateLegalMoves();
    }

    private void updatePlayers(Boolean takeback) {
        whitePlayer.setPlayerKing(whitePlayer.establishKing());
        blackPlayer.setPlayerKing(blackPlayer.establishKing());//TODO: remove probably
        whitePlayer.isInCheck = whitePlayer.calculateAttackOnSquare(this.whitePlayer.playerKing.getPiecePosition());
        blackPlayer.isInCheck = blackPlayer.calculateAttackOnSquare(this.blackPlayer.playerKing.getPiecePosition());
    }

    private void updateLegalMoves() {
        whiteMoves = calculateLegalMoves(Color.WHITE);
        whiteMoves.addAll(whitePlayer.calculateCastlingMoves(blackMoves));
        blackMoves = calculateLegalMoves(Color.BLACK);
        blackMoves.addAll(blackPlayer.calculateCastlingMoves(whiteMoves));
    }

    private void changeCurrentPlayer() {
        this.currentPlayer = currentPlayer().getOpponent();
    }

    public void updatePieceListsAndGameState(Move move) {
        move.updatePieceList();
    }

    public void takebackPieceListsAndGameState(Move move) {
        move.undoMove();
    }


    public BlackPlayer blackPlayer() {
        return this.blackPlayer;
    }

    public WhitePlayer whitePlayer() {
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

    public List<Piece> getAllPieces() {
        final List<Piece> allPieces = new ArrayList<>();
        allPieces.addAll(getWhitePieces());
        allPieces.addAll(getBlackPieces());
        return allPieces;
    }

    public Pawn getEnPassantPawn() {
        if (this.enPassantPawnList.isEmpty()) {
            return null;
        }
        return this.enPassantPawnList.get(enPassantPawnList.size() - 1);
    }

    public Piece getPiece(final int coordinate) {
        if (!this.getTile(coordinate).isTileOccupied()) {
            return null;
        }
        return this.getTile(coordinate).getPiece();
    }

    public List<Move> getLegalMoves() {
        if (this.currentPlayer.getColor().isWhite()) {
            return this.whiteMoves;
        } else {
            return this.blackMoves;
        }
    }

    public List<Move> getOpponentLegalMoves() {
        if (this.currentPlayer.getColor().isWhite()) {
            return this.blackMoves;
        } else {
            return this.whiteMoves;
        }
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
        return activePieces;
    }

    public List<Move> getAllMoves() {
        final List<Move> allMoves = new ArrayList<>();
        allMoves.addAll(this.whiteMoves);
        allMoves.addAll(this.blackMoves);
        return allMoves;
    }

    public Tile getTile(int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }


    private static List<Tile> createGameBoard(final Builder builder) {
        Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return new ArrayList<>(List.of(tiles));
    }


    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Color nextPlayer;
        List<Pawn> enPassantPawnList = new ArrayList<>();
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
            this.enPassantPawnList = new ArrayList<>();
            enPassantPawnList.add(enPassantPawn);

        }

        public void setCastlingRights(boolean whiteKingSideCastle, boolean whiteQueenSideCastle, boolean blackKingSideCastle, boolean blackQueenSideCastle) {
            this.whiteKingSideCastle = whiteKingSideCastle;
            this.whiteQueenSideCastle = whiteQueenSideCastle;
            this.blackKingSideCastle = blackKingSideCastle;
            this.blackQueenSideCastle = blackQueenSideCastle;
        }


    }


}

