package za.co.knonchalant.pokewhat.tie;

import za.co.knonchalant.pokewhat.domain.lookup.ERank;
import za.co.knonchalant.pokewhat.domain.Hand;
import za.co.knonchalant.pokewhat.domain.HandResult;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CardComboTieResolver implements ITieResolver {

    private final int firstGroupOfCards;
    private final int secondGroupOfCards;

    public CardComboTieResolver(int firstGroupOfCards, int secondGroupOfCards) {
        this.firstGroupOfCards = firstGroupOfCards;
        this.secondGroupOfCards = secondGroupOfCards;
    }

    @Override
    public List<HandResult> resolve(List<HandResult> inputs) {
        inputs.sort(Comparator.comparingInt(value -> -getHighestRankOfMatchingCards(value.getHand(), firstGroupOfCards).getNumeric()));

        ERank rankOfMatchingCards = getHighestRankOfMatchingCards(inputs.get(0).getHand(), firstGroupOfCards);

        List<HandResult> topMatchingResults = inputs.stream()
                .filter(r -> getHighestRankOfMatchingCards(r.getHand(), firstGroupOfCards) == rankOfMatchingCards)
                .collect(Collectors.toList());

        // if we have more than one hand with the same rank higher of two pair, check the next pair card
        if (topMatchingResults.size() > 1) {
            ERank rankOfSecondMatchingCards = getHighestRankOfMatchingCards(inputs.get(0).getHand(), secondGroupOfCards, rankOfMatchingCards);

            topMatchingResults = inputs.stream()
                    .filter(r -> getHighestRankOfMatchingCards(r.getHand(), secondGroupOfCards, rankOfMatchingCards) == rankOfSecondMatchingCards)
                    .collect(Collectors.toList());

            if (topMatchingResults.size() > 1) {
                List<HandResult> highCardResolve = new HighCardTieResolver(rankOfMatchingCards).resolve(topMatchingResults);
                inputs.removeAll(highCardResolve);
                inputs.addAll(0, highCardResolve);

                return inputs;
            }
        }

        inputs.remove(topMatchingResults.get(0));
        inputs.add(0, topMatchingResults.get(0));

        return inputs;
    }

    private ERank getHighestRankOfMatchingCards(Hand hand, int numberOfCards, ERank... ignore) {
        ERank rank = ERank.TWO;
        List<ERank> ignoredRanks = Arrays.asList(ignore);

        for (Map.Entry<ERank, Long> ranksByCount : hand.getRankCount().entrySet()) {
            if (ranksByCount.getValue() == numberOfCards && ranksByCount.getKey().getNumeric() > rank.getNumeric()) {
                if (!ignoredRanks.contains(ranksByCount.getKey())) {
                    rank = ranksByCount.getKey();
                }
            }
        }

        return rank;
    }
}
