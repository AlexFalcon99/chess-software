package software.ulpgc.chess.infrastructure.api;

public record CreateRoomRequest (
    String roomName,
    String creatorName
){}