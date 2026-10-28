package software.ulpgc.chess.application.usecase;

import software.ulpgc.chess.application.port.input.CreateRoomPort;
import software.ulpgc.chess.application.port.input.GetGameHistoryPort;
import software.ulpgc.chess.application.port.input.GetRoomStatePort;
import software.ulpgc.chess.application.port.input.JoinRoomPort;
import software.ulpgc.chess.application.port.input.MakeMovePort;
import software.ulpgc.chess.application.port.output.RoomRepository;
import software.ulpgc.chess.domain.model.Game;
import software.ulpgc.chess.domain.model.Move;
import software.ulpgc.chess.domain.model.Room;
import software.ulpgc.chess.domain.model.User;

import java.util.List;
import java.util.UUID;

public class ChessService implements CreateRoomPort, JoinRoomPort, MakeMovePort, GetRoomStatePort, GetGameHistoryPort {

    private final RoomRepository roomRepository;

    public ChessService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(String roomName, User creator) {
        String roomId = UUID.randomUUID().toString().substring(0, 8);
        String gameId = UUID.randomUUID().toString().substring(0, 8);

        // Se inicializa la partida con el creador como blancas y la sala asociada
        Game game = new Game(gameId, creator);
        Room room = new Room(roomId, roomName, game);

        roomRepository.save(room);
        return room;
    }

    @Override
    public Room joinRoom(String roomId, User player) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la sala: " + roomId));

        // Lógica de unirse delegada en Game (negras y estado IN_PROGRESS)
        room.getGame().join(player);
        roomRepository.save(room);
        return room;
    }

    @Override
    public Room makeMove(String roomId, Move move) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la sala: " + roomId));

        // Validación de turno y registro del movimiento en Game
        room.getGame().makeMove(move);
        roomRepository.save(room);
        return room;
    }

    @Override
    public Room getRoom(String roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la sala: " + roomId));
    }

    @Override
    public List<Move> getHistory(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la sala: " + roomId));

        // Retorna la lista inmutable de movimientos
        return room.getGame().getMoves();
    }
}