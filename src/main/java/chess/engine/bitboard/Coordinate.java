//package chess.engine.bitboard;
//
//public class Coordinate {
//
//    public final int fileIndex;
//    public final int rankIndex;
//
//    public Coordinate(int fileIndex, int rankIndex) {
//        this.fileIndex = fileIndex;
//        this.rankIndex = rankIndex;
//    }
//
//    public Coordinate(int squareIndex) {
//        this.fileIndex = BoardHelper.fileIndex(squareIndex);
//        this.rankIndex = BoardHelper.rankIndex(squareIndex);
//    }
//
//    public boolean IsLightSquare() {
//        return (fileIndex + rankIndex) % 2 != 0;
//    }
//
//    public int CompareTo(Coordinate other) {
//        return (fileIndex == other.fileIndex && rankIndex == other.rankIndex) ? 0 : 1;
//    }
//
//    public static Coordinate add(Coordinate a, Coordinate b) {
//        return new Coordinate(a.fileIndex + b.fileIndex, a.rankIndex + b.rankIndex);
//    }
//
//    public static Coordinate subtract(Coordinate a, Coordinate b) {
//        return new Coordinate(a.fileIndex + b.fileIndex, a.rankIndex + b.rankIndex);
//    }
//
//    public static Coordinate multiply(Coordinate a, Coordinate b) {
//        return new Coordinate(a.fileIndex + b.fileIndex, a.rankIndex + b.rankIndex);
//    }
//
//    public static Coordinate divide(Coordinate a, Coordinate b) {
//        return new Coordinate(a.fileIndex + b.fileIndex, a.rankIndex + b.rankIndex);
//    }
//
//
//    public boolean IsValidSquare() {
//        return (fileIndex >= 0 && fileIndex < 8 && rankIndex >= 0 && rankIndex < 8);
//    }
//
//    public int squareIndex() {
//        return BoardHelper.indexFromCoord(this);
//    }
//}
//
