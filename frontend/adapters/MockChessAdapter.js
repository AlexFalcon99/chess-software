import { ChessServicePort } from '../ports/ChessServicePort.js';

export class MockChessAdapter extends ChessServicePort {
    constructor() {
        super();
        this.room = {
            id: "MOCK-1234",
            name: "Sala Mock Test",
            game: {
                id: "GAME-999",
                whitePlayer: { id: "p1", username: "Alex (Mock)" },
                blackPlayer: null,
                currentTurn: "WHITE",
                status: "WAITING_FOR_PLAYERS",
                moves: []
            }
        };
    }

    async createRoom(roomName, creatorName) {
        this.room.name = roomName;
        this.room.game.whitePlayer.username = creatorName;
        return JSON.parse(JSON.stringify(this.room));
    }

    async joinRoom(roomId, playerName) {
        this.room.game.blackPlayer = { id: "p2", username: playerName };
        this.room.game.status = "IN_PROGRESS";
        return JSON.parse(JSON.stringify(this.room));
    }

    async makeMove(roomId, fromSquare, toSquare, playerColor, pieceType) {
        this.room.game.moves.push({ from: fromSquare, to: toSquare, color: playerColor });
        this.room.game.currentTurn = playerColor === 'WHITE' ? 'BLACK' : 'WHITE';
        return JSON.parse(JSON.stringify(this.room));
    }

    async getRoomState(roomId) {
        return JSON.parse(JSON.stringify(this.room));
    }

    async getHistory(roomId) {
        return JSON.parse(JSON.stringify(this.room.game.moves));
    }
}