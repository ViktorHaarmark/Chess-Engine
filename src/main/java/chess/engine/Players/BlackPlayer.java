package chess.engine.Players;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.board.Move.KingsideCastlingMove;
import chess.engine.board.Move.QueensideCastlingMove;
import chess.engine.board.Tile;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

public class BlackPlayer extends Player {

    public BlackPlayer(final Board board) {
        super(board);
    }

    @Override
    public List<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    public String toString() {
        return "Black";
    }

    @Override
    public List<Move> getCastlingMoves() {
        if (!board.currentPlayer().equals(this)) {
            throw new RuntimeException("this only works if it is black to move");
        }
        final List<Move> castlingMoves = new ArrayList<>();

        //Kingside castling

        if (!this.isInCheck() && this.playerKing.getPiecePosition() == 4) {
            // Kingside castling
            if (this.playerKing.isKingSideCastleCapable() &&
                    !this.board.getTile(5).isTileOccupied() &&
                    !this.board.getTile(6).isTileOccupied()) {
                if (board.getOpponentBoardControl().contains(5) && board.getOpponentBoardControl().contains(6)) {
                    final Tile rookTile = this.board.getTile(7);

                    if (rookTile.isTileOccupied() && rookTile.getPiece().getPieceType().isRook() && rookTile.getPiece().isFirstMove()) {
                        castlingMoves.add(new KingsideCastlingMove(this.board,
                                this.playerKing,
                                6,
                                (Rook) rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                5));
                    }
                }
            }

            // Queenside Castling
            if (this.playerKing.isQueenSideCastleCapable() && !this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()) {
                if (board.getOpponentBoardControl().contains(2) && board.getOpponentBoardControl().contains(3)) {
                    final Tile rookTile = this.board.getTile(0);

                    if (rookTile.isTileOccupied() && rookTile.getPiece().getPieceType().isRook() && rookTile.getPiece().isFirstMove()) {
                        castlingMoves.add(new QueensideCastlingMove(this.board,
                                this.playerKing,
                                2,
                                (Rook) rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                3));
                    }
                }
            }

        }

        return castlingMoves;
    }


}
