package za.co.knonchalant.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knonchalant.pokewhat.domain.lookup.EHand;
import za.co.knonchalant.pokewhat.domain.Hand;
import za.co.knonchalant.pokewhat.domain.HandResult;

import java.util.ArrayList;
import java.util.List;

import static za.co.knonchalant.pokewhat.domain.lookup.ERank.*;
import static za.co.knonchalant.pokewhat.domain.lookup.ESuit.*;
import static za.co.knonchalant.pokewhat.domain.lookup.ESuit.SPADES;

public class HandCompareTieTest {
    @Test
    public void testFourOfAKindTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), FOUR.of(SPADES), FOUR.of(HEARTS), TWO.of(SPADES));
        Hand two = new Hand(FIVE.of(CLUBS), FIVE.of(DIAMONDS), FIVE.of(SPADES), FIVE.of(HEARTS), THREE.of(SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
    }

    @Test
    public void testHighCardTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), THREE.of(DIAMONDS), TWO.of(SPADES), SEVEN.of(HEARTS), EIGHT.of(SPADES));
        Hand two = new Hand(FIVE.of(CLUBS), THREE.of(DIAMONDS), TWO.of(SPADES), SEVEN.of(HEARTS), EIGHT.of(SPADES));


        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
    }

    @Test
    public void testHighCardActualTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), THREE.of(DIAMONDS), TWO.of(SPADES), SEVEN.of(HEARTS), EIGHT.of(SPADES));
        Hand two = new Hand(FOUR.of(CLUBS), THREE.of(DIAMONDS), TWO.of(SPADES), SEVEN.of(HEARTS), EIGHT.of(SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        pureTie(compare);
    }

    private void pureTie(List<HandResult> compare) {
        for (HandResult handResult : compare) {
            Assertions.assertTrue(handResult.isTieBreakWinner());
        }
    }

    @Test
    public void testFourOfAKindHighCardTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), FOUR.of(SPADES), FOUR.of(HEARTS), TWO.of(SPADES));
        Hand two = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), FOUR.of(SPADES), FOUR.of(HEARTS), THREE.of(SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
    }

    @Test
    public void testThreeOfAKindTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), FOUR.of(SPADES), SEVEN.of(HEARTS), TWO.of(SPADES));
        Hand two = new Hand(FIVE.of(CLUBS), FIVE.of(DIAMONDS), FIVE.of(SPADES), TWO.of(HEARTS), THREE.of(SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
    }

    @Test
    public void testTwoPairTieHighCard() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), FIVE.of(SPADES), FIVE.of(HEARTS), TWO.of(SPADES));
        Hand two = new Hand(FIVE.of(CLUBS), FIVE.of(DIAMONDS), FOUR.of(SPADES), FOUR.of(HEARTS), EIGHT.of(SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
        Assertions.assertEquals(compare.get(1).getHandResult(), EHand.TWO_PAIR);
    }

    @Test
    public void testTwoPairTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), FIVE.of(SPADES), FIVE.of(HEARTS), TWO.of(SPADES));
        Hand two = new Hand(SIX.of(CLUBS), SIX.of(DIAMONDS), FOUR.of(SPADES), FOUR.of(HEARTS), EIGHT.of(SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
        Assertions.assertEquals(compare.get(1).getHandResult(), EHand.TWO_PAIR);
    }

    @Test
    public void testFullHouseTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), FIVE.of(SPADES), FIVE.of(HEARTS), FOUR.of(SPADES));
        Hand two = new Hand(SIX.of(CLUBS), SIX.of(DIAMONDS), FOUR.of(SPADES), FOUR.of(HEARTS), SIX.of(SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
        Assertions.assertEquals(compare.get(1).getHandResult(), EHand.FULL_HOUSE);
    }

    @Test
    public void testFullHouseHighCardTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(SIX.of(CLUBS), SIX.of(DIAMONDS), FOUR.of(SPADES), FOUR.of(HEARTS), SIX.of(SPADES));
        Hand two = new Hand(SIX.of(CLUBS), SIX.of(DIAMONDS), FOUR.of(SPADES), FOUR.of(HEARTS), SIX.of(SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        pureTie(compare);
    }

    @Test
    public void testTwoOfAKindTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), FIVE.of(SPADES), SEVEN.of(HEARTS), TWO.of(SPADES));
        Hand two = new Hand(FIVE.of(CLUBS), FIVE.of(DIAMONDS), FOUR.of(SPADES), TWO.of(HEARTS), EIGHT.of(SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
        Assertions.assertEquals(compare.get(1).getHandResult(), EHand.ONE_PAIR);
    }

    @Test
    public void testTwoOfAKindHighCardTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), FIVE.of(SPADES), SEVEN.of(HEARTS), TWO.of(SPADES));
        Hand two = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), FOUR.of(SPADES), TWO.of(HEARTS), EIGHT.of(SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
        Assertions.assertEquals(compare.get(1).getHandResult(), EHand.ONE_PAIR);
    }

    @Test
    public void testThreeOfAKindHighCardTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), FOUR.of(SPADES), SEVEN.of(HEARTS), TWO.of(SPADES));
        Hand two = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), FOUR.of(SPADES), TWO.of(HEARTS), EIGHT.of(SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
        Assertions.assertEquals(compare.get(1).getHandResult(), EHand.THREE_OF_KIND);
    }

    @Test
    public void testStraightTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), FIVE.of(DIAMONDS), SIX.of(SPADES), SEVEN.of(HEARTS), EIGHT.of(SPADES));
        Hand two = new Hand(FIVE.of(DIAMONDS), SIX.of(SPADES), SEVEN.of(HEARTS), EIGHT.of(SPADES), NINE.of(CLUBS));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(one, compare.get(1).getHand());
        Assertions.assertEquals(EHand.STRAIGHT, compare.get(1).getHandResult());
    }

    @Test
    public void testStraightFlushTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(CLUBS), FIVE.of(CLUBS), SIX.of(CLUBS), SEVEN.of(CLUBS), EIGHT.of(CLUBS));
        Hand two = new Hand(FIVE.of(CLUBS), SIX.of(CLUBS), SEVEN.of(CLUBS), EIGHT.of(CLUBS), NINE.of(CLUBS));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(one, compare.get(1).getHand());
        Assertions.assertEquals(EHand.STRAIGHT_FLUSH, compare.get(1).getHandResult());
    }


}
