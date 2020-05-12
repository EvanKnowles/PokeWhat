package za.co.knowles.pokewhat;

import za.co.knowles.pokewhat.domain.Card;
import za.co.knowles.pokewhat.domain.Hand;
import za.co.knowles.pokewhat.domain.HandResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HandGeneration {
    private static final int HAND_CARDS = 5;

    public static Hand getBest(List<Card> commonCards, List<Card> playerCards) {
        ArrayList<Card> cards = new ArrayList<>(commonCards);
        cards.addAll(playerCards);

        List<Hand> generate = generate(cards);
        List<HandResult> compare = HandComparator.compare(generate);

        List<HandResult> topRankedHands = compare.stream().filter(c -> c.getHandResult() == compare.get(0).getHandResult()).collect(Collectors.toList());
        if (topRankedHands.size() > 1) {
            topRankedHands = topRankedHands.stream().filter(HandResult::isTieBreakWinner).collect(Collectors.toList());
        }

        return topRankedHands.get(0).getHand();
    }

    private static List<Hand> generate(List<Card> cards) {
        List<int[]> combinations = generate(cards.size(), HAND_CARDS);

        ArrayList<Hand> hands = new ArrayList<>();
        for (int[] combination : combinations) {
            Hand hand = new Hand();
            hands.add(hand);
            for (int cardIndex : combination) {
                hand.add(cards.get(cardIndex));
            }
        }

        return hands;
    }

    private static void helper(List<int[]> combinations, int[] data, int start, int end, int index) {
        if (index == data.length) {
            int[] combination = data.clone();
            combinations.add(combination);
        } else if (start <= end) {
            data[index] = start;
            helper(combinations, data, start + 1, end, index + 1);
            helper(combinations, data, start + 1, end, index);
        }
    }

    private static List<int[]> generate(int totalItems, int numberOfItems) {
        List<int[]> combinations = new ArrayList<>();
        helper(combinations, new int[numberOfItems], 0, totalItems - 1, 0);
        return combinations;
    }

}
