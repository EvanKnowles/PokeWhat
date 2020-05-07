package za.co.knonchalant.pokewhat.domain.lookup;

import za.co.knonchalant.pokewhat.domain.Game;
import za.co.knonchalant.pokewhat.event.EnterBlindsEvent;
import za.co.knonchalant.pokewhat.event.IGameEventHandler;
import za.co.knonchalant.pokewhat.event.PreFlopEvent;

import java.util.Arrays;
import java.util.List;

public enum EGameState {
    DONE(0), RIVER(1, DONE), TURN(1, RIVER),
    FLOP(3, TURN), PRE_FLOP(0, FLOP, new PreFlopEvent()),
    BLINDS(0, PRE_FLOP, new EnterBlindsEvent()), NOT_STARTED(0, BLINDS),
    WAITING_FOR_PLAYERS(0, NOT_STARTED);

    public static final EGameState FIRST_STATE = WAITING_FOR_PLAYERS;

    private static final List<EGameState> PRE_GAME_STATES = Arrays.asList(NOT_STARTED, WAITING_FOR_PLAYERS);
    private final int cardsToReveal;
    private final EGameState advance;

    private final IGameEventHandler gameEventHandler;

    EGameState(int cardsToReveal) {
        this.cardsToReveal = cardsToReveal;
        advance = null;
        gameEventHandler = null;
    }

    EGameState(int cardsToReveal, EGameState advance, IGameEventHandler gameEventHandler) {
        this.cardsToReveal = cardsToReveal;
        this.advance = advance;
        this.gameEventHandler = gameEventHandler;
    }

    EGameState(int cardsToReveal, EGameState advance) {
        this.cardsToReveal = cardsToReveal;
        this.advance = advance;
        this.gameEventHandler = null;
    }

    public int getCardsToReveal() {
        return cardsToReveal;
    }

    public EGameState advance() {
        return advance;
    }

    public boolean preGame() {
        return PRE_GAME_STATES.contains(this);
    }

    public void event(Game game) {
        if (gameEventHandler != null) {
            gameEventHandler.event(game);
        }
    }
}
