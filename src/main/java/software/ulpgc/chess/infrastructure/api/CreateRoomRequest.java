package software.ulpgc.chess.infrastructure.api;

public class CreateRoomRequest {
    private String roomName;
    private String creatorName;

    public CreateRoomRequest() {
    }

    public CreateRoomRequest(String roomName, String creatorName) {
        this.roomName = roomName;
        this.creatorName = creatorName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
