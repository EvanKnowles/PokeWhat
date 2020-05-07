package za.co.knonchalant.pokewhat.event;

import za.co.knonchalant.pokewhat.domain.Game;

public class PreFlopEvent implements IGameEventHandler {
    @Override
    public void event(Game game) {
        game.dealCards();
    }
}
