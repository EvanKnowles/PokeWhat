package za.co.knowles.pokewhat.domain.lookup;

import za.co.knowles.pokewhat.domain.HandResult;
import za.co.knowles.pokewhat.tie.NOfAKindTieResolver;
import za.co.knowles.pokewhat.tie.HighCardTieResolver;
import za.co.knowles.pokewhat.tie.ITieResolver;
import za.co.knowles.pokewhat.tie.CardComboTieResolver;

import java.util.List;

public enum EHand {
    FOUR_OF_A_KIND("4 of a Kind", 3, new NOfAKindTieResolver(4)),
    STRAIGHT_FLUSH("Straight Flush", 2, new HighCardTieResolver()),
    STRAIGHT("Straight", 6, new HighCardTieResolver()),
    FLUSH("Flush", 5, new HighCardTieResolver()),
    HIGH_CARD("High Card", 10, new HighCardTieResolver()),
    ONE_PAIR("1 Pair", 9, new NOfAKindTieResolver(2)),
    TWO_PAIR("2 Pair", 8, new CardComboTieResolver(2, 2)),
    ROYAL_FLUSH("Royal Flush", 1),
    THREE_OF_KIND("3 of a Kind", 7, new NOfAKindTieResolver(3)),
    FULL_HOUSE("Full House", 4, new CardComboTieResolver(3, 2)),

    FOLD("Fold", 100);

    private final String name;
    private final int rank;
    private final ITieResolver tieResolver;

    EHand(String name, int rank) {
        this(name, rank, null);
    }

    EHand(String name, int rank, ITieResolver tieResolver) {
        this.name = name;
        this.rank = rank;
        this.tieResolver = tieResolver;
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }

    public List<HandResult> sortTies(List<HandResult> ties) {
        return tieResolver.resolve(ties);
    }
}
