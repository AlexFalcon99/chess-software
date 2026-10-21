package software.ulpgc.chess.application.port.output;

import software.ulpgc.chess.domain.model.Room;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    void save(Room room);
    Optional<Room> findById(String id);
    List<Room> findAll();
}
