import { ChessServicePort } from '../ports/ChessServicePort.js';

export class HttpChessAdapter extends ChessServicePort {
    constructor(baseUrl = 'http://localhost:8080') {
        super();
        this.baseUrl = baseUrl.endsWith('/') ? baseUrl.slice(0, -1) : baseUrl;
    }

    async createRoom(roomName, creatorName) {
        const response = await fetch(`${this.baseUrl}/api/rooms`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ roomName, creatorName })
        });
        if (!response.ok) throw new Error(await this._extractError(response));
        return await response.json();
    }

    async joinRoom(roomId, playerName) {
        const response = await fetch(`${this.baseUrl}/api/rooms/join`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ roomId, playerName })
        });
        if (!response.ok) throw new Error(await this._extractError(response));
        return await response.json();
    }

    async makeMove(roomId, fromSquare, toSquare, playerColor, pieceType = 'PAWN') {
        const fileMap = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];
        const rankMap = ['ONE', 'TWO', 'THREE', 'FOUR', 'FIVE', 'SIX', 'SEVEN', 'EIGHT'];

        const payload = {
            roomId: roomId,
            piece: pieceType,
            fromFile: fileMap[fromSquare.charCodeAt(0) - 97],
            fromRank: rankMap[parseInt(fromSquare[1], 10) - 1],
            toFile: fileMap[toSquare.charCodeAt(0) - 97],
            toRank: rankMap[parseInt(toSquare[1], 10) - 1],
            playerColor: playerColor
        };

        const response = await fetch(`${this.baseUrl}/api/moves`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!response.ok) throw new Error(await this._extractError(response));
        return await response.json();
    }

    async getRoomState(roomId) {
        const response = await fetch(`${this.baseUrl}/api/rooms?id=${encodeURIComponent(roomId)}`);
        if (!response.ok) throw new Error(await this._extractError(response));
        return await response.json();
    }

    async getHistory(roomId) {
        const response = await fetch(`${this.baseUrl}/api/history?id=${encodeURIComponent(roomId)}`);
        if (!response.ok) throw new Error(await this._extractError(response));
        return await response.json();
    }

    async _extractError(response) {
        try {
            const data = await response.json();
            return data.error || `HTTP Error ${response.status}`;
        } catch {
            return `HTTP Error ${response.status}`;
        }
    }
}