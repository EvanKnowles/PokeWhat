package za.co.knowles.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knowles.pokewhat.domain.Card;
import za.co.knowles.pokewhat.domain.lookup.EHand;
import za.co.knowles.pokewhat.domain.Hand;
import za.co.knowles.pokewhat.domain.HandResult;
import za.co.knowles.pokewhat.domain.lookup.ESuit;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static za.co.knowles.pokewhat.domain.lookup.ERank.*;

public class HandGenerationTest {

    @Test
    public void testHand() {
        List<Card> cards = Arrays.asList(FOUR.of(ESuit.CLUBS), FIVE.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), THREE.of(ESuit.HEARTS), NINE.of(ESuit.SPADES), FOUR.of(ESuit.SPADES), THREE.of(ESuit.DIAMONDS));

        Hand generate = HandGeneration.getBest(Collections.emptyList(), cards);

        Assertions.assertEquals(EHand.TWO_PAIR, HandResolver.getHand(generate));
    }

    @Test
    public void testTieOnLowHighCardPair() {
        //Table cards: [A♦, J♠, 4♣, 4♠, 3♠]
        //Player Two:[6♦, 7♠]
        //Player One:[10♥, 7♦]
        //There's a tie for the Main Pot: Player One and Player Two win 4.0with 1 Pair
        List<Card> commonCards = Arrays.asList(ACE.of(ESuit.DIAMONDS), JACK.of(ESuit.SPADES), FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.SPADES), THREE.of(ESuit.SPADES));

        List<Card> playerOne = Arrays.asList(SIX.of(ESuit.DIAMONDS), SEVEN.of(ESuit.SPADES));
        List<Card> playerTwo = Arrays.asList(TEN.of(ESuit.HEARTS), SEVEN.of(ESuit.DIAMONDS));
        Hand playerOneHand = HandGeneration.getBest(commonCards, playerOne);
        Hand playerTwoHand = HandGeneration.getBest(commonCards, playerTwo);

        List<HandResult> compare = HandComparator.compare(Arrays.asList(playerOneHand, playerTwoHand));
        System.out.println(compare);
    }

    @Test
    public void testGame() {
        List<Card> commonCards = Arrays.asList(FOUR.of(ESuit.CLUBS), FIVE.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), THREE.of(ESuit.HEARTS), NINE.of(ESuit.SPADES));

        List<Card> playerOne = Arrays.asList(TEN.of(ESuit.DIAMONDS), TWO.of(ESuit.DIAMONDS));
        List<Card> playerTwo = Arrays.asList(THREE.of(ESuit.DIAMONDS), TWO.of(ESuit.CLUBS));

        Hand playerOneHand = HandGeneration.getBest(commonCards, playerOne);
        Hand playerTwoHand = HandGeneration.getBest(commonCards, playerTwo);

        List<HandResult> compare = HandComparator.compare(Arrays.asList(playerOneHand, playerTwoHand));

        Assertions.assertEquals(playerTwoHand, compare.get(0).getHand());
        Assertions.assertEquals(EHand.TWO_PAIR, compare.get(0).getHandResult());
    }
}
