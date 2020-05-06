package za.co.knonchalant.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knonchalant.pokewhat.domain.EHand;
import za.co.knonchalant.pokewhat.domain.Hand;

import static za.co.knonchalant.pokewhat.domain.ERank.*;
import static za.co.knonchalant.pokewhat.domain.ESuit.*;

public class HandTest {

    @Test
    public void testFailHand() {
        Hand hand = new Hand();
        hand.addCard(FOUR, CLUBS);
        hand.addCard(FOUR, HEARTS);
        hand.addCard(FOUR, SPADES);
        hand.addCard(FOUR, DIAMONDS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.FOLD, result);
    }

    @Test
    public void testFourOfAKind() {
        Hand hand = new Hand();
        hand.addCard(FOUR, CLUBS);
        hand.addCard(FOUR, HEARTS);
        hand.addCard(FOUR, SPADES);
        hand.addCard(FOUR, DIAMONDS);
        hand.addCard(EIGHT, CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.FOUR_OF_A_KIND, result);
    }

    @Test
    public void testStraightFlush() {
        Hand hand = new Hand();
        hand.addCard(NINE, CLUBS);
        hand.addCard(TEN, CLUBS);
        hand.addCard(JACK, CLUBS);
        hand.addCard(QUEEN, CLUBS);
        hand.addCard(KING, CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.STRAIGHT_FLUSH, result);
    }

    @Test
    public void testStraight() {
        Hand hand = new Hand();
        hand.addCard(FOUR, CLUBS);
        hand.addCard(FIVE, HEARTS);
        hand.addCard(SIX, CLUBS);
        hand.addCard(SEVEN, CLUBS);
        hand.addCard(EIGHT, CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.STRAIGHT, result);
    }

    @Test
    public void testFlush() {
        Hand hand = new Hand();
        hand.addCard(FOUR, CLUBS);
        hand.addCard(TEN, CLUBS);
        hand.addCard(SIX, CLUBS);
        hand.addCard(SEVEN, CLUBS);
        hand.addCard(EIGHT, CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.FLUSH, result);
    }

    @Test
    public void testHighCard() {
        EHand result = HandResolver.getHand(new Hand(FOUR.of(CLUBS), FIVE.of(DIAMONDS), TWO.of(SPADES), THREE.of(HEARTS), NINE.of(SPADES)));
        Assertions.assertEquals(EHand.HIGH_CARD, result);
    }

    @Test
    public void testOnePair() {
        Hand hand = new Hand();
        hand.addCard(FOUR, CLUBS);
        hand.addCard(FOUR, HEARTS);
        hand.addCard(FIVE, SPADES);
        hand.addCard(SEVEN, DIAMONDS);
        hand.addCard(EIGHT, CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.ONE_PAIR, result);
    }

    @Test
    public void testTwoPair() {
        Hand hand = new Hand();
        hand.addCard(FOUR, CLUBS);
        hand.addCard(FOUR, HEARTS);
        hand.addCard(FIVE, SPADES);
        hand.addCard(FIVE, DIAMONDS);
        hand.addCard(EIGHT, CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.TWO_PAIR, result);
    }

    @Test
    public void testRoyalFlush() {
        Hand hand = new Hand();
        hand.addCard(TEN, CLUBS);
        hand.addCard(JACK, CLUBS);
        hand.addCard(QUEEN, CLUBS);
        hand.addCard(KING, CLUBS);
        hand.addCard(ACE, CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.ROYAL_FLUSH, result);
    }

    @Test
    public void testThreeOfAKind() {
        Hand hand = new Hand();
        hand.addCard(FOUR, CLUBS);
        hand.addCard(FOUR, HEARTS);
        hand.addCard(FOUR, SPADES);
        hand.addCard(SEVEN, DIAMONDS);
        hand.addCard(EIGHT, CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.THREE_OF_KIND, result);
    }

    @Test
    public void testFullHouse() {
        Hand hand = new Hand();
        hand.addCard(FOUR, CLUBS);
        hand.addCard(FOUR, HEARTS);
        hand.addCard(FOUR, SPADES);
        hand.addCard(EIGHT, DIAMONDS);
        hand.addCard(EIGHT, CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.FULL_HOUSE, result);
    }
}
