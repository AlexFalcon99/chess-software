package software.ulpgc.chess.application.usecase;

import software.ulpgc.chess.application.port.input.GetRoomStatePort;
import software.ulpgc.chess.application.port.output.RoomRepository;
import software.ulpgc.chess.domain.model.Room;


public class GetRoomStateUseCase implements GetRoomStatePort{
    private final RoomRepository roomRepository;

    public GetRoomStateUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;

    }

    @Override
    public Room getRoom(String roomId){

        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada con ID: " + roomId));
    }
}
