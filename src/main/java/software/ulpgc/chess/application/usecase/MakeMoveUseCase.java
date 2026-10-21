package software.ulpgc.chess.application.usecase;


import software.ulpgc.chess.application.port.input.MakeMovePort;
import software.ulpgc.chess.application.port.output.RoomRepository;
import software.ulpgc.chess.domain.model.Move;
import software.ulpgc.chess.domain.model.Room;

public class MakeMoveUseCase implements MakeMovePort{
    private final RoomRepository roomRepository;

    public MakeMoveUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room makeMove(String roomId, Move move ){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada: " + roomId));
        room.getGame().makeMove(move);
        roomRepository.save(room);
        return room;
    }
}
