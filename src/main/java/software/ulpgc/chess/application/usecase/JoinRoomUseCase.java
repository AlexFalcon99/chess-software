package software.ulpgc.chess.application.usecase;


import software.ulpgc.chess.application.port.input.JoinRoomPort;
import software.ulpgc.chess.application.port.output.RoomRepository;
import software.ulpgc.chess.domain.model.Room;
import software.ulpgc.chess.domain.model.User;


public class JoinRoomUseCase implements JoinRoomPort {
    private final RoomRepository roomRepository;

    public JoinRoomUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    @Override
    public Room joinRoom(String roomId, User player){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada: " + roomId));

        room.getGame().join(player);
        roomRepository.save(room);

        return room;
    }
}
