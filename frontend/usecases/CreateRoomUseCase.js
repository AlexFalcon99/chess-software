export class CreateRoomUseCase {
    constructor(chessService) {
        this.chessService = chessService;
    }

    async execute(roomName, creatorName) {
        if (!roomName.trim() || !creatorName.trim()) {
            throw new Error("El nombre de la sala y del creador son obligatorios");
        }
        return await this.chessService.createRoom(roomName, creatorName);
    }
}