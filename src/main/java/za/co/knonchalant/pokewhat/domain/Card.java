package za.co.knonchalant.pokewhat.domain;

public class Card {
    private ESuit suit;
    private ERank rank;

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
}
