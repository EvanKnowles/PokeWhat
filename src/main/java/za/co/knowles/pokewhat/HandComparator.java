package za.co.knowles.pokewhat;

import za.co.knowles.pokewhat.domain.lookup.EHand;
import za.co.knowles.pokewhat.domain.Hand;
import za.co.knowles.pokewhat.domain.HandResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HandComparator {
    public static List<HandResult> compare(Collection<Hand> hands) {
        ArrayList<HandResult> handResults = new ArrayList<>();

        for (Hand hand : hands) {
            handResults.add(new HandResult(hand, HandResolver.getHand(hand)));
        }

        handResults.sort(Comparator.comparingInt(o -> o.getHandResult().getRank()));

        checkForTies(handResults);

        return handResults;
    }

    private static void checkForTies(ArrayList<HandResult> handResults) {
        int best = handResults.get(0).getHandResult().getRank();

        List<HandResult> ties = handResults.stream().filter(r -> r.getHandResult().getRank() == best).collect(Collectors.toList());
        if (ties.size() > 1) {
            handResults.removeAll(ties);
            handResults.addAll(0, breakTies(ties));
        }
    }

    private static List<HandResult> breakTies(List<HandResult> ties) {
        EHand theHand = ties.get(0).getHandResult();

        List<HandResult> handResults = theHand.sortTies(ties);
        handResults.get(0).setTieBreakWinner(true);

        return handResults;
    }
}
