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

public class WhitePlayer extends Player {

    public WhitePlayer(final Board board) {
        super(board);
    }

    @Override
    public List<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    public String toString() {
        return "White";
    }

    @Override
    public List<Move> getCastlingMoves() {

        final List<Move> castlingMoves = new ArrayList<>();

        if (!this.isInCheck() && this.playerKing.getPiecePosition() == 60) {

            // Kingside castling
            if (this.playerKing.isKingSideCastleCapable() && !this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                if (board.getOpponentBoardControl().contains(61) && board.getOpponentBoardControl().contains(62)) {
                    final Tile rookTile = this.board.getTile(63);

                    if (rookTile.isTileOccupied() && rookTile.getPiece().getPieceType().isRook() && rookTile.getPiece().isFirstMove()) {
                        castlingMoves.add(new KingsideCastlingMove(this.board,
                                this.playerKing,
                                62,
                                (Rook) rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                61));
                    }
                }
            }

            // Queenside castling
            if (this.playerKing.isQueenSideCastleCapable() && !this.board.getTile(57).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(59).isTileOccupied()) {
                if (board.getOpponentBoardControl().contains(58) && board.getOpponentBoardControl().contains(59)) {
                    final Tile rookTile = this.board.getTile(56);

                    if (rookTile.isTileOccupied() && rookTile.getPiece().getPieceType().isRook() && rookTile.getPiece().isFirstMove()) {
                        castlingMoves.add(new QueensideCastlingMove(this.board,
                                this.playerKing,
                                58,
                                (Rook) rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                59));
                    }
                }
            }
        }
        return castlingMoves;
    }

}
