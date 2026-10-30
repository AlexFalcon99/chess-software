export class ChessServicePort {
    async createRoom(roomName, creatorName) {
        throw new Error("Method createRoom() not implemented");
    }

    async joinRoom(roomId, playerName) {
        throw new Error("Method joinRoom() not implemented");
    }

    async makeMove(roomId, fromSquare, toSquare, playerColor, pieceType) {
        throw new Error("Method makeMove() not implemented");
    }

    async getRoomState(roomId) {
        throw new Error("Method getRoomState() not implemented");
    }

    async getHistory(roomId) {
        throw new Error("Method getHistory() not implemented");
    }
}