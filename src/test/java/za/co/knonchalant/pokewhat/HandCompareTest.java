package za.co.knonchalant.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knonchalant.pokewhat.domain.Hand;
import za.co.knonchalant.pokewhat.domain.HandResult;

import java.util.ArrayList;
import java.util.List;

import static za.co.knonchalant.pokewhat.domain.ERank.*;
import static za.co.knonchalant.pokewhat.domain.ESuit.*;

public class HandCompareTest {

    @Test
    public void testTwo() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand highCard = new Hand(FOUR.of(CLUBS), FIVE.of(DIAMONDS), TWO.of(SPADES), THREE.of(HEARTS), NINE.of(SPADES));
        Hand pair = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), TWO.of(SPADES), THREE.of(HEARTS), NINE.of(SPADES));

        handArrayList.add(pair);
        handArrayList.add(highCard);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(pair, compare.get(0).getHand());
    }

    @Test
    public void testMore() {
        List<Hand> handArrayList = new ArrayList<>();

        Hand highCard = new Hand(FOUR.of(CLUBS), FIVE.of(DIAMONDS), TWO.of(SPADES), THREE.of(HEARTS), NINE.of(SPADES));
        Hand pair = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), TWO.of(SPADES), THREE.of(HEARTS), NINE.of(SPADES));
        Hand twoPair = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), TWO.of(SPADES), TWO.of(HEARTS), NINE.of(SPADES));
        Hand fullHouse = new Hand(FOUR.of(CLUBS), FOUR.of(DIAMONDS), TWO.of(SPADES), TWO.of(HEARTS), TWO.of(SPADES));

        handArrayList.add(highCard);
        handArrayList.add(pair);
        handArrayList.add(twoPair);
        handArrayList.add(fullHouse);

        List<HandResult> compare = HandComparator.compare(handArrayList);

        Assertions.assertEquals(fullHouse, compare.get(0).getHand());
    }

}
