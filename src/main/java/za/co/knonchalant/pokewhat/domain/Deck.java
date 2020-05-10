package za.co.knonchalant.pokewhat.domain;

import za.co.knonchalant.pokewhat.domain.lookup.ERank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>(ERank.FULL_DECK);
        Collections.shuffle(cards);
    }

    public Deck(List<Card> cards) {
        this.cards =  new ArrayList<>(cards);
    }

    public Card takeCard() {
        return cards.remove(0);
    }

    public int count() {
        return cards.size();
    }
}
