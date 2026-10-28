package software.ulpgc.chess.domain.model;

import java.time.Instant;

public class Move {
    // Casillas encapsuladas mediante el Value Object Square
    private final Square from;
    private final Square to;

    // Propiedades de la pieza y metadatos del movimiento
    private final PieceColor color;
    private final PieceType type;
    private final int moveNumber;
    private final Instant timestamp;

    public Move(Square from, Square to, PieceColor color, PieceType type, int moveNumber, Instant timestamp) {
        this.from = from;
        this.to = to;
        this.color = color;
        this.type = type;
        this.moveNumber = moveNumber;
        this.timestamp = timestamp;
    }

    public Square getFrom() {
        return from;
    }

    public Square getTo() {
        return to;
    }

    public PieceColor getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}