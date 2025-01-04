package chess.engine.GUI;

import java.util.List;

import chess.engine.GUI.Table.TilePanel;

public enum BoardDirection {

    NORMAL {
        @Override
        List<TilePanel> traverse(final List<TilePanel> boardTiles) {
            return boardTiles;
        }

        @Override
        BoardDirection opposite() {
            return FLIPPED;
        }
    },

    FLIPPED {

        @Override
        List<TilePanel> traverse(List<TilePanel> boardTiles) {
            return boardTiles.reversed();
        }

        @Override
        BoardDirection opposite() {
            return NORMAL;
        }

    };


    abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);

    abstract BoardDirection opposite();



}
