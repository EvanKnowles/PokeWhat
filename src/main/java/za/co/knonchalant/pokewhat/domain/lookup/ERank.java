package za.co.knonchalant.pokewhat.domain.lookup;

import za.co.knonchalant.pokewhat.domain.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ERank {
    TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"), FIVE(5, "5"),
    SIX(6, "6"), SEVEN(7, "7"), EIGHT(8, "8"), NINE(9, "9"), TEN(10, "10"),
    JACK(11, "J"), QUEEN(12, "Q"), KING(13, "K"), ACE(14, "A");

    public static final List<Card> FULL_DECK;

    static {
        ArrayList<Card> buildDeck = new ArrayList<>();
        for (ERank value : values()) {
            for (ESuit suit : ESuit.values()) {
                buildDeck.add(value.of(suit));
            }
        }

        FULL_DECK = Collections.unmodifiableList(buildDeck);
    }

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
