//package chess.engine.bitboard;
//
//
//import java.util.List;
//
//public class FenUtility {
//    public final static String START_POSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
//
//    // Load position from fen string
//    public static PositionInfo positionFromFen(String fen) {
//        return new PositionInfo(fen);
//    }
//
//
//    public static String currentFen(Board board) {
//        return currentFen(board, true);
//    }
//
//    /// <summary>
//    /// Get the fen string of the current position
//    /// When alwaysIncludeEPSquare is true the en passant square will be included
//    /// in the fen string even if no enemy pawn is in a position to capture it.
//    /// </summary>
//    public static String currentFen(Board board, boolean alwaysIncludeEPSquare) {
//        StringBuilder fen = new StringBuilder();
//        for (int rank = 7; rank >= 0; rank--) {
//            int numEmptyFiles = 0;
//            for (int file = 0; file < 8; file++) {
//                int i = rank * 8 + file;
//                int piece = board.Square[i];
//                if (piece != 0) {
//                    if (numEmptyFiles != 0) {
//                        fen.append(numEmptyFiles);
//                        numEmptyFiles = 0;
//                    }
//                    boolean isBlack = Piece.isColor(piece, Piece.Black);
//                    int pieceType = Piece.getPieceType(piece);
//                    char pieceChar = switch (pieceType) {
//                        case Piece.ROOK -> 'R';
//                        case Piece.KNIGHT -> 'N';
//                        case Piece.BISHOP -> 'B';
//                        case Piece.QUEEN -> 'Q';
//                        case Piece.KING -> 'K';
//                        case Piece.PAWN -> 'P';
//                        default -> ' ';
//                    };
//                    fen.append((isBlack) ? String.valueOf(pieceChar).toLowerCase() : String.valueOf(pieceChar));
//                } else {
//                    numEmptyFiles++;
//                }
//
//            }
//            if (numEmptyFiles != 0) {
//                fen.append(numEmptyFiles);
//            }
//            if (rank != 0) {
//                fen.append('/');
//            }
//        }
//
//        // Side to move
//        fen.append(' ');
//        fen.append((board.IsWhiteToMove) ? 'w' : 'b');
//
//        // Castling
//        boolean whiteKingside = (board.CurrentGameState.castlingRights & 1) == 1;
//        boolean whiteQueenside = (board.CurrentGameState.castlingRights >> 1 & 1) == 1;
//        boolean blackKingside = (board.CurrentGameState.castlingRights >> 2 & 1) == 1;
//        boolean blackQueenside = (board.CurrentGameState.castlingRights >> 3 & 1) == 1;
//        fen.append(' ');
//        fen.append((whiteKingside) ? "K" : "");
//        fen.append((whiteQueenside) ? "Q" : "");
//        fen.append((blackKingside) ? "k" : "");
//        fen.append((blackQueenside) ? "q" : "");
//        fen.append(((board.CurrentGameState.castlingRights) == 0) ? "-" : "");
//
//        // En-passant
//        fen.append(' ');
//        int epFileIndex = board.CurrentGameState.enPassantFile - 1;
//        int epRankIndex = (board.IsWhiteToMove) ? 5 : 2;
//
//        boolean isEnPassant = epFileIndex != -1;
//        boolean includeEP = alwaysIncludeEPSquare || enPassantCanBeCaptured(epFileIndex, epRankIndex, board);
//        if (isEnPassant && includeEP) {
//            fen.append(BoardHelper.squareNameFromCoordinate(epFileIndex, epRankIndex));
//        } else {
//            fen.append('-');
//        }
//
//        // 50 move counter
//        fen.append(' ');
//        fen.append(board.CurrentGameState.fiftyMoveCounter);
//
//        // Full-move count (should be one at start, and increase after each move by black)
//        fen.append(' ');
//        fen.append((board.PlyCount / 2) + 1);
//
//        return fen.toString();
//    }
//
//    static boolean enPassantCanBeCaptured(int epFileIndex, int epRankIndex, Board board) {
//        Coordinate captureFromA = new Coordinate(epFileIndex - 1, epRankIndex + (board.IsWhiteToMove ? -1 : 1));
//        Coordinate captureFromB = new Coordinate(epFileIndex + 1, epRankIndex + (board.IsWhiteToMove ? -1 : 1));
//        int epCaptureSquare = new Coordinate(epFileIndex, epRankIndex).squareIndex();
//        int friendlyPawn = Piece.makePiece(Piece.PAWN, board.MoveColour);
//
//        return canCapture(board, captureFromA, epCaptureSquare) || canCapture(board, captureFromB, epCaptureSquare);
//    }
//
//
//    private static boolean canCapture(Board board, Coordinate from, int epCaptureSquare) {
////        boolean isPawnOnSquare = board.square[from.SquareIndex] == friendlyPawn;
////        if (from.IsValidSquare() && isPawnOnSquare) {
////            Move move = new Move(from.squareIndex(), epCaptureSquare, Move.EN_PASSANT_CAPTURE_FLAG);
////            board.makeMove(move);
////            board.MakeNullMove();
////            boolean wasLegalMove = !board.calculateInCheckState();
////
////            board.UnmakeNullMove();
////            board.unMakeMove(move);
////            return wasLegalMove;
////        }
//        return false;
//    }
//
//
////    public static String flipFen(String fen) {
////        String flippedFen = "";
////        String[] sections = fen.split(" ");
////
//////        List<String> invertedFenChars = new ();
////        String[] fenRanks = sections[0].split("/");
////
////        for (int i = fenRanks.length - 1; i >= 0; i--) {
////            String rank = fenRanks[i];
////            for (char c : rank) {
////
////                flippedFen += invertCase(c);
////
////                if (i != 0) {
////                    flippedFen += '/';
////                }
////            }
////        }
////
////        flippedFen += " " + (sections[1][0] == 'w' ? 'b' : 'w');
////        String castlingRights = sections[2];
////        String flippedRights = "";
////        for (char c : "kqKQ") {
////            if (castlingRights.contains(c)) {
////                flippedRights += invertCase(c);
////            }
////        }
////        flippedFen += " " + (flippedRights.Length == 0 ? "-" : flippedRights);
////
////        String ep = sections[3];
////        String flippedEp = ep[0] + "";
////        if (ep.length() > 1) {
////            flippedEp += ep[1] == '6' ? '3' : '6';
////        }
////        flippedFen += " " + flippedEp;
////        flippedFen += " " + sections[4] + " " + sections[5];
////
////
////        return flippedFen;
////    }
////
////    private static char invertCase(char c) {
////        if (Character.isLowerCase(c)) {
////            return Character.toUpperCase(c);
////        }
////        return Character.toLowerCase(c);
////    }
//
//
//}