package za.co.knonchalant.pokewhat.domain;

import za.co.knonchalant.pokewhat.domain.lookup.EBetResult;
import za.co.knonchalant.pokewhat.exceptions.ShouldNotHaveHappenedException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RoundBets {
    private final Map<Player, Double> bets = new HashMap<>();
    private final Map<Player, EBetResult> results = new HashMap<>();
    private double currentBet;
    private double totalBetPool;
    private final int totalPlayers;

    public RoundBets(int totalPlayers, double blind, Player big, Player little) {
        this.totalPlayers = totalPlayers;

        double bigBlind = 2 * blind;

        bets.put(big, bigBlind);
        bets.put(little, blind);

        this.currentBet = bigBlind;
    }

    public EBetResult placeBet(Player player, double bet) {
        bets.putIfAbsent(player, 0d);

        double playerTotal = bet + bets.get(player);
        if (playerTotal < currentBet) {
            return playerResult(EBetResult.FOLDED, player);
        }

        bets.put(player, playerTotal);

        if (playerTotal == currentBet && bet == 0) {
            return playerResult(EBetResult.CHECKED, player);
        }

        if (playerTotal == currentBet) {
            return playerResult(EBetResult.SAW, player);
        }

        if (playerTotal > currentBet) {
            currentBet = playerTotal;
            return playerResult(EBetResult.RAISED, player);
        }

        throw new ShouldNotHaveHappenedException("Player total was unlike the bet in any way.");
    }

    public boolean isRoundDone() {
        if (results.size() != totalPlayers) {
            return false;
        }

        for (Map.Entry<Player, Double> playerDoubleEntry : bets.entrySet()) {
            if (playerDoubleEntry.getValue() < currentBet) {
                if (results.get(playerDoubleEntry.getKey()) != EBetResult.FOLDED) {
                    return false;
                }
            }
        }

        return true;
    }

    private EBetResult playerResult(EBetResult result, Player player) {
        results.put(player, result);
        return result;
    }

    public void next() {
        if (!isRoundDone()) {
            return;
        }

        Set<Player> foldedPlayers = new HashSet<>();
        for (Player player : results.keySet()) {
            if (results.get(player) != EBetResult.FOLDED) {
                totalBetPool += bets.get(player);
            } else {
                foldedPlayers.add(player);
            }
        }

        results.clear();
        bets.clear();
        currentBet = 0;

        for (Player foldedPlayer : foldedPlayers) {
            results.put(foldedPlayer, EBetResult.FOLDED);
        }
    }
}