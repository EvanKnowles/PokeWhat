package za.co.knonchalant.pokewhat.domain;

public class Player {
    private final String name;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
