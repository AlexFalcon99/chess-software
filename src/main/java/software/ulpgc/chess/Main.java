package software.ulpgc.chess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import software.ulpgc.chess.application.usecase.ChessService;
import software.ulpgc.chess.infrastructure.api.ChessApiController;
import software.ulpgc.chess.infrastructure.persistence.InMemoryRoomRepository;

import java.io.IOException;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        int port = 8080;

        // 1. Repositorio en memoria (puerto de salida)
        InMemoryRoomRepository roomRepository = new InMemoryRoomRepository();

        // 2. Casos de uso / servicio de aplicación
        ChessService chessService = new ChessService(roomRepository);

        // 3. Serializador Gson con soporte para fechas Instant
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class,
                        (JsonSerializer<Instant>) (src, type, ctx) -> new JsonPrimitive(src.toString()))
                .setPrettyPrinting()
                .create();

        try {
            // 4. Inyección de dependencias en el controlador HTTP
            ChessApiController controller = new ChessApiController(
                    port,
                    gson,
                    chessService,
                    chessService,
                    chessService,
                    chessService,
                    chessService
            );

            // 5. Iniciar servidor
            controller.start();
            System.out.println("=========================================");
            System.out.println("  Servidor de Ajedrez iniciado con éxito  ");
            System.out.println("  Escuchando en: http://localhost:" + port);
            System.out.println("=========================================");

        } catch (IOException e) {
            System.err.println("Error fatal al arrancar el servidor: " + e.getMessage());
        }
    }
}