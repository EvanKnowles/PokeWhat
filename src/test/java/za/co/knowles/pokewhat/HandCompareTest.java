package za.co.knowles.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knowles.pokewhat.domain.Hand;
import za.co.knowles.pokewhat.domain.HandResult;
import za.co.knowles.pokewhat.domain.lookup.ESuit;

import java.util.ArrayList;
import java.util.List;

import static za.co.knowles.pokewhat.domain.lookup.ERank.*;

public class HandCompareTest {

    @Test
    public void testTwo() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand highCard = new Hand(FOUR.of(ESuit.CLUBS), FIVE.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), THREE.of(ESuit.HEARTS), NINE.of(ESuit.SPADES));
        Hand pair = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), THREE.of(ESuit.HEARTS), NINE.of(ESuit.SPADES));

        handArrayList.add(pair);
        handArrayList.add(highCard);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(pair, compare.get(0).getHand());
    }

    @Test
    public void testMore() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand highCard = new Hand(FOUR.of(ESuit.CLUBS), FIVE.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), THREE.of(ESuit.HEARTS), NINE.of(ESuit.SPADES));
        Hand pair = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), THREE.of(ESuit.HEARTS), NINE.of(ESuit.SPADES));
        Hand twoPair = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), TWO.of(ESuit.HEARTS), NINE.of(ESuit.SPADES));
        Hand fullHouse = new Hand(FOUR.of(ESuit.CLUBS), FOUR.of(ESuit.DIAMONDS), TWO.of(ESuit.SPADES), TWO.of(ESuit.HEARTS), TWO.of(ESuit.SPADES));

        handArrayList.add(highCard);
        handArrayList.add(pair);
        handArrayList.add(twoPair);
        handArrayList.add(fullHouse);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(fullHouse, compare.get(0).getHand());
    }

}
