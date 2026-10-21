package software.ulpgc.chess.application.port.input;


import software.ulpgc.chess.domain.model.Room;
import software.ulpgc.chess.domain.model.User;
public interface CreateRoomPort {
    Room createRoom(String roomName, User creator);
}

