package chess.engine.bitboard;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import static chess.engine.bitboard.BitBoardUtils.*;

@EqualsAndHashCode
@Getter
public final class Move {
    private final short moveValue;

    // move flags
    public static final int NO_FLAG = 0b0000;
    public static final int EN_PASSANT_CAPTURE_FLAG = 0b0001;
    public static final int CASTLE_FLAG = 0b0010;
    public static final int PAWN_TWO_UP_FLAG = 0b0011;

    public static final int QUEEN_PROMOTION_FLAG = 0b0100;
    public static final int KNIGHT_PROMOTION_FLAG = 0b0101;
    public static final int ROOK_PROMOTION_FLAG = 0b0110;
    public static final int BISHOP_PROMOTION_FLAG = 0b0111;

    private static final int START_SQUARE_MASK = 0b0000000000111111;
    private static final int TARGET_SQUARE_MASK = 0b0000111111000000;
    private static final int FLAG_MASK = 0b1111000000000000;

    public static final Move NULL_MOVE = new Move((short) 0);

    public Move(short moveValue) {
        this.moveValue = moveValue;
    }

    public Move(int startSquare, int targetSquare) {
        this.moveValue = (short) (startSquare | (targetSquare << 6));
    }

    public Move(int startSquare, int targetSquare, int flag) {
        this.moveValue = (short) (startSquare | (targetSquare << 6) | (flag << 12));
    }

    public boolean isNull() {
        return moveValue == 0;
    }

    public int getStartSquare() {
        return moveValue & START_SQUARE_MASK;
    }

    public int getTargetSquare() {
        return (moveValue & TARGET_SQUARE_MASK) >> 6;
    }

    public int getMoveFlag() {
        return (moveValue & FLAG_MASK) >> 12;
    }

    public boolean isPromotion() {
        return getMoveFlag() >= QUEEN_PROMOTION_FLAG; //queen promotion is the smallest promotion flag
    }


    public int getPromotionPieceType() {
        return switch (getMoveFlag()) {
            case ROOK_PROMOTION_FLAG -> ROOKS;
            case KNIGHT_PROMOTION_FLAG -> KNIGHTS;
            case BISHOP_PROMOTION_FLAG -> BISHOPS;
            case QUEEN_PROMOTION_FLAG -> QUEENS;
            default -> 0;
        };
    }

    @Override
    public String toString() {
        return String.format("Move: %d -> %d, Flag: %d", getStartSquare(), getTargetSquare(), getMoveFlag());
    }
}