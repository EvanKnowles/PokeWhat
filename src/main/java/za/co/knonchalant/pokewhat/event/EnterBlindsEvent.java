package za.co.knonchalant.pokewhat.event;

import za.co.knonchalant.pokewhat.domain.Game;

public class EnterBlindsEvent implements IGameEventHandler {
    @Override
    public void event(Game game) {
        game.setupBlinds();
    }
}
