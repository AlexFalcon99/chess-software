package software.ulpgc.chess.application.port.input;

import software.ulpgc.chess.domain.model.Room;
import software.ulpgc.chess.domain.model.User;

public interface JoinRoomPort {
    Room joinRoom(String roomID, User player);
}
