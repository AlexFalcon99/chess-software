export class JoinRoomUseCase {
    constructor(chessService) {
        this.chessService = chessService;
    }

    async execute(roomId, playerName) {
        if (!roomId.trim() || !playerName.trim()) {
            throw new Error("El identificador de sala y tu nombre son obligatorios");
        }
        return await this.chessService.joinRoom(roomId, playerName);
    }
}