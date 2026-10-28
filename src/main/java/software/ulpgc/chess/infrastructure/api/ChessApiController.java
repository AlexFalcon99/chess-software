package software.ulpgc.chess.infrastructure.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import software.ulpgc.chess.application.port.input.CreateRoomPort;
import software.ulpgc.chess.application.port.input.GetGameHistoryPort;
import software.ulpgc.chess.application.port.input.GetRoomStatePort;
import software.ulpgc.chess.application.port.input.JoinRoomPort;
import software.ulpgc.chess.application.port.input.MakeMovePort;
import software.ulpgc.chess.domain.model.Move;
import software.ulpgc.chess.domain.model.Room;
import software.ulpgc.chess.domain.model.Square;
import software.ulpgc.chess.domain.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

    // Puertos de entrada de la aplicación
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
        // Endpoints de la API
        this.server.createContext("/api/rooms", this::handleRooms);
        this.server.createContext("/api/rooms/join", this::handleJoin);
        this.server.createContext("/api/moves", this::handleMoves);
        this.server.createContext("/api/history", this::handleHistory);
    }

    public void start() {
        this.server.start();
    }

    public void stop() {
        this.server.stop(0);
    }

    // Manejador: Crear sala (POST) y consultar estado (GET /api/rooms?id=...)
    private void handleRooms(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if ("POST".equalsIgnoreCase(method)) {
                String body = read(exchange.getRequestBody());
                CreateRoomRequest request = gson.fromJson(body, CreateRoomRequest.class);

                // User(username, id)
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

    // Manejador: Unirse a sala (POST)
    private void handleJoin(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, Map.of("error", "Method not allowed"));
            return;
        }

        try {
            String body = read(exchange.getRequestBody());
            JoinRoomRequest request = gson.fromJson(body, JoinRoomRequest.class);

            // User(username, id)
            User player = new User(request.playerName(), UUID.randomUUID().toString());
            Room room = joinRoomPort.joinRoom(request.roomId(), player);
            sendResponse(exchange, 200, room);

        } catch (Exception e) {
            sendResponse(exchange, 400, Map.of("error", e.getMessage()));
        }
    }

    // Manejador: Enviar movimiento (POST)
    private void handleMoves(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, Map.of("error", "Method not allowed"));
            return;
        }

        try {
            String body = read(exchange.getRequestBody());
            MakeMoveRequest request = gson.fromJson(body, MakeMoveRequest.class);

            // Creación de las casillas origen y destino
            Square from = new Square(request.fromFile(), request.fromRank());
            Square to = new Square(request.toFile(), request.toRank());

            // Instanciación de Move: Move(from, to, color, type, moveNumber, timestamp)
            Move move = new Move(
                    from,
                    to,
                    request.playerColor(),
                    request.piece(),
                    0,
                    Instant.now()
            );

            Room room = makeMovePort.makeMove(request.roomId(), move);
            sendResponse(exchange, 200, room);

        } catch (Exception e) {
            sendResponse(exchange, 400, Map.of("error", e.getMessage()));
        }
    }

    // Manejador: Consultar historial (GET /api/history?id=...)
    private void handleHistory(HttpExchange exchange) throws IOException {
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

    // Lectura del flujo de entrada
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

    // Respuesta HTTP con serialización Gson
    private void sendResponse(HttpExchange exchange, int statusCode, Object data) throws IOException {
        byte[] bytes = gson.toJson(data).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // Obtener parámetros de consulta de la URI
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