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

    public WhitePlayer(final Board board,
                       final List<Move> whiteStandardPossibleMoves,
                       final List<Move> blackStandardPossibleMoves) {
        super(board, whiteStandardPossibleMoves, blackStandardPossibleMoves);
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
        return this.board.getBlackPlayer();
    }

    @Override
    public String toString() {
        return "White";
    }

    @Override
    protected List<Move> calculateKingCastlingCollection(final List<Move> playerLegals,
                                                               final List<Move> opponentsLegals) {

        final List<Move> castlingMoves = new ArrayList<>();

        if (!this.isInCheck() && this.playerKing.getPiecePosition() == 60) {

            // Kingside castling
            if (this.playerKing.isKingSideCastleCapable() && !this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty()) {
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
                if (Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty()) {
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
