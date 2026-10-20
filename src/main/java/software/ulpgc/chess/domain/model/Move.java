package software.ulpgc.chess.domain.model;

import java.time.Instant;

public class Move {
    private final String from;
    private final String to;
    private final PieceColor color;
    private final PieceType type;
    private final int moveNumber;
    private final Instant timestamp;

    public Move(String from, String to, PieceColor color, PieceType type, int moveNumber, Instant timestamp) {
        this.from = from;
        this.to = to;
        this.color = color;
        this.type = type;
        this.moveNumber = moveNumber;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
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
