//package chess.engine.bitboard;
//
//
//// Helper class for dealing with FEN strings
//public static class FenUtility {
//    public final static String StartPositionFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
//
//    // Load position from fen string
//    public static PositionInfo PositionFromFen(String fen) {
//        PositionInfo loadedPositionInfo = new (fen);
//        return loadedPositionInfo;
//    }
//
//    /// <summary>
//    /// Get the fen string of the current position
//    /// When alwaysIncludeEPSquare is true the en passant square will be included
//    /// in the fen string even if no enemy pawn is in a position to capture it.
//    /// </summary>
//    public static string CurrentFen(Board board, bool alwaysIncludeEPSquare =true) {
//        string fen = "";
//        for (int rank = 7; rank >= 0; rank--) {
//            int numEmptyFiles = 0;
//            for (int file = 0; file < 8; file++) {
//                int i = rank * 8 + file;
//                int piece = board.Square[i];
//                if (piece != 0) {
//                    if (numEmptyFiles != 0) {
//                        fen += numEmptyFiles;
//                        numEmptyFiles = 0;
//                    }
//                    bool isBlack = Piece.IsColour(piece, Piece.Black);
//                    int pieceType = Piece.PieceType(piece);
//                    char pieceChar = ' ';
//                    switch (pieceType) {
//                        case Piece.Rook:
//                            pieceChar = 'R';
//                            break;
//                        case Piece.Knight:
//                            pieceChar = 'N';
//                            break;
//                        case Piece.Bishop:
//                            pieceChar = 'B';
//                            break;
//                        case Piece.Queen:
//                            pieceChar = 'Q';
//                            break;
//                        case Piece.King:
//                            pieceChar = 'K';
//                            break;
//                        case Piece.Pawn:
//                            pieceChar = 'P';
//                            break;
//                    }
//                    fen += (isBlack) ? pieceChar.ToString().ToLower() : pieceChar.ToString();
//                } else {
//                    numEmptyFiles++;
//                }
//
//            }
//            if (numEmptyFiles != 0) {
//                fen += numEmptyFiles;
//            }
//            if (rank != 0) {
//                fen += '/';
//            }
//        }
//
//        // Side to move
//        fen += ' ';
//        fen += (board.IsWhiteToMove) ? 'w' : 'b';
//
//        // Castling
//        bool whiteKingside = (board.CurrentGameState.castlingRights & 1) == 1;
//        bool whiteQueenside = (board.CurrentGameState.castlingRights >> 1 & 1) == 1;
//        bool blackKingside = (board.CurrentGameState.castlingRights >> 2 & 1) == 1;
//        bool blackQueenside = (board.CurrentGameState.castlingRights >> 3 & 1) == 1;
//        fen += ' ';
//        fen += (whiteKingside) ? "K" : "";
//        fen += (whiteQueenside) ? "Q" : "";
//        fen += (blackKingside) ? "k" : "";
//        fen += (blackQueenside) ? "q" : "";
//        fen += ((board.CurrentGameState.castlingRights) == 0) ? "-" : "";
//
//        // En-passant
//        fen += ' ';
//        int epFileIndex = board.CurrentGameState.enPassantFile - 1;
//        int epRankIndex = (board.IsWhiteToMove) ? 5 : 2;
//
//        bool isEnPassant = epFileIndex != -1;
//        bool includeEP = alwaysIncludeEPSquare || EnPassantCanBeCaptured(epFileIndex, epRankIndex, board);
//        if (isEnPassant && includeEP) {
//            fen += BoardHelper.SquareNameFromCoordinate(epFileIndex, epRankIndex);
//        } else {
//            fen += '-';
//        }
//
//        // 50 move counter
//        fen += ' ';
//        fen += board.CurrentGameState.fiftyMoveCounter;
//
//        // Full-move count (should be one at start, and increase after each move by black)
//        fen += ' ';
//        fen += (board.PlyCount / 2) + 1;
//
//        return fen;
//    }
//
//    static bool EnPassantCanBeCaptured(int epFileIndex, int epRankIndex, Board board) {
//        Coord captureFromA = new Coord(epFileIndex - 1, epRankIndex + (board.IsWhiteToMove ? -1 : 1));
//        Coord captureFromB = new Coord(epFileIndex + 1, epRankIndex + (board.IsWhiteToMove ? -1 : 1));
//        int epCaptureSquare = new Coord(epFileIndex, epRankIndex).SquareIndex;
//        int friendlyPawn = Piece.MakePiece(Piece.Pawn, board.MoveColour);
//
//
//        return CanCapture(captureFromA) || CanCapture(captureFromB);
//
//
//        bool CanCapture (Coord from)
//        {
//            bool isPawnOnSquare = board.Square[from.SquareIndex] == friendlyPawn;
//            if (from.IsValidSquare() && isPawnOnSquare) {
//                Move move = new Move(from.SquareIndex, epCaptureSquare, Move.EnPassantCaptureFlag);
//                board.MakeMove(move);
//                board.MakeNullMove();
//                bool wasLegalMove = !board.CalculateInCheckState();
//
//                board.UnmakeNullMove();
//                board.UnmakeMove(move);
//                return wasLegalMove;
//            }
//
//            return false;
//        }
//    }
//
//    public static string FlipFen(string fen) {
//        string flippedFen = "";
//        string[] sections = fen.Split(' ');
//
//        List<char> invertedFenChars = new ();
//        string[] fenRanks = sections[0].Split('/');
//
//        for (int i = fenRanks.Length - 1; i >= 0; i--) {
//            string rank = fenRanks[i];
//            foreach( char c in rank)
//            {
//                flippedFen += InvertCase(c);
//            }
//            if (i != 0) {
//                flippedFen += '/';
//            }
//        }
//
//        flippedFen += " " + (sections[1][0] == 'w' ? 'b' : 'w');
//        string castlingRights = sections[2];
//        string flippedRights = "";
//        foreach( char c in "kqKQ")
//        {
//            if (castlingRights.Contains(c)) {
//                flippedRights += InvertCase(c);
//            }
//        }
//        flippedFen += " " + (flippedRights.Length == 0 ? "-" : flippedRights);
//
//        string ep = sections[3];
//        string flippedEp = ep[0] + "";
//        if (ep.Length > 1) {
//            flippedEp += ep[1] == '6' ? '3' : '6';
//        }
//        flippedFen += " " + flippedEp;
//        flippedFen += " " + sections[4] + " " + sections[5];
//
//
//        return flippedFen;
//
//        char InvertCase ( char c)
//        {
//            if (char.IsLower(c)) {
//                return char.ToUpper(c);
//            }
//            return char.ToLower(c);
//        }
//    }
//
//
//}