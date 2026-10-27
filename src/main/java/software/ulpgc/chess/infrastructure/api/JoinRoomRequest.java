package software.ulpgc.chess.infrastructure.api;

public class JoinRoomRequest {
    private String roomId;
    private String playerName;

    public JoinRoomRequest() {
    }

    public JoinRoomRequest(String roomId, String playerName) {
        this.roomId = roomId;
        this.playerName = playerName;
    }


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
