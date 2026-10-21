package software.ulpgc.chess.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private final String id;
    private final User whitePlayer;
    private User blackPlayer;
    private PieceColor currentTurn;
    private GameStatus status;
    private final List<Move> moves;

    public Game(String id, User whitePlayer) {
        this.id = id;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = null;
        this.currentTurn = PieceColor.WHITE;
        this.status = GameStatus.WAITING_FOR_PLAYERS;
        this.moves = new ArrayList<>();
    }

    public void join(User opponent){
        if(this.status != GameStatus.WAITING_FOR_PLAYERS){
            throw new IllegalStateException("La partida ya está llena");
        }
        this.blackPlayer = opponent;
        this.status = GameStatus.IN_PROGRESS;

    }

    public void makeMove(Move move){
        if(this.status != GameStatus.IN_PROGRESS){
            throw new IllegalStateException("No se pueden realizar movimientos");
        }
        if(move.getColor() != this.currentTurn){
            throw new IllegalArgumentException("No es el turno del color");
        }
        
        this.moves.add(move);
        this.currentTurn = (this.currentTurn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

    }

    public void resign(String playerId){
        if (status == GameStatus.IN_PROGRESS){
            status = GameStatus.RESIGNED;
        }
    }

    public void finish(GameStatus finalStatus){
        if (status == GameStatus.IN_PROGRESS) {
            this.status = finalStatus;
        }
    }
        


    public String getId() {
        return id;
    }

    public User getWhitePlayer() {
        return whitePlayer;
    }

    public User getBlackPlayer() {
        return blackPlayer;
    }

    public PieceColor getCurrentTurn() {
        return currentTurn;
    }

    public GameStatus getStatus() {
        return status;
    }

    public List<Move> getMoves() {
        return Collections.unmodifiableList(moves);
    }
}
