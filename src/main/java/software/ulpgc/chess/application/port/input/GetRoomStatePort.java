package software.ulpgc.chess.application.port.input;

import software.ulpgc.chess.domain.model.Room;



public interface GetRoomStatePort {
    Room getRoom(String roomId);
}
