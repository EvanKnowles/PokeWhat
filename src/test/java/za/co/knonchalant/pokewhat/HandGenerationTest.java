package za.co.knonchalant.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knonchalant.pokewhat.domain.Card;
import za.co.knonchalant.pokewhat.domain.EHand;
import za.co.knonchalant.pokewhat.domain.Hand;
import za.co.knonchalant.pokewhat.domain.HandResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static za.co.knonchalant.pokewhat.domain.ERank.*;
import static za.co.knonchalant.pokewhat.domain.ESuit.*;

public class HandGenerationTest {

    @Test
    public void testHand() {
        List<Card> cards = Arrays.asList(FOUR.of(CLUBS), FIVE.of(DIAMONDS), TWO.of(SPADES), THREE.of(HEARTS), NINE.of(SPADES), FOUR.of(SPADES), THREE.of(DIAMONDS));

        Hand generate = HandGeneration.getBest(Collections.emptyList(), cards);

        Assertions.assertEquals(EHand.TWO_PAIR, HandResolver.getHand(generate));
    }

    @Test
    public void testGame() {
        List<Card> commonCards = Arrays.asList(FOUR.of(CLUBS), FIVE.of(DIAMONDS), TWO.of(SPADES), THREE.of(HEARTS), NINE.of(SPADES));

        List<Card> playerOne = Arrays.asList(TEN.of(DIAMONDS), TWO.of(DIAMONDS));
        List<Card> playerTwo = Arrays.asList(THREE.of(DIAMONDS), TWO.of(CLUBS));

        Hand playerOneHand = HandGeneration.getBest(commonCards, playerOne);
        Hand playerTwoHand = HandGeneration.getBest(commonCards, playerTwo);

        List<HandResult> compare = HandComparator.compare(Arrays.asList(playerOneHand, playerTwoHand));

        Assertions.assertEquals(playerTwoHand, compare.get(0).getHand());
        Assertions.assertEquals(EHand.TWO_PAIR, compare.get(0).getHandResult());
    }
}
