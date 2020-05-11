package za.co.knonchalant.pokewhat.domain;

import za.co.knonchalant.pokewhat.domain.lookup.EHand;

public class HandResult {
    private Hand hand;
    private EHand handResult;

    private boolean tieBreakWinner;

    public HandResult(Hand hand, EHand handResult) {
        this.hand = hand;
        this.handResult = handResult;
    }

    public Hand getHand() {
        return hand;
    }


    public EHand getHandResult() {
        return handResult;
    }

    public boolean isTieBreakWinner() {
        return tieBreakWinner;
    }

    public void setTieBreakWinner(boolean tieBreakWinner) {
        this.tieBreakWinner = tieBreakWinner;
    }

    @Override
    public String toString() {
        return handResult.getName() + ": " + hand;
    }
}
