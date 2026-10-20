package software.ulpgc.chess.domain.model;

public class User {
    private final String id;
    private String username;

    public User(String username, String id) {
        this.username = username;
        this.id = id;
    }
}
