package software.ulpgc.chess.domain.model;

public class User {
    private final String id;
    private final String username;

    public User(String username, String id) {
        this.username = username;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
