package za.co.knonchalant.pokewhat;

import za.co.knonchalant.pokewhat.domain.Card;
import za.co.knonchalant.pokewhat.domain.Hand;
import za.co.knonchalant.pokewhat.domain.HandResult;

import java.util.ArrayList;
import java.util.List;

public class HandGeneration {
    private static final int HAND_CARDS = 5;

    public static Hand getBest(List<Card> commonCards, List<Card> playerCards) {
        ArrayList<Card> cards = new ArrayList<>(commonCards);
        cards.addAll(playerCards);

        List<Hand> generate = generate(cards);
        List<HandResult> compare = HandComparator.compare(generate);
        return compare.get(0).getHand();
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
