package za.co.knowles.pokewhat.domain;

import java.util.Map;

public class GameResult {
    private Map<Player, HandResult> winners;
    private String description;
    private double amount;

    public GameResult(Map<Player, HandResult> winningPlayers, String description, double amount) {
        this.winners = winningPlayers;
        this.description = description;
        this.amount = amount;
    }

    public Map<Player, HandResult> getWinners() {
        return winners;
    }

    public String getDescription() {
        return description;
    }

    public double getAmountPerPlayer() {
        return amount / winners.size();
    }
}
