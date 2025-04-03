package chess.engine.GUI.Lichess;

import chess.engine.GUI.Lichess.Events.GameStatus;
import chess.engine.GUI.Lichess.Events.GameUpdate;
import chess.engine.GUI.Lichess.Events.LichessEvent;
import chess.engine.GUI.Lichess.Events.Type;
import chess.engine.Players.ai.AlphaBeta;
import chess.engine.Players.ai.MoveStrategy;
import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.pgn.FenUtility;

import java.util.HashMap;

public class LichessBot {
    private static final HashMap<String, GameStatus> GAME_MAP = new HashMap<>();
    static final MoveStrategy engine = new AlphaBeta(4);


    public static void main(String[] args) {
        LichessDao.streamEvents();
    }

    static void handleEvent(LichessEvent event) {
        Type type = event.getType();
        switch (type) {
            case CHALLENGE: {
                System.out.println("Challenge received");
                String challengeId = event.getChallenge().getGameId();
                LichessDao.acceptChallenge(challengeId);
                System.out.println("Challenge accepted");
                break;
            }
            case GAME_START: {
                String gameId = event.getGame().getGameId();
                GAME_MAP.put(gameId, event.getGame());
                LichessDao.streamGame(gameId);
                break;
            }
            case GAME_FINISH: {
                System.out.println("Game is finished");
                if (event.getGame().getMyColor().equals(event.getGame().getWinner())) {
                    System.out.println("I won");
                }  else {
                    System.out.println("I lost? or draw, idk");
                }
                break;
            }
        }
    }

    static void handleGameUpdate(String gameId, GameUpdate gameUpdate) {
        GameStatus game = GAME_MAP.get(gameId);
        game.update(gameUpdate);

        String[] moves = game.getMoves();
        String initialFen = game.getInitialFen();

        String fen = LichessUtility.getFenFromMoves(moves, initialFen);
        if (game.getDrawOffer()) {
            System.out.println("I consider the draw");
        }
        if (FenUtility.getCurrentMoveMakerFromFen(fen).toString().equals(game.getMyColor())) {
            String bestMove = calculateBestMove(fen);
            if (bestMove != null)
                LichessDao.sendMove(gameId, bestMove);
        } else {
            System.out.println("Just chilling, not my move to make");
        }
    }


    private static String calculateBestMove(String fen) {
        Board board = FenUtility.createGameFromFEN(fen);
        Move bestMove = engine.execute(board);
        return bestMove.getUCIMove();
    }


}
