package software.ulpgc.chess.application.port.input;

import software.ulpgc.chess.domain.model.Room;
import software.ulpgc.chess.domain.model.Move;

public interface MakeMovePort {
    Room makeMove(String roomId, Move move);

}
