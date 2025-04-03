package chess.engine.bitboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Board {

    public static long wPawns, wKnights, wBishops, wRooks, wQueens, wKings, bPawns, bKnights, bBishops, bRooks, bQueens, bKings;
    public static long whitePieces, blackPieces, allPieces;

    public final static int WHITE_INDEX = 0;
    public final static int BLACK_INDEX = 1;

    // Stores piece code for each square on the board
    public int[] Square;
    // Square index of white and black king
    public int[] KingSquare;
    // # Bitboards
    // Bitboard for each piece type and colour (white pawns, white knights, ... black pawns, etc.)
    public long[] PieceBitboards;
    // Bitboards for all pieces of either colour (all white pieces, all black pieces)
    public long[] ColourBitboards;
    public long AllPiecesBitboard;
    public long FriendlyOrthogonalSliders;
    public long FriendlyDiagonalSliders;
    public long EnemyOrthogonalSliders;
    public long EnemyDiagonalSliders;
    // Piece count excluding pawns and kings
    public int TotalPieceCountWithoutPawnsAndKings;
    // # Piece lists
    public PieceList[] Rooks;
    public PieceList[] Bishops;
    public PieceList[] Queens;
    public PieceList[] Knights;
    public PieceList[] Pawns;

    // # Side to move info
    public boolean IsWhiteToMove;
    public int MoveColour = IsWhiteToMove ? Piece.White : Piece.Black;
    public int OpponentColour = IsWhiteToMove ? Piece.Black : Piece.White;
    public int MoveColourIndex = IsWhiteToMove ? WHITE_INDEX : BLACK_INDEX;
    public int OpponentColourIndex = IsWhiteToMove ? BLACK_INDEX : WHITE_INDEX;
    // List of (hashed) positions since last pawn move or capture (for detecting repetitions)
    public Stack<Long> RepetitionPositionHistory;

    // Total plies (half-moves) played in game
    public int PlyCount;
    public GameState CurrentGameState;
    public int FiftyMoveCounter = CurrentGameState.fiftyMoveCounter;
    //    public String CurrentFEN = FenUtility.(this);
    public String GameStartFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public List<Move> AllGameMoves;


    // # Private stuff
    private PieceList[] allPieceLists;
    private Stack<GameState> gameStateHistory;
    private PositionInfo StartPositionInfo;
    private boolean cachedInCheckValue;
    private boolean hasCachedInCheckValue;

    public Board() {
        Square = new int[64];
    }


    // Make a move on the board
    // The inSearch parameter controls whether this move should be recorded in the game history.
    // (for detecting three-fold repetition)
    public void makeMove(Move move, boolean inSearch) {
        // Get info about move
        int startSquare = move.getStartSquare();
        int targetSquare = move.getTargetSquare();
        int moveFlag = move.getMoveFlag();
        boolean isPromotion = move.isPromotion();
        boolean isEnPassant = (moveFlag == Move.EN_PASSANT_CAPTURE_FLAG);

        int movedPiece = Square[startSquare];
        int movedPieceType = Piece.getPieceType(movedPiece);
        int capturedPiece = isEnPassant ? Piece.makePiece(Piece.PAWN, OpponentColour) : Square[targetSquare];
        int capturedPieceType = Piece.getPieceType(capturedPiece);

        int prevCastleState = CurrentGameState.castlingRights;
        int prevEnPassantFile = CurrentGameState.enPassantFile;
        int newCastlingRights = CurrentGameState.castlingRights;
        int newEnPassantFile = 0;

        // Update bitboard of moved piece (pawn promotion is a special case and is corrected later)
        movePiece(movedPiece, startSquare, targetSquare);

        // Handle captures
        if (capturedPieceType != Piece.NONE) {
            int captureSquare = targetSquare;

            if (isEnPassant) {
                captureSquare = targetSquare + (IsWhiteToMove ? -8 : 8);
                Square[captureSquare] = Piece.NONE;
            }
            if (capturedPieceType != Piece.PAWN) {
                TotalPieceCountWithoutPawnsAndKings--;
            }

            // Remove captured piece from bitboards/piece list
            allPieceLists[capturedPiece].removePieceAtSquare(captureSquare);
            toggleSquare(PieceBitboards, capturedPiece, captureSquare);
            toggleSquare(ColourBitboards, OpponentColourIndex, captureSquare);
        }

        // Handle king
        if (movedPieceType == Piece.KING) {
            KingSquare[MoveColourIndex] = targetSquare;
            newCastlingRights &= (IsWhiteToMove) ? 0b1100 : 0b0011;

            // Handle castling
            if (moveFlag == Move.CASTLE_FLAG) {
                int rookPiece = Piece.makePiece(Piece.ROOK, MoveColour);
                boolean kingside = targetSquare == BoardHelper.g1 || targetSquare == BoardHelper.g8; //boardhelper
                int castlingRookFromIndex = (kingside) ? targetSquare + 1 : targetSquare - 2;
                int castlingRookToIndex = (kingside) ? targetSquare - 1 : targetSquare + 1;

                // Update rook position
                toggleSquares(PieceBitboards, rookPiece, castlingRookFromIndex, castlingRookToIndex);
                toggleSquares(ColourBitboards, MoveColourIndex, castlingRookFromIndex, castlingRookToIndex);
                allPieceLists[rookPiece].movePiece(castlingRookFromIndex, castlingRookToIndex);
                Square[castlingRookFromIndex] = Piece.NONE;
                Square[castlingRookToIndex] = Piece.ROOK | MoveColour;

            }
        }

        // Handle promotion
        if (isPromotion) {
            TotalPieceCountWithoutPawnsAndKings++;
            int promotionPieceType = switch (moveFlag) {
                case Move.QUEEN_PROMOTION_FLAG -> Piece.QUEEN;
                case Move.ROOK_PROMOTION_FLAG -> Piece.ROOK;
                case Move.KNIGHT_PROMOTION_FLAG -> Piece.KNIGHT;
                case Move.BISHOP_PROMOTION_FLAG -> Piece.BISHOP;
                default -> 0;
            };

            int promotionPiece = Piece.makePiece(promotionPieceType, MoveColour);

            // Remove pawn from promotion square and add promoted piece instead
            toggleSquare(PieceBitboards, movedPiece, targetSquare);
            toggleSquare(ColourBitboards, promotionPiece, targetSquare);
            allPieceLists[movedPiece].removePieceAtSquare(targetSquare);
            allPieceLists[promotionPiece].addPieceAtSquare(targetSquare);
            Square[targetSquare] = promotionPiece;
        }

        // Pawn has moved two forwards, mark file with en-passant flag
        if (moveFlag == Move.PAWN_TWO_UP_FLAG) {
            int file = BoardHelper.fileIndex(startSquare) + 1;
            newEnPassantFile = file;
        }

        // Update castling rights
        if (prevCastleState != 0) {
            // Any piece moving to/from rook square removes castling right for that side
            if (targetSquare == BoardHelper.h1 || startSquare == BoardHelper.h1) {
                newCastlingRights &= GameState.clearWhiteKingsideMask;
            } else if (targetSquare == BoardHelper.a1 || startSquare == BoardHelper.a1) {
                newCastlingRights &= GameState.clearWhiteQueensideMask;
            }
            if (targetSquare == BoardHelper.h8 || startSquare == BoardHelper.h8) {
                newCastlingRights &= GameState.clearBlackKingsideMask;
            } else if (targetSquare == BoardHelper.a8 || startSquare == BoardHelper.a8) {
                newCastlingRights &= GameState.clearBlackQueensideMask;
            }
        }

        // Change side to move
        IsWhiteToMove = !IsWhiteToMove;

        PlyCount++;
        int newFiftyMoveCounter = CurrentGameState.fiftyMoveCounter + 1;

        // Update extra bitboards
        AllPiecesBitboard = ColourBitboards[WHITE_INDEX] | ColourBitboards[BLACK_INDEX];
        UpdateSliderBitboards();

        // Pawn moves and captures reset the fifty move counter and clear 3-fold repetition history
        if (movedPieceType == Piece.PAWN || capturedPieceType != Piece.NONE) {
            if (!inSearch) {
                RepetitionPositionHistory.clear();
            }
            newFiftyMoveCounter = 0;
        }

        GameState newState = new GameState(capturedPieceType, newEnPassantFile, newCastlingRights, newFiftyMoveCounter);
        gameStateHistory.push(newState);
        CurrentGameState = newState;
        hasCachedInCheckValue = false;

        if (!inSearch) {
            //RepetitionPositionHistory.push(newState.zobristKey);
            AllGameMoves.add(move);
        }
    }

    public void makeMove(Move move) {
        makeMove(move, false);
    }

    private void toggleSquare(long[] bitboards, int index, int square) {
        bitboards[index] ^= (1L << square);
    }

    private void toggleSquares(long[] bitboards, int index, int fromSquare, int toSquare) {
        bitboards[index] ^= (1L << fromSquare);
        bitboards[index] ^= (1L << toSquare);
    }

    public void unMakeMove(Move move) {
        unMakeMove(move, false);
    }

    // Undo a move previously made on the board
    public void unMakeMove(Move move, boolean inSearch) {
        // Swap colour to move
        IsWhiteToMove = !IsWhiteToMove;

        boolean undoingWhiteMove = IsWhiteToMove;

        // Get move info
        int movedFrom = move.getStartSquare();
        int movedTo = move.getTargetSquare();
        int moveFlag = move.getMoveFlag();

        boolean undoingEnPassant = moveFlag == Move.EN_PASSANT_CAPTURE_FLAG;
        boolean undoingPromotion = move.isPromotion();
        boolean undoingCapture = CurrentGameState.capturedPieceType != Piece.NONE;

        int movedPiece = undoingPromotion ? Piece.makePiece(Piece.PAWN, MoveColour) : Square[movedTo];
        int movedPieceType = Piece.getPieceType(movedPiece);
        int capturedPieceType = CurrentGameState.capturedPieceType;

        // If undoing promotion, then remove piece from promotion square and replace with pawn
        if (undoingPromotion) {
            int promotedPiece = Square[movedTo];
            int pawnPiece = Piece.makePiece(Piece.PAWN, MoveColour);
            TotalPieceCountWithoutPawnsAndKings--;

            allPieceLists[promotedPiece].removePieceAtSquare(movedTo);
            allPieceLists[movedPiece].addPieceAtSquare(movedTo);
            toggleSquare(PieceBitboards, promotedPiece, movedTo);
            toggleSquare(PieceBitboards, pawnPiece, movedTo);
        }

        movePiece(movedPiece, movedTo, movedFrom);

        // Undo capture
        if (undoingCapture) {
            int captureSquare = movedTo;
            int capturedPiece = Piece.makePiece(capturedPieceType, OpponentColour);

            if (undoingEnPassant) {
                captureSquare = movedTo + ((undoingWhiteMove) ? -8 : 8);
            }
            if (capturedPieceType != Piece.PAWN) {
                TotalPieceCountWithoutPawnsAndKings++;
            }

            // Add back captured piece
            toggleSquare(PieceBitboards, capturedPiece, captureSquare);
            toggleSquare(ColourBitboards, OpponentColourIndex, capturedPiece);
            allPieceLists[capturedPiece].addPieceAtSquare(captureSquare);
            Square[captureSquare] = capturedPiece;
        }


        // Update king
        if (movedPieceType == Piece.KING) {
            KingSquare[MoveColourIndex] = movedFrom;

            // Undo castling
            if (moveFlag == Move.CASTLE_FLAG) {
                int rookPiece = Piece.makePiece(Piece.ROOK, MoveColour);
                boolean kingside = movedTo == BoardHelper.g1 || movedTo == BoardHelper.g8;
                int rookSquareBeforeCastling = kingside ? movedTo + 1 : movedTo - 2;
                int rookSquareAfterCastling = kingside ? movedTo - 1 : movedTo + 1;

                // Undo castling by returning rook to original square
//                BitBoardUtility.ToggleSquares(ref PieceBitboards[rookPiece], rookSquareAfterCastling, rookSquareBeforeCastling);
//                BitBoardUtility.ToggleSquares(ref ColourBitboards[MoveColourIndex], rookSquareAfterCastling, rookSquareBeforeCastling);
                Square[rookSquareAfterCastling] = Piece.NONE;
                Square[rookSquareBeforeCastling] = rookPiece;
                allPieceLists[rookPiece].movePiece(rookSquareAfterCastling, rookSquareBeforeCastling);
            }
        }

        AllPiecesBitboard = ColourBitboards[WHITE_INDEX] | ColourBitboards[BLACK_INDEX];
        UpdateSliderBitboards();

        if (!inSearch && !RepetitionPositionHistory.isEmpty()) {
            RepetitionPositionHistory.pop();
        }
        if (!inSearch) {
            AllGameMoves.remove(AllGameMoves.size() - 1);
        }

        // Go back to previous state
        gameStateHistory.pop();
        CurrentGameState = gameStateHistory.peek();
        PlyCount--;
        hasCachedInCheckValue = false;
    }

    // Switch side to play without making a move (NOTE: must not be in check when called)
    public void MakeNullMove() {
        IsWhiteToMove = !IsWhiteToMove;

        PlyCount++;

        CurrentGameState = new GameState(Piece.NONE, 0, CurrentGameState.castlingRights, CurrentGameState.fiftyMoveCounter + 1);
        gameStateHistory.push(CurrentGameState);
        UpdateSliderBitboards();
        hasCachedInCheckValue = true;
        cachedInCheckValue = false;
    }

    public void UnmakeNullMove() {
        IsWhiteToMove = !IsWhiteToMove;
        PlyCount--;
        gameStateHistory.pop();
        CurrentGameState = gameStateHistory.peek();
        UpdateSliderBitboards();
        hasCachedInCheckValue = true;
        cachedInCheckValue = false;
    }

    // Is current player in check?
    // Note: caches check value so calling multiple times does not require recalculating
    public boolean IsInCheck() {
        if (hasCachedInCheckValue) {
            return cachedInCheckValue;
        }
        cachedInCheckValue = calculateInCheckState();
        hasCachedInCheckValue = true;

        return cachedInCheckValue;
    }

    // Calculate in check value
    // Call IsInCheck instead for automatic caching of value
    public boolean calculateInCheckState() {
//        int kingSquare = KingSquare[MoveColourIndex];
//        long blockers = AllPiecesBitboard;
//
//        if (EnemyOrthogonalSliders != 0) {
//            long rookAttacks = Magic.GetRookAttacks(kingSquare, blockers);
//            if ((rookAttacks & EnemyOrthogonalSliders) != 0) {
//                return true;
//            }
//        }
//        if (EnemyDiagonalSliders != 0) {
//            long bishopAttacks = Magic.GetBishopAttacks(kingSquare, blockers);
//            if ((bishopAttacks & EnemyDiagonalSliders) != 0) {
//                return true;
//            }
//        }
//
//        long enemyKnights = PieceBitboards[Piece.makePiece(Piece.KNIGHT, OpponentColour)];
//        if ((BitBoardUtility.KnightAttacks[kingSquare] & enemyKnights) != 0) {
//            return true;
//        }
//
//        long enemyPawns = PieceBitboards[Piece.makePiece(Piece.PAWN, OpponentColour)];
//        long pawnAttackMask = IsWhiteToMove ? BitBoardUtility.WhitePawnAttacks[kingSquare] : BitBoardUtility.BlackPawnAttacks[kingSquare];
//        if ((pawnAttackMask & enemyPawns) != 0) {
//            return true;
//        }

        return false;
    }


    // Load the starting position
    public void loadStartPosition() {
        loadPosition(FenUtility.START_POSITION_FEN);
    }

    public void loadPosition(String fen) {
        PositionInfo posInfo = FenUtility.positionFromFen(fen);
        loadPosition(posInfo);
    }

    public void loadPosition(PositionInfo posInfo) {
        StartPositionInfo = posInfo;
        Initialize();

        // Load pieces into board array and piece lists
        for (int squareIndex = 0; squareIndex < 64; squareIndex++) {
            int piece = posInfo.squares[squareIndex];
            int pieceType = Piece.getPieceType(piece);
            int colourIndex = Piece.isWhite(piece) ? WHITE_INDEX : BLACK_INDEX;
            Square[squareIndex] = piece;

            if (piece != Piece.NONE) {
                BitBoardUtils.setSquare(PieceBitboards[piece], squareIndex);
                BitBoardUtils.setSquare(ColourBitboards[colourIndex], squareIndex);

                if (pieceType == Piece.KING) {
                    KingSquare[colourIndex] = squareIndex;
                } else {
                    allPieceLists[piece].addPieceAtSquare(squareIndex);
                }
                TotalPieceCountWithoutPawnsAndKings += (pieceType == Piece.PAWN || pieceType == Piece.KING) ? 0 : 1;
            }
        }

        // Side to move
        IsWhiteToMove = posInfo.whiteToMove;

        // Set extra bitboards
        AllPiecesBitboard = ColourBitboards[WHITE_INDEX] | ColourBitboards[BLACK_INDEX];
        UpdateSliderBitboards();

        // Create gamestate
        int whiteCastle = ((posInfo.whiteCastleKingside) ? 1 << 0 : 0) | ((posInfo.whiteCastleQueenside) ? 1 << 1 : 0);
        int blackCastle = ((posInfo.blackCastleKingside) ? 1 << 2 : 0) | ((posInfo.blackCastleQueenside) ? 1 << 3 : 0);
        int castlingRights = whiteCastle | blackCastle;

        PlyCount = (posInfo.moveCount - 1) * 2 + (IsWhiteToMove ? 0 : 1);

        // Set game state (note: calculating zobrist key relies on current game state)
        CurrentGameState = new GameState(Piece.NONE, posInfo.epFile, castlingRights, posInfo.fiftyMovePlyCount);
//        ulong zobristKey = Zobrist.CalculateZobristKey(this);
        //CurrentGameState = new GameState(Piece.NONE, posInfo.epFile, castlingRights, posInfo.fiftyMovePlyCount);
//
//        RepetitionPositionHistory.Push(zobristKey);

        gameStateHistory.push(CurrentGameState);
    }

//    @Override
//    public String toString() {
//        return BoardHelper.createDiagram(this, IsWhiteToMove);
//    }

    public static Board createBoard(String fen) {
        Board board = new Board();
        board.loadPosition(fen);
        return board;
    }

    public static Board createBoard() {
        return createBoard(FenUtility.START_POSITION_FEN);
    }

    public static Board createBoard(Board source) {
        Board board = new Board();
        board.loadPosition(source.StartPositionInfo);

        for (int i = 0; i < source.AllGameMoves.size(); i++) {
            board.makeMove(source.AllGameMoves.get(i));
        }
        return board;
    }

    // Update piece lists / bitboards based on given move info.
    // Note that this does not account for the following things, which must be handled separately:
    // 1. Removal of a captured piece
    // 2. Movement of rook when castling
    // 3. Removal of pawn from 1st/8th rank during pawn promotion
    // 4. Addition of promoted piece during pawn promotion
//    void movePiece(int piece, int startSquare, int targetSquare) {
//        BitBoardUtility.toggleSquares(ref PieceBitboards[piece], startSquare, targetSquare);
//        BitBoardUtility.toggleSquares(ref ColourBitboards[MoveColourIndex], startSquare, targetSquare);
//
//        allPieceLists[piece].movePiece(startSquare, targetSquare);
//        Square[startSquare] = Piece.NONE;
//        Square[targetSquare] = piece;
//    }
//
//    void UpdateSliderBitboards() {
//        int friendlyRook = Piece.MakePiece(Piece.ROOK, MoveColour);
//        int friendlyQueen = Piece.MakePiece(Piece.QUEEN, MoveColour);
//        int friendlyBishop = Piece.MakePiece(Piece.BISHOP, MoveColour);
//        FriendlyOrthogonalSliders = PieceBitboards[friendlyRook] | PieceBitboards[friendlyQueen];
//        FriendlyDiagonalSliders = PieceBitboards[friendlyBishop] | PieceBitboards[friendlyQueen];
//
//        int enemyRook = Piece.MakePiece(Piece.ROOK, OpponentColour);
//        int enemyQueen = Piece.MakePiece(Piece.QUEEN, OpponentColour);
//        int enemyBishop = Piece.MakePiece(Piece.BISHOP, OpponentColour);
//        EnemyOrthogonalSliders = PieceBitboards[enemyRook] | PieceBitboards[enemyQueen];
//        EnemyDiagonalSliders = PieceBitboards[enemyBishop] | PieceBitboards[enemyQueen];
//    }
//
//    private void Initialize() {
//        AllGameMoves = new ArrayList<>();
//        KingSquare = new int[2];
//        Square = new int[64];
//
//        RepetitionPositionHistory = new Stack<Long>();
//        gameStateHistory = new Stack<GameState>();
//
//        CurrentGameState = new GameState();
//        PlyCount = 0;
//
//        Knights = new PieceList[]{new PieceList(10), new PieceList(10)};
//        Pawns = new PieceList[]{new PieceList(8), new PieceList(8)};
//        Rooks = new PieceList[]{new PieceList(10), new PieceList(10)};
//        Bishops = new PieceList[]{new PieceList(10), new PieceList(10)};
//        Queens = new PieceList[]{new PieceList(9), new PieceList(9)};
//
//        allPieceLists = new PieceList[Piece.MaxPieceIndex + 1];
//        allPieceLists[Piece.WhitePawn] = Pawns[WHITE_INDEX];
//        allPieceLists[Piece.WhiteKnight] = Knights[WHITE_INDEX];
//        allPieceLists[Piece.WhiteBishop] = Bishops[WHITE_INDEX];
//        allPieceLists[Piece.WhiteRook] = Rooks[WHITE_INDEX];
//        allPieceLists[Piece.WhiteQueen] = Queens[WHITE_INDEX];
//        allPieceLists[Piece.WhiteKing] = new PieceList(1);
//
//        allPieceLists[Piece.BlackPawn] = Pawns[BLACK_INDEX];
//        allPieceLists[Piece.BlackKnight] = Knights[BLACK_INDEX];
//        allPieceLists[Piece.BlackBishop] = Bishops[BLACK_INDEX];
//        allPieceLists[Piece.BlackRook] = Rooks[BLACK_INDEX];
//        allPieceLists[Piece.BlackQueen] = Queens[BLACK_INDEX];
//        allPieceLists[Piece.BlackKing] = new PieceList(1);
//
//        TotalPieceCountWithoutPawnsAndKings = 0;
//
//        // Initialize bitboards
//        PieceBitboards = new long[Piece.MaxPieceIndex + 1];
//        ColourBitboards = new long[2];
//        AllPiecesBitboard = 0;
//    }

}

