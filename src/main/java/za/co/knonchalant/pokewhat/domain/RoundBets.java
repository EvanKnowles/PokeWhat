package za.co.knonchalant.pokewhat.domain;

import za.co.knonchalant.pokewhat.domain.lookup.EBetResult;
import za.co.knonchalant.pokewhat.exceptions.ShouldNotHaveHappenedException;

import java.util.*;
import java.util.stream.Collectors;

public class RoundBets {
    private final Map<Player, Double> bets = new HashMap<>();
    private final Map<Player, EBetResult> results = new HashMap<>();
    private final List<Player> allInPlayers = new ArrayList<>();
    private final List<Player> activePlayers;

    private double currentBet;
    private boolean allIn;
    private int currentPlayerIndex;

    public RoundBets(double blind, Player big, Player little, List<Player> activePlayers) {
        this.activePlayers = activePlayers;
        for (Player activePlayer : activePlayers) {
            results.put(activePlayer, EBetResult.NOT_BETTED);
        }

        double bigBlind = 2 * blind;

        playerBet(big, bigBlind);
        playerBet(little, blind);

        this.currentBet = bigBlind;
        this.currentPlayerIndex = activePlayers.indexOf(little);
    }

    public RoundBets(List<Player> activeAfterRound, Player firstPlayer) {
        this.activePlayers = activeAfterRound;
        this.currentPlayerIndex = activeAfterRound.indexOf(firstPlayer);
        for (Player activePlayer : activePlayers) {
            results.put(activePlayer, EBetResult.NOT_BETTED);
        }
    }

    public EBetResult placeBet(Player player, double bet) {
        if (activePlayers.indexOf(player) != currentPlayerIndex) {
            return EBetResult.OUT_OF_TURN;
        }

        boolean goingAllIn = bet >= player.getMoney();
        bet = Math.min(bet, player.getMoney());

        double playerTotal = bet + getPlayerBet(player);
        if (!goingAllIn && playerTotal < currentBet) {
            results.put(player, EBetResult.FOLDED);
            return EBetResult.FOLDED;
        }

        playerBet(player, bet);

        Player nextPlayer = null;
        while (nextPlayer == null || (results.get(nextPlayer) != null && results.get(nextPlayer).isOutOfGame())) {
            currentPlayerIndex = (currentPlayerIndex + 1) % activePlayers.size();
            nextPlayer = activePlayers.get(currentPlayerIndex);
        }

        if (playerTotal == currentBet && bet == 0) {
            return playerResult(EBetResult.CHECKED, player);
        }

        if (playerTotal == currentBet) {
            return playerResult(goingAllIn ? EBetResult.ALL_IN : EBetResult.SAW, player);
        }

        if (playerTotal > currentBet) {
            currentBet = playerTotal;
            return playerResult(goingAllIn ? EBetResult.ALL_IN : EBetResult.RAISED, player);
        }

        throw new ShouldNotHaveHappenedException("Player total was unlike the bet in any way.");
    }

    private void playerBet(Player player, double bet) {
        player.loseMoney(bet);
        bets.put(player, bet + getPlayerBet(player));
    }

    private Double getPlayerBet(Player player) {
        if (!bets.containsKey(player)) {
            return 0d;
        }

        return bets.get(player);
    }

    public boolean isRoundDone() {
        if (allIn) {
            return true;
        }

        List<Player> bettingPlayers = getBettingPlayers();

        for (Player bettingPlayer : bettingPlayers) {
            if (results.get(bettingPlayer) == EBetResult.NOT_BETTED) {
                return false;
            }
        }


//        if (bettingPlayers.size() == 1) {
//            return true;
//        }
//
//        if (results.size() != bettingPlayers.size()) {
//            return false;
//        }
//
//        for (Map.Entry<Player, Double> playerDoubleEntry : bets.entrySet()) {
//            if (playerDoubleEntry.getValue() < currentBet) {
//                if (results.get(playerDoubleEntry.getKey()) != EBetResult.FOLDED) {
//                    return false;
//                }
//            }
//        }

        return true;
    }

    private List<Player> getBettingPlayers() {
        return activePlayers.stream().filter(a -> !results.get(a).isOutOfGame()).collect(Collectors.toList());
    }

    private EBetResult playerResult(EBetResult result, Player player) {
        results.put(player, result);
        return result;
    }

    public void next(Player nextPlayer) {
        if (!isRoundDone()) {
            return;
        }

        Set<Player> foldedPlayers = new HashSet<>();
        for (Player player : results.keySet()) {
            EBetResult result = results.get(player);

            if (result != EBetResult.FOLDED) {
                if (result == EBetResult.ALL_IN) {
                    allInPlayers.add(player);
                }
            } else {
                foldedPlayers.add(player);
            }
        }

        if (results.containsValue(EBetResult.ALL_IN)) {
            allIn = true;
        }

        List<Player> bettingPlayers = getBettingPlayers();

        results.clear();
        bets.clear();
        currentBet = 0;
        currentPlayerIndex = activePlayers.indexOf(nextPlayer);

        for (Player foldedPlayer : foldedPlayers) {
            results.put(foldedPlayer, EBetResult.FOLDED);
        }

        for (Player foldedPlayer : allInPlayers) {
            results.put(foldedPlayer, EBetResult.ALL_IN);
        }

        for (Player bettingPlayer : bettingPlayers) {
            results.put(bettingPlayer, EBetResult.NOT_BETTED);
        }
    }

    public double getTotalBetPool() {
        return bets.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public boolean isAllIn() {
        return allIn;
    }

    public List<Player> getActiveAfterRound() {
        return getBettingPlayers();
    }

    public Player getCurrentPlayer() {
        return activePlayers.get(currentPlayerIndex);
    }
}