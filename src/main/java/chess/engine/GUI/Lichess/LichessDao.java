package chess.engine.GUI.Lichess;

import chess.engine.GUI.Lichess.Events.GameUpdate;
import chess.engine.GUI.Lichess.Events.LichessEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;


public class LichessDao {
    final private static String API_TOKEN_PATH = "C:\\Users\\hrhaa\\kodeord\\lichessapikey.txt";
    final private static String API_TOKEN = LichessUtility.readPasswordFromFile(API_TOKEN_PATH);
    final private static String LICHESS_BASE_URL = "https://lichess.org/api/";
    final private static String STREAM_EVENT_PATH = "stream/event";
    final private static String STREAM_GAME_PATH = "bot/game/stream/";
    final private static String SEND_MOVE_PATH = "bot/game/{gameId}/move/{move}";
    final private static String CHALLENGE_PATH = "challenge/{challengeId}/accept";
    final private static String BOT_UPGRADE_PATH = "bot/account/upgrade";
    final private static String DRAW_PATH = "bot/game/{gameId}/draw/{accept}";


    static void streamEvents() {
        System.out.println(API_TOKEN);
        String url = LICHESS_BASE_URL + STREAM_EVENT_PATH;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = buildGetEvent(url);
        // Listen for events
        CompletableFuture<Void> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> response.body().forEach(line -> {
                    if (!line.isEmpty()) {
                        System.out.println("Received Event: " + line);
                        try {
                            LichessEvent lichessEvent = Mapper.jsonToLichessEvent(line);
                            LichessBot.handleEvent(lichessEvent);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
        future.join();
    }


    public static void streamGame(String gameId) {
        String url = LICHESS_BASE_URL + STREAM_GAME_PATH + gameId;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = buildGetEvent(url);

        client.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> response.body().forEach(line -> {
                    if (!line.isEmpty()) {
                        System.out.println("Game Update: " + line);
                        try {
                            GameUpdate gameUpdate = Mapper.jsonToGameUpdate(gameId, line);
                            LichessBot.handleGameUpdate(gameId, gameUpdate);

                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }))
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }


    static void sendMove(String gameId, String move) {
        String url = LICHESS_BASE_URL + SEND_MOVE_PATH.replace("{gameId}", gameId).replace("{move}", move);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + API_TOKEN)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Move played: " + move);
            } else {
                System.err.println("Failed to play move. Status: " + response.statusCode());
                System.err.println("Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void acceptChallenge(String challengeId) {
        String url = LICHESS_BASE_URL + CHALLENGE_PATH.replace("{challengeId}", challengeId);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = buildPOSTEvent(url, HttpRequest.BodyPublishers.noBody());

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Accepted challenge: " + challengeId);
            } else {
                System.err.println("Failed to accept challenge: " + challengeId);
                System.err.println("Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HttpRequest buildGetEvent(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + LichessDao.API_TOKEN)
                .GET()
                .build();
    }

    private static HttpRequest buildPOSTEvent(String url, HttpRequest.BodyPublisher body) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + LichessDao.API_TOKEN) // Add the token
                .POST(body)
                .build();
    }

    @SuppressWarnings("unused")
    private void upgradeAccount() {//If I need to upgrade an account again
        final String url = LICHESS_BASE_URL + BOT_UPGRADE_PATH;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = buildPOSTEvent(url, HttpRequest.BodyPublishers.noBody());

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("HTTP Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 200) {
                System.out.println("Account successfully upgraded to a bot!");
            } else {
                System.err.println("Failed to upgrade account. Please check the response body for more details.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void acceptDrawOffer(String gameId, boolean accept) {
        String toTakeOrNotToTake = (accept) ? "true" : "false";
        final String url = LICHESS_BASE_URL + DRAW_PATH.replace("{gameId}", gameId).replace("{accept}", toTakeOrNotToTake);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = buildPOSTEvent(url, HttpRequest.BodyPublishers.noBody());
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("HTTP Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 200) {
                if (accept) {
                    System.out.println("Draw offer was accepted!");
                } else {
                    System.out.println("Draw offer was declined");
                }
            } else {
                System.err.println("Failed to handle draw offer.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

