package software.ulpgc.chess.domain.model;

public class Room {
    private final String id;
    private final String name;
    private final Game game;

    public Room(String id, String name, Game game) {
        this.id = id;
        this.name = name;
        this.game = game;
    }

    public boolean isFull(){
        return this.game.getStatus() != GameStatus.WAITING_FOR_PLAYERS;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Game getGame() {
        return game;
    }
}
