package software.ulpgc.chess.infrastructure.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ChessApiController {
    private final HttpServer server;
    private final Gson gson;

    // Aquí inyectarás tus puertos o casos de uso de application.port.input
    // Por ejemplo: private final CreateRoomPort createRoomPort;

    public ChessApiController(int port, Gson gson) throws IOException {
        this.gson = gson;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        registerRoutes();
        this.server.setExecutor(null); // Crea un ejecutor por defecto
    }

    private void registerRoutes() {
        // Rutas principales de la API
        this.server.createContext("/api/rooms", this::handleRooms);
        this.server.createContext("/api/moves", this::handleMoves);
    }

    public void start() {
        this.server.start();
        System.out.println("Servidor iniciado en el puerto " + server.getAddress().getPort());
    }

    public void stop() {
        this.server.stop(0);
    }

    // Manejador para creación y consulta de salas (/api/rooms)
    private void handleRooms(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if ("POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange.getRequestBody());
                CreateRoomRequest request = gson.fromJson(body, CreateRoomRequest.class);

                // TODO: Llamar al puerto/caso de uso correspondiente
                // Room createdRoom = createRoomPort.execute(request.roomName(), request.creatorName());

                String jsonResponse = gson.toJson(Map.of(
                        "status", "CREATED",
                        "roomName", request.roomName(),
                        "creator", request.creatorName()
                ));
                sendResponse(exchange, 201, jsonResponse);

            } else if ("GET".equalsIgnoreCase(method)) {
                // TODO: Llamar al puerto para obtener salas o estado
                String jsonResponse = gson.toJson(Map.of("message", "Listado o estado de salas"));
                sendResponse(exchange, 200, jsonResponse);

            } else {
                sendResponse(exchange, 405, gson.toJson(Map.of("error", "Método no permitido")));
            }
        } catch (Exception e) {
            sendResponse(exchange, 400, gson.toJson(Map.of("error", e.getMessage())));
        }
    }

    // Manejador para jugadas (/api/moves)
    private void handleMoves(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if ("POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange.getRequestBody());
                MakeMoveRequest request = gson.fromJson(body, MakeMoveRequest.class);

                // TODO: Llamar al puerto/caso de uso de movimiento
                // makeMovePort.execute(...);

                String jsonResponse = gson.toJson(Map.of(
                        "status", "MOVE_APPLIED",
                        "piece", request.piece(),
                        "from", request.fromFile() + "" + request.fromRank(),
                        "to", request.toFile() + "" + request.toRank()
                ));
                sendResponse(exchange, 200, jsonResponse);

            } else {
                sendResponse(exchange, 405, gson.toJson(Map.of("error", "Método no permitido")));
            }
        } catch (Exception e) {
            sendResponse(exchange, 400, gson.toJson(Map.of("error", e.getMessage())));
        }
    }

    // Funciones auxiliares para I/O HTTP
    private String readRequestBody(InputStream is) throws IOException {
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        byte[] bytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
