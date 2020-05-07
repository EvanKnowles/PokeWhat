package za.co.knonchalant.pokewhat.tie;

import za.co.knonchalant.pokewhat.domain.Card;
import za.co.knonchalant.pokewhat.domain.lookup.ERank;
import za.co.knonchalant.pokewhat.domain.HandResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class HighCardTieResolver implements ITieResolver {
    public static final int NUMBER_OF_CARDS = 5;
    private final List<ERank> ignoreList;

    public HighCardTieResolver(ERank... ignore) {
        ignoreList = new ArrayList<>(Arrays.asList(ignore));
    }

    @Override
    public List<HandResult> resolve(List<HandResult> inputs) {
        for (HandResult input : inputs) {
            input.getHand().sortCards();
        }

        for (int cardIndex = 0; cardIndex < NUMBER_OF_CARDS; cardIndex++) {
            ERank theNumber = null;
            for (int i = 0; i < inputs.size(); i++) {
                if (i == 0) {
                    theNumber = inputs.get(i).getHand().getCardRank(cardIndex);
                } else {
                    if (theNumber != inputs.get(i).getHand().getCardRank(cardIndex)) {
                        theNumber = null;
                        break;
                    }
                }
            }

            if (theNumber == null) {
                break;
            } else {
                ignoreList.add(theNumber);
            }
        }

        inputs.sort(Comparator.comparingInt(this::getRank));

        int rank = getRank(inputs.get(0));

        for (HandResult input : inputs) {
            if (getRank(input) == rank) {
                input.setTieBreakWinner(true);
            }
        }

        return inputs;
    }

    private int getRank(HandResult h) {
        List<Card> cards = h.getHand().getCards();

        for (Card card : cards) {
            ERank rank = card.getRank();
            if (!ignoreList.contains(rank)) {
                return -rank.getNumeric();
            }
        }

        return 100;
    }
}
