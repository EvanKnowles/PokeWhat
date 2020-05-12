package za.co.knowles.pokewhat.event;

import za.co.knowles.pokewhat.domain.Game;

public interface IGameEventHandler {

    void event(Game game);

}
