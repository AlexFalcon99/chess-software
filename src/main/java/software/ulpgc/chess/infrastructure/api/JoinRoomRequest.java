package software.ulpgc.chess.infrastructure.api;

public record JoinRoomRequest (
        String roomId, String playerName
){}
