package za.co.knonchalant.pokewhat.domain;

public enum ERank {
    ONE(1, "1"), TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"), FIVE(5, "5"),
    SIX(6, "6"), SEVEN(7, "7"), EIGHT(8, "8"), NINE(9, "9"), TEN(10, "10"),
    JACK(11, "J"), QUEEN(12, "Q"), KING(13, "K"), ACE(14, "A");

    private final int numeric;
    private final String name;

    ERank(int numeric, String name) {
        this.numeric = numeric;
        this.name = name;
    }

    public Card of(ESuit suit) {
        return new Card(suit, this);
    }

    public int getNumeric() {
        return numeric;
    }

    public double getPositionForCardRank() {
        return Math.pow(2, numeric * 4);
    }

    @Override
    public String toString() {
        return name;
    }
}
