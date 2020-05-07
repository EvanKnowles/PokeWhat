package za.co.knonchalant.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knonchalant.pokewhat.domain.Card;
import za.co.knonchalant.pokewhat.domain.Deck;

import java.util.HashSet;
import java.util.Set;

public class DeckTest {

    @Test
    public void deckTest() {
        Deck deck = new Deck();
        Assertions.assertEquals(52, deck.count());

        Card card = deck.takeCard();

        Assertions.assertNotNull(card);
        Assertions.assertEquals(51, deck.count());

        Set<Card> allCards = new HashSet<>();
        allCards.add(card);
        for (int i = 0; i < 51; i++) {
            allCards.add(deck.takeCard());
        }

        Assertions.assertEquals(0, deck.count());
        Assertions.assertEquals(52, allCards.size());
    }

}
