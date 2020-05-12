package za.co.knowles.pokewhat.event;

import za.co.knowles.pokewhat.domain.Game;

public class PreFlopEvent implements IGameEventHandler {
    @Override
    public void event(Game game) {
        game.dealCards();
    }
}
