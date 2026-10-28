package software.ulpgc.chess.domain.model;

public record Square(File file, Rank rank) {
    @Override
    public String toString(){
        return file.name() + rank.name().replace("R", "");
    }
}
