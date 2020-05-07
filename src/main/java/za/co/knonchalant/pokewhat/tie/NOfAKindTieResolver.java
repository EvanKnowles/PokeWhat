package za.co.knonchalant.pokewhat.tie;

import za.co.knonchalant.pokewhat.domain.lookup.ERank;
import za.co.knonchalant.pokewhat.domain.Hand;
import za.co.knonchalant.pokewhat.domain.HandResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NOfAKindTieResolver implements ITieResolver {

    private final int numberOfCards;

    public NOfAKindTieResolver(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    @Override
    public List<HandResult> resolve(List<HandResult> inputs) {
        inputs.sort(Comparator.comparingInt(value -> -getRankOfMatchingCards(value.getHand()).getNumeric()));

        ERank rankOfMatchingCards = getRankOfMatchingCards(inputs.get(0).getHand());

        List<HandResult> topMatchingResults = inputs.stream()
                .filter(r -> getRankOfMatchingCards(r.getHand()) == rankOfMatchingCards)
                .collect(Collectors.toList());

        // if we have more than one hand with the same rank four of a kind, check high card
        if (topMatchingResults.size() > 1) {
            List<HandResult> highCardResolve = new HighCardTieResolver(rankOfMatchingCards).resolve(topMatchingResults);
            inputs.removeAll(highCardResolve);
            inputs.addAll(0, highCardResolve);
            return inputs;
        }

        inputs.remove(topMatchingResults.get(0));
        inputs.add(0, topMatchingResults.get(0));

        return inputs;
    }

    private ERank getRankOfMatchingCards(Hand hand) {
        for (Map.Entry<ERank, Long> eRankLongEntry : hand.getRankCount().entrySet()) {
            if (eRankLongEntry.getValue() == numberOfCards) {
                return eRankLongEntry.getKey();
            }
        }

        throw new RuntimeException("Trying to resolve a four of a kind tie, but can't find them.");
    }
}
