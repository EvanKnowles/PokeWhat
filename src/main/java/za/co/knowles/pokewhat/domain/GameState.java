package za.co.knowles.pokewhat.domain;

import za.co.knowles.pokewhat.domain.lookup.EGameState;

import java.util.List;

public class GameState {
    private final EGameState state;
    private final List<Card> currentCards;

    public GameState(EGameState gameState, List<Card> currentCards) {
        state = gameState;
        this.currentCards = currentCards;
    }

    public EGameState getState() {
        return state;
    }

    public List<Card> getCurrentCards() {
        return currentCards;
    }
}
