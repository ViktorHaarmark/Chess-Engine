package chess;

import chess.engine.Players.BlackPlayer;
import chess.engine.Players.Player;
import chess.engine.Players.WhitePlayer;

public enum Color {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isSecondRank(int tileCoordinate) {
            return tileCoordinate / 8 == 6;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public Color getOpponentColor() {
            return Color.BLACK;
        }

        @Override
        public String toString() {
            return "white";
        }

        @Override
        public boolean isPromotionRow(int tileCoordinate) {
            return tileCoordinate / 8 == 0;
        }
    },

    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isSecondRank(int tileCoordinate) {
            return tileCoordinate / 8 == 1;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }

        @Override
        public Color getOpponentColor() {
            return Color.WHITE;
        }

        @Override
        public String toString() {
            return "black";
        }

        @Override
        public boolean isPromotionRow(int tileCoordinate) {
            return tileCoordinate / 8 == 7;
        }
    };

    public abstract int getDirection();

    public abstract boolean isSecondRank(final int tileCoordinate);

    public abstract boolean isPromotionRow(final int tileCoordinate);

    public abstract boolean isBlack();

    public abstract boolean isWhite();

    public abstract Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer);

    public abstract Color getOpponentColor();

    public abstract String toString();
}

