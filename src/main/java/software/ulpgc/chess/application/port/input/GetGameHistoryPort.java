package software.ulpgc.chess.application.port.input;

import software.ulpgc.chess.domain.model.Move;

import java.util.List;

public interface GetGameHistoryPort {
    List<Move> getHistory(String roomId);
}
