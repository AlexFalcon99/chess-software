export class GetRoomStateUseCase {
    constructor(chessService) {
        this.chessService = chessService;
    }

    async execute(roomId) {
        if (!roomId) throw new Error("Se requiere identificador de sala");
        return await this.chessService.getRoomState(roomId);
    }
}