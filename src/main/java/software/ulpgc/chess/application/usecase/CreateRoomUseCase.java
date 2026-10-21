package software.ulpgc.chess.application.usecase;

import software.ulpgc.chess.application.port.input.CreateRoomPort;
import software.ulpgc.chess.application.port.output.RoomRepository;
import software.ulpgc.chess.domain.model.Game;
import software.ulpgc.chess.domain.model.Room;
import software.ulpgc.chess.domain.model.User;

import java.util.UUID;
public class CreateRoomUseCase implements CreateRoomPort{
    private final RoomRepository roomRepository;

    public CreateRoomUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(String roomName, User creator){
        String roomId = UUID.randomUUID().toString();
        String gameId = UUID.randomUUID().toString();

        Game game = new Game(gameId, creator);
        Room room = new Room(roomId, roomName, game);

        roomRepository.save(room);
        return room;
    }
}
