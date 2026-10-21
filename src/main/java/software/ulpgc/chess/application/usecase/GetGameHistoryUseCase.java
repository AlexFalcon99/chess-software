package software.ulpgc.chess.application.usecase;

import software.ulpgc.chess.application.port.input.GetGameHistoryPort;
import software.ulpgc.chess.application.port.output.RoomRepository;
import software.ulpgc.chess.domain.model.Move;
import software.ulpgc.chess.domain.model.Room;

import java.util.List;


public class GetGameHistoryUseCase implements GetGameHistoryPort{
    private final RoomRepository roomRepository;

    public GetGameHistoryUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Move> getHistory(String roomId){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada con ID: " + roomId));
        return room.getGame().getMoves();
    }
}
