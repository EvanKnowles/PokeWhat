package za.co.knowles.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knowles.pokewhat.domain.lookup.EHand;
import za.co.knowles.pokewhat.domain.Hand;
import za.co.knowles.pokewhat.domain.lookup.ESuit;

import static za.co.knowles.pokewhat.domain.lookup.ERank.*;

public class HandTest {

    @Test
    public void testFailHand() {
        Hand hand = new Hand();
        hand.addCard(FOUR, ESuit.CLUBS);
        hand.addCard(FOUR, ESuit.HEARTS);
        hand.addCard(FOUR, ESuit.SPADES);
        hand.addCard(FOUR, ESuit.DIAMONDS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.FOLD, result);
    }

    @Test
    public void testFourOfAKind() {
        Hand hand = new Hand();
        hand.addCard(FOUR, ESuit.CLUBS);
        hand.addCard(FOUR, ESuit.HEARTS);
        hand.addCard(FOUR, ESuit.SPADES);
        hand.addCard(FOUR, ESuit.DIAMONDS);
        hand.addCard(EIGHT, ESuit.CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.FOUR_OF_A_KIND, result);
    }

    @Test
    public void testStraightFlush() {
        Hand hand = new Hand();
        hand.addCard(NINE, ESuit.CLUBS);
        hand.addCard(TEN, ESuit.CLUBS);
        hand.addCard(JACK, ESuit.CLUBS);
        hand.addCard(QUEEN, ESuit.CLUBS);
        hand.addCard(KING, ESuit.CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.STRAIGHT_FLUSH, result);
    }

    @Test
    public void testStraight() {
        Hand hand = new Hand();
        hand.addCard(FOUR, ESuit.CLUBS);
        hand.addCard(FIVE, ESuit.HEARTS);
        hand.addCard(SIX, ESuit.CLUBS);
        hand.addCard(SEVEN, ESuit.CLUBS);
        hand.addCard(EIGHT, ESuit.CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.STRAIGHT, result);
    }

    @Test
    public void testFlush() {
        Hand hand = new Hand();
        hand.addCard(FOUR, ESuit.CLUBS);
        hand.addCard(TEN, ESuit.CLUBS);
        hand.addCard(SIX, ESuit.CLUBS);
        hand.addCard(SEVEN, ESuit.CLUBS);
        hand.addCard(EIGHT, ESuit.CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.FLUSH, result);
    }

    @Test
    public void testHighCard() {
        EHand result = HandResolver.getHand(new Hand(FOUR.of(ESuit.CLUBS), FIVE.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), THREE.of(ESuit.HEARTS), NINE.of(ESuit.SPADES)));
        Assertions.assertEquals(EHand.HIGH_CARD, result);
    }

    @Test
    public void testOnePair() {
        Hand hand = new Hand();
        hand.addCard(FOUR, ESuit.CLUBS);
        hand.addCard(FOUR, ESuit.HEARTS);
        hand.addCard(FIVE, ESuit.SPADES);
        hand.addCard(SEVEN, ESuit.DIAMONDS);
        hand.addCard(EIGHT, ESuit.CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.ONE_PAIR, result);
    }

    @Test
    public void testTwoPair() {
        Hand hand = new Hand();
        hand.addCard(FOUR, ESuit.CLUBS);
        hand.addCard(FOUR, ESuit.HEARTS);
        hand.addCard(FIVE, ESuit.SPADES);
        hand.addCard(FIVE, ESuit.DIAMONDS);
        hand.addCard(EIGHT, ESuit.CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.TWO_PAIR, result);
    }

    @Test
    public void testRoyalFlush() {
        Hand hand = new Hand();
        hand.addCard(TEN, ESuit.CLUBS);
        hand.addCard(JACK, ESuit.CLUBS);
        hand.addCard(QUEEN, ESuit.CLUBS);
        hand.addCard(KING, ESuit.CLUBS);
        hand.addCard(ACE, ESuit.CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.ROYAL_FLUSH, result);
    }

    @Test
    public void testThreeOfAKind() {
        Hand hand = new Hand();
        hand.addCard(FOUR, ESuit.CLUBS);
        hand.addCard(FOUR, ESuit.HEARTS);
        hand.addCard(FOUR, ESuit.SPADES);
        hand.addCard(SEVEN, ESuit.DIAMONDS);
        hand.addCard(EIGHT, ESuit.CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.THREE_OF_KIND, result);
    }

    @Test
    public void testFullHouse() {
        Hand hand = new Hand();
        hand.addCard(FOUR, ESuit.CLUBS);
        hand.addCard(FOUR, ESuit.HEARTS);
        hand.addCard(FOUR, ESuit.SPADES);
        hand.addCard(EIGHT, ESuit.DIAMONDS);
        hand.addCard(EIGHT, ESuit.CLUBS);

        EHand result = HandResolver.getHand(hand);
        Assertions.assertEquals(EHand.FULL_HOUSE, result);
    }
}
