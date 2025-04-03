package chess.engine.board;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class BoardUtils {

    public static final int NUM_TILES = 64;
    public static final int ROW_LENGTH = 8;

    private static final String[] ALGEBRAIC_NOTATION_ROW = initializeAlgebraicNotationRow();
    private static final String[] ALGEBRAIC_NOTATION_COLUMN = initializeAlgebraicNotationColumn();

    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinate();

    private static Map<String, Integer> initializePositionToCoordinate() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();

        for (int i = 0; i < NUM_TILES; i++) {
            positionToCoordinate.put(getPositionAtCoordinate(i), i);
        }
        return ImmutableMap.copyOf(positionToCoordinate);
    }


    public static boolean isValidTileCoordinate(int coordinate) {
        return (coordinate >= 0 && coordinate < NUM_TILES);
    }

    public static int rowDifference(int squareOne, int squareTwo) {
        return Math.abs(squareOne / ROW_LENGTH - squareTwo / ROW_LENGTH);
    }

    public static int columnDifference(int squareOne, int squareTwo) {
        return Math.max(squareOne % 8, squareTwo % 8) - Math.min(squareOne % 8, squareTwo % 8);
    }

    public static int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    private static String[] initializeAlgebraicNotationColumn() {
        return new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
    }

    private static String[] initializeAlgebraicNotationRow() {
        return new String[]{"8", "7", "6", "5", "4", "3", "2", "1"};
    }

    public static String getPositionAtCoordinate(final int coordinate) {
        int row = coordinate / 8;
        int column = coordinate % 8;
        return ALGEBRAIC_NOTATION_COLUMN[column] + ALGEBRAIC_NOTATION_ROW[row];


    }


    public static boolean isEndGame(Board board) {
        return (board.getCurrentPlayer().isInCheckMate() ||
                board.getCurrentPlayer().isInStaleMate());
    }

}
