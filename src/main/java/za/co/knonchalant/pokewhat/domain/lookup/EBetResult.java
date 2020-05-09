package za.co.knonchalant.pokewhat.domain.lookup;

public enum EBetResult {
    NOT_BETTED, SAW, RAISED, FOLDED, CHECKED, OUT_OF_TURN, ALL_IN;

    public boolean isOutOfGame() {
        return this == FOLDED || this == ALL_IN;
    }
}
