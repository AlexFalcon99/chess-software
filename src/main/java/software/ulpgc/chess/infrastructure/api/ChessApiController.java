package software.ulpgc.chess.infrastructure.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import software.ulpgc.chess.application.port.input.*;
import software.ulpgc.chess.domain.model.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChessApiController {
    private final HttpServer server;
    private final Gson gson;

    private final CreateRoomPort createRoomPort;
    private final GetGameHistoryPort getGameHistoryPort;
    private final GetRoomStatePort getRoomStatePort;
    private final JoinRoomPort joinRoomPort;
    private final MakeMovePort makeMovePort;

    public ChessApiController(int port,
                              Gson gson,
                              CreateRoomPort createRoomPort,
                              GetGameHistoryPort getGameHistoryPort,
                              GetRoomStatePort getRoomStatePort,
                              JoinRoomPort joinRoomPort,
                              MakeMovePort makeMovePort) throws IOException {
        this.gson = gson;
        this.createRoomPort = createRoomPort;
        this.getGameHistoryPort = getGameHistoryPort;
        this.getRoomStatePort = getRoomStatePort;
        this.joinRoomPort = joinRoomPort;
        this.makeMovePort = makeMovePort;

        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        registerRoutes();
        this.server.setExecutor(null);
    }

    private void registerRoutes() {
        this.server.createContext("/api/rooms", this::handleRooms);
        this.server.createContext("/api/rooms/join", this::handleJoin);
        this.server.createContext("/api/moves", this::handleMoves);
        this.server.createContext("/api/rooms/move", this::handleMoves); // alias de compatibilidad
        this.server.createContext("/api/history", this::handleHistory);
    }

    public void start() {
        this.server.start();
    }

    public void stop() {
        this.server.stop(0);
    }

    private boolean handleCorsPreflight(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return true;
        }
        return false;
    }

    private void handleRooms(HttpExchange exchange) throws IOException {
        if (handleCorsPreflight(exchange)) return;
        String method = exchange.getRequestMethod();

        try {
            if ("POST".equalsIgnoreCase(method)) {
                String body = read(exchange.getRequestBody());
                CreateRoomRequest request = gson.fromJson(body, CreateRoomRequest.class);
                User creator = new User(request.creatorName(), UUID.randomUUID().toString());
                Room room = createRoomPort.createRoom(request.roomName(), creator);
                sendResponse(exchange, 201, room);
            } else if ("GET".equalsIgnoreCase(method)) {
                String roomId = getQueryParam(exchange.getRequestURI(), "id");
                if (roomId == null || roomId.isBlank()) {
                    sendResponse(exchange, 400, Map.of("error", "Missing 'id' query param"));
                    return;
                }
                Room state = getRoomStatePort.getRoom(roomId);
                sendResponse(exchange, 200, state);
            } else {
                sendResponse(exchange, 405, Map.of("error", "Method not allowed"));
            }
        } catch (Exception e) {
            sendResponse(exchange, 400, Map.of("error", e.getMessage()));
        }
    }

    private void handleJoin(HttpExchange exchange) throws IOException {
        if (handleCorsPreflight(exchange)) return;
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, Map.of("error", "Method not allowed"));
            return;
        }

        try {
            String body = read(exchange.getRequestBody());
            JoinRoomRequest request = gson.fromJson(body, JoinRoomRequest.class);
            User player = new User(request.playerName(), UUID.randomUUID().toString());
            Room room = joinRoomPort.joinRoom(request.roomId(), player);
            sendResponse(exchange, 200, room);
        } catch (Exception e) {
            sendResponse(exchange, 400, Map.of("error", e.getMessage()));
        }
    }

    private void handleMoves(HttpExchange exchange) throws IOException {
        if (handleCorsPreflight(exchange)) return;
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, Map.of("error", "Method not allowed"));
            return;
        }

        try {
            String body = read(exchange.getRequestBody());
            MakeMoveRequest request = gson.fromJson(body, MakeMoveRequest.class);

            Square from = new Square(request.fromFile(), request.fromRank());
            Square to = new Square(request.toFile(), request.toRank());

            Move move = new Move(
                    from,
                    to,
                    request.playerColor(),
                    request.piece() != null ? request.piece() : PieceType.PAWN,
                    0,
                    Instant.now()
            );

            Room room = makeMovePort.makeMove(request.roomId(), move);
            sendResponse(exchange, 200, room);
        } catch (Exception e) {
            sendResponse(exchange, 400, Map.of("error", e.getMessage()));
        }
    }

    private void handleHistory(HttpExchange exchange) throws IOException {
        if (handleCorsPreflight(exchange)) return;
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, Map.of("error", "Method not allowed"));
            return;
        }

        try {
            String roomId = getQueryParam(exchange.getRequestURI(), "id");
            if (roomId == null || roomId.isBlank()) {
                sendResponse(exchange, 400, Map.of("error", "Missing 'id' query param"));
                return;
            }
            List<Move> history = getGameHistoryPort.getHistory(roomId);
            sendResponse(exchange, 200, history);
        } catch (Exception e) {
            sendResponse(exchange, 400, Map.of("error", e.getMessage()));
        }
    }

    private String read(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }

    private void sendResponse(HttpExchange exchange, int statusCode, Object data) throws IOException {
        byte[] bytes = gson.toJson(data).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String getQueryParam(URI uri, String key) {
        String query = uri.getQuery();
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equalsIgnoreCase(key)) {
                return pair[1];
            }
        }
        return null;
    }
}