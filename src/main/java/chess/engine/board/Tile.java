package chess.engine.board;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import chess.engine.pieces.Piece;
import lombok.Getter;


@Getter
public abstract class Tile {

    protected final int tileCoordinate;

    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile(int tileCoordinate, Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    private Tile(int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public abstract String toString();


    public static final class EmptyTile extends Tile {

        EmptyTile(final int coordinate) {
            super(coordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "-";
        }
    }


    public static final class OccupiedTile extends Tile {
        private final Piece piece;

        OccupiedTile(int coordinate, Piece pieceOnTile) {
            super(coordinate);

            this.piece = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.piece;
        }

        @Override
        public String toString() {
            return getPiece().getPieceColor().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString();
        }
    }
}