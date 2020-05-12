package za.co.knowles.pokewhat;

import za.co.knowles.pokewhat.domain.Hand;
import za.co.knowles.pokewhat.domain.lookup.EHand;
import za.co.knowles.pokewhat.domain.lookup.ERank;
import za.co.knowles.pokewhat.domain.lookup.ESuit;

import java.util.Map;
import java.util.Set;

public class HandResolver {
    private static final Long STRAIGHT_ACE_LOW = 0x403cL;
    private static final Long STRAIGHT_BIT_NORMALIZED = 31L;
    private static final Long ROYAL_FLUSH = 0x7c00L;

    public static EHand getHand(Hand hand) {
        if (!hand.isComplete()) {
            return EHand.FOLD;
        }

        long bitField = hand.toBitField();

        Set<ESuit> suitSet = hand.getSuitSet();

        Map<ERank, Long> rankCount = hand.getRankCount();

        int arrayIndex = rankCount.values()
                .stream()
                .mapToInt(countOfRank -> (int) (Math.pow(2, countOfRank) - 1)).sum();

        if (isStraight(bitField)) {
            arrayIndex -= 2;
        }

        arrayIndex = (arrayIndex - 1) % 15; // array index bumped to 0

        if (isFlush(suitSet)) {
            if (bitField == ROYAL_FLUSH) {
                arrayIndex += 5;
            } else {
                arrayIndex -= 1;
            }
        }

        return EHand.values()[arrayIndex];
    }

    private static boolean isFlush(Set<ESuit> suitSet) {
        return suitSet.size() == 1;
    }

    private static boolean isStraight(long suitBitField) {

        return (normalizeBitField(suitBitField) == STRAIGHT_BIT_NORMALIZED)
                || (suitBitField == STRAIGHT_ACE_LOW);
    }

    private static long normalizeBitField(long suitBitField) {
        return suitBitField / (getLSB(suitBitField));
    }

    private static long getLSB(long value) {
        return value & -value;
    }
}
