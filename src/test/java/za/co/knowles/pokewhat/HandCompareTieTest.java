package za.co.knowles.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knowles.pokewhat.domain.lookup.EHand;
import za.co.knowles.pokewhat.domain.Hand;
import za.co.knowles.pokewhat.domain.HandResult;
import za.co.knowles.pokewhat.domain.lookup.ESuit;

import java.util.ArrayList;
import java.util.List;

import static za.co.knowles.pokewhat.domain.lookup.ERank.*;

public class HandCompareTieTest {
    @Test
    public void testFourOfAKindTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), FOUR.of(ESuit.HEARTS), TWO.of(ESuit.SPADES));
        Hand two = new Hand(FIVE.of(ESuit.CLUBS), FIVE.of(ESuit.DIAMONDS), FIVE.of(ESuit.SPADES), FIVE.of(ESuit.HEARTS), THREE.of(ESuit.SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
    }

    @Test
    public void testHighCardTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), THREE.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), SEVEN.of(ESuit.HEARTS), EIGHT.of(ESuit.SPADES));
        Hand two = new Hand(FIVE.of(ESuit.CLUBS), THREE.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), SEVEN.of(ESuit.HEARTS), EIGHT.of(ESuit.SPADES));


        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
    }

    @Test
    public void testHighCardActualTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), THREE.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), SEVEN.of(ESuit.HEARTS), EIGHT.of(ESuit.SPADES));
        Hand two = new Hand(FOUR.of(ESuit.CLUBS), THREE.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), SEVEN.of(ESuit.HEARTS), EIGHT.of(ESuit.SPADES));

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

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), FOUR.of(ESuit.HEARTS), TWO.of(ESuit.SPADES));
        Hand two = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), FOUR.of(ESuit.HEARTS), THREE.of(ESuit.SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(0).getHand(), two);
    }

    @Test
    public void testThreeOfAKindTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), SEVEN.of(ESuit.HEARTS), TWO.of(ESuit.SPADES));
        Hand two = new Hand(FIVE.of(ESuit.CLUBS), FIVE.of(ESuit.DIAMONDS), FIVE.of(ESuit.SPADES), TWO.of(ESuit.HEARTS), THREE.of(ESuit.SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
    }

    @Test
    public void testTwoPairTieHighCard() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), FIVE.of(ESuit.SPADES), FIVE.of(ESuit.HEARTS), TWO.of(ESuit.SPADES));
        Hand two = new Hand(FIVE.of(ESuit.CLUBS), FIVE.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), FOUR.of(ESuit.HEARTS), EIGHT.of(ESuit.SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
        Assertions.assertEquals(compare.get(1).getHandResult(), EHand.TWO_PAIR);
    }

    @Test
    public void testTwoPairTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), FIVE.of(ESuit.SPADES), FIVE.of(ESuit.HEARTS), TWO.of(ESuit.SPADES));
        Hand two = new Hand(SIX.of(ESuit.CLUBS), SIX.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), FOUR.of(ESuit.HEARTS), EIGHT.of(ESuit.SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
        Assertions.assertEquals(compare.get(1).getHandResult(), EHand.TWO_PAIR);
    }

    @Test
    public void testFullHouseTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), FIVE.of(ESuit.SPADES), FIVE.of(ESuit.HEARTS), FOUR.of(ESuit.SPADES));
        Hand two = new Hand(SIX.of(ESuit.CLUBS), SIX.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), FOUR.of(ESuit.HEARTS), SIX.of(ESuit.SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
        Assertions.assertEquals(compare.get(1).getHandResult(), EHand.FULL_HOUSE);
    }

    @Test
    public void testFullHouseHighCardTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(SIX.of(ESuit.CLUBS), SIX.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), FOUR.of(ESuit.HEARTS), SIX.of(ESuit.SPADES));
        Hand two = new Hand(SIX.of(ESuit.CLUBS), SIX.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), FOUR.of(ESuit.HEARTS), SIX.of(ESuit.SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        pureTie(compare);
    }

    @Test
    public void testTwoOfAKindTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), FIVE.of(ESuit.SPADES), SEVEN.of(ESuit.HEARTS), TWO.of(ESuit.SPADES));
        Hand two = new Hand(FIVE.of(ESuit.CLUBS), FIVE.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), TWO.of(ESuit.HEARTS), EIGHT.of(ESuit.SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
        Assertions.assertEquals(compare.get(1).getHandResult(), EHand.ONE_PAIR);
    }

    @Test
    public void testTwoOfAKindHighCardTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), FIVE.of(ESuit.SPADES), SEVEN.of(ESuit.HEARTS), TWO.of(ESuit.SPADES));
        Hand two = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), TWO.of(ESuit.HEARTS), EIGHT.of(ESuit.SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(1).getHand(), one);
        Assertions.assertEquals(compare.get(1).getHandResult(), EHand.ONE_PAIR);
    }

    @Test
    public void testThreeOfAKindHighCardTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), SEVEN.of(ESuit.HEARTS), TWO.of(ESuit.SPADES));
        Hand two = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), FOUR.of(ESuit.SPADES), TWO.of(ESuit.HEARTS), EIGHT.of(ESuit.SPADES));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(compare.get(0).getHand(), two);
        Assertions.assertEquals(compare.get(0).getHandResult(), EHand.THREE_OF_KIND);
    }

    @Test
    public void testStraightTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), FIVE.of(ESuit.DIAMONDS), SIX.of(ESuit.SPADES), SEVEN.of(ESuit.HEARTS), EIGHT.of(ESuit.SPADES));
        Hand two = new Hand(FIVE.of(ESuit.DIAMONDS), SIX.of(ESuit.SPADES), SEVEN.of(ESuit.HEARTS), EIGHT.of(ESuit.SPADES), NINE.of(ESuit.CLUBS));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(one, compare.get(1).getHand());
        Assertions.assertEquals(EHand.STRAIGHT, compare.get(1).getHandResult());
    }

    @Test
    public void testStraightFlushTie() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand one = new Hand(FOUR.of(ESuit.CLUBS), FIVE.of(ESuit.CLUBS), SIX.of(ESuit.CLUBS), SEVEN.of(ESuit.CLUBS), EIGHT.of(ESuit.CLUBS));
        Hand two = new Hand(FIVE.of(ESuit.CLUBS), SIX.of(ESuit.CLUBS), SEVEN.of(ESuit.CLUBS), EIGHT.of(ESuit.CLUBS), NINE.of(ESuit.CLUBS));

        handArrayList.add(one);
        handArrayList.add(two);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(one, compare.get(1).getHand());
        Assertions.assertEquals(EHand.STRAIGHT_FLUSH, compare.get(1).getHandResult());
    }


}
