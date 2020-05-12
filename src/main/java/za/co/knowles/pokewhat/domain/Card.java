package za.co.knowles.pokewhat.domain;

import za.co.knowles.pokewhat.domain.lookup.ERank;
import za.co.knowles.pokewhat.domain.lookup.ESuit;

import java.util.Objects;

public class Card {
    private final ESuit suit;
    private final ERank rank;

    public Card(ESuit suit, ERank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public ESuit getSuit() {
        return suit;
    }

    public ERank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank + "" + suit.getPicture();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit &&
                rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }
}
