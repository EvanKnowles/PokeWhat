package za.co.knonchalant.pokewhat.domain;

import za.co.knonchalant.pokewhat.domain.lookup.EBetResult;
import za.co.knonchalant.pokewhat.exceptions.ShouldNotHaveHappenedException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RoundBets {
    private final Map<Player, Double> bets;
    private final Map<Player, EBetResult> results = new HashMap<>();
    private final List<Player> allInPlayers = new ArrayList<>();
    private final List<Player> activePlayers;
    private final List<Player> foldedPlayers = new ArrayList<>();

    private final String potName;
    private double currentBet;
    private double totalBets;
    private boolean allIn;
    private int currentPlayerIndex;

    public RoundBets(double blind, Player big, Player little, List<Player> activePlayers) {
        this.potName = "Main";
        this.bets = new HashMap<>();

        this.activePlayers = activePlayers;
        for (Player activePlayer : activePlayers) {
            results.put(activePlayer, EBetResult.NOT_BETTED);
        }

        double bigBlind = 2 * blind;

        playerBet(big, bigBlind);
        playerBet(little, blind);

        totalBets = sumOfCurrentBets();

        this.currentBet = bigBlind;
        this.currentPlayerIndex = activePlayers.indexOf(little);
    }

    /**
     * Constructor used when resolving all in bets
     *
     * @param newBets the list of new bets taken from breaking an existing betting round down into smaller all-in chunks
     * @param potName the description for the results
     */
    public RoundBets(Map<Player, Double> newBets, String potName) {
        this.potName = potName;
        this.activePlayers = new ArrayList<>(newBets.keySet());
        this.bets = newBets;
        this.totalBets = sumOfCurrentBets();
    }

    public RoundBets(List<Player> activeAfterRound, Player firstPlayer) {
        this.potName = "Side";
        this.bets = new HashMap<>();

        this.activePlayers = activeAfterRound;
        this.currentPlayerIndex = activeAfterRound.indexOf(firstPlayer);
        for (Player activePlayer : activePlayers) {
            results.put(activePlayer, EBetResult.NOT_BETTED);
        }
    }

    private double sumOfCurrentBets() {
        return bets.values().stream().mapToDouble(Double::doubleValue).sum();
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
            foldedPlayers.add(player);
            return EBetResult.FOLDED;
        }

        playerBet(player, bet);

        Player nextPlayer = null;
        while (nextPlayer == null || (results.get(nextPlayer) != null && results.get(nextPlayer).isOutOfGame())) {
            currentPlayerIndex = (currentPlayerIndex + 1) % activePlayers.size();
            nextPlayer = activePlayers.get(currentPlayerIndex);
        }

        if (playerTotal >= currentBet && bet == 0) {
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

        double entireBet = bet + getPlayerBet(player);
        bets.put(player, entireBet);

        totalBets += bet;
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
            if (results.get(bettingPlayer) == EBetResult.NOT_BETTED || bets.get(bettingPlayer) < currentBet) {
                return false;
            }
        }

        return true;
    }

    private List<Player> getBettingPlayers() {
        return activePlayers.stream().filter(a -> !results.get(a).isOutOfGame()).collect(Collectors.toList());
    }

    public List<Player> getInGamePlayers() {
        ArrayList<Player> players = new ArrayList<>(activePlayers);
        players.removeAll(foldedPlayers);
        return players;
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
        currentBet = 0;

        for (Player foldedPlayer : foldedPlayers) {
            results.put(foldedPlayer, EBetResult.FOLDED);
        }

        for (Player foldedPlayer : allInPlayers) {
            results.put(foldedPlayer, EBetResult.ALL_IN);
        }

        for (Player bettingPlayer : bettingPlayers) {
            results.put(bettingPlayer, EBetResult.NOT_BETTED);
        }

        if (nextPlayer != null) {
            currentPlayerIndex = activePlayers.indexOf(nextPlayer);
        }
    }

    public double getTotalBetPool() {
        return totalBets;
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

    public String getDescription() {
        return potName;
    }

    public List<RoundBets> normalize() {
        if (!isRoundDone() || this.allInPlayers.size() <= 1) {
            return Collections.singletonList(this);
        }

        List<RoundBets> resultingBets = new ArrayList<>();
        List<Player> activePlayers = new ArrayList<>(this.activePlayers);
        Map<Double, List<Player>> collect = this.allInPlayers.stream().collect(Collectors.groupingBy(bets::get));
        ArrayList<Double> allInBets = new ArrayList<>(collect.keySet());
        allInBets.sort(Double::compareTo);

        while (allInBets.size() > 1) {
            Map<Player, Double> newBets = new HashMap<>();
            Double current = allInBets.remove(0);
            for (Player activePlayer : activePlayers) {
                newBets.put(activePlayer, current);
            }

            resultingBets.add(new RoundBets(newBets, resultingBets.isEmpty() ? "Main" : "Side"));

            for (Player activePlayer : activePlayers) {
                if (collect.get(current).contains(activePlayer)) {
                    bets.remove(activePlayer);
                } else {
                    bets.put(activePlayer, bets.get(activePlayer) - current);
                }
            }
        }

        resultingBets.add(new RoundBets(bets, "Side"));

        return resultingBets;
    }
}