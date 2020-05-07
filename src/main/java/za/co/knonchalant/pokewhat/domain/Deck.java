package za.co.knonchalant.pokewhat.domain;

import za.co.knonchalant.pokewhat.domain.lookup.ERank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    private final List<Card> cards = new ArrayList<>(ERank.FULL_DECK);

    private final Random random = new Random();

    public Card takeCard() {
        int cardIndex = random.nextInt(cards.size());
        return cards.remove(cardIndex);
    }

    public int count() {
        return cards.size();
    }
}
