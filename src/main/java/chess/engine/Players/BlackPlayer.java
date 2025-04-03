package chess.engine.Players;

import chess.Color;
import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.board.Move.KingsideCastlingMove;
import chess.engine.board.Move.QueensideCastlingMove;
import chess.engine.board.Tile;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BlackPlayer extends Player {

    public BlackPlayer(final Board board,
                       final List<Move> whiteStandardPossibleMoves,
                       final List<Move> blackStandardPossibleMoves) {
        super(board, blackStandardPossibleMoves, whiteStandardPossibleMoves);
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
        return this.board.getWhitePlayer();
    }

    @Override
    public String toString() {
        return "Black";
    }

    @Override
    protected List<Move> calculateKingCastlingCollection(final List<Move> playerLegals,
                                                               final List<Move> opponentsLegals) {

        final List<Move> castlingMoves = new ArrayList<>();

        //Kingside castling

        if (!this.isInCheck() && this.playerKing.getPiecePosition() == 4) {

            // Kingside castling
            if (this.playerKing.isKingSideCastleCapable() &&
                    !this.board.getTile(5).isTileOccupied() &&
                    !this.board.getTile(6).isTileOccupied()) {
                if (Player.calculateAttacksOnTile(5, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty()) {
                    final Tile rookTile = this.board.getTile(7);

                    if (rookTile.isTileOccupied() && rookTile.getPiece() instanceof Rook && rookTile.getPiece().isFirstMove()) {
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
                if (Player.calculateAttacksOnTile(2, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty()) {
                    final Tile rookTile = this.board.getTile(0);

                    if (rookTile.isTileOccupied() && rookTile.getPiece() instanceof Rook && rookTile.getPiece().isFirstMove()) {
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
