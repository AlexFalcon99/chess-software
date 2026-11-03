export class MakeMoveUseCase {
    constructor(chessService) {
        this.chessService = chessService;
    }

    async execute(roomId, fromSquare, toSquare, playerColor) {
        if (!roomId) throw new Error("No hay partida activa");
        if (fromSquare === toSquare) throw new Error("La casilla de destino debe ser diferente");
        return await this.chessService.makeMove(roomId, fromSquare, toSquare, playerColor);
    }
}