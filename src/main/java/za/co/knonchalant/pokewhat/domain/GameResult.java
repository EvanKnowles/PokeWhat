package za.co.knonchalant.pokewhat.domain;

import java.util.HashMap;
import java.util.Map;

public class GameResult {
    private String message;

    private Map<Player, HandResult> winners;

    public GameResult(Map<Player, HandResult> winningPlayers) {
        this.winners = winningPlayers;
    }

    public Map<Player, HandResult> getWinners() {
        return winners;
    }
}
