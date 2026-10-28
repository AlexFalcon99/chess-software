package software.ulpgc.chess.infrastructure.persistence;

import software.ulpgc.chess.application.port.output.RoomRepository;
import software.ulpgc.chess.domain.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRoomRepository implements RoomRepository {
    // Almacenamiento thread-safe en memoria indexado por el ID de la sala
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    @Override
    public void save(Room room) {
        // Almacenar o actualizar la sala en memoria
        rooms.put(room.getId(), room);
    }

    @Override
    public Optional<Room> findById(String id) {
        // Búsqueda por identificador envuelta en Optional
        return Optional.ofNullable(rooms.get(id));
    }

    @Override
    public List<Room> findAll() {
        // Retornar lista con todas las salas activas
        return new ArrayList<>(rooms.values());
    }
}