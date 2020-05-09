package za.co.knonchalant.pokewhat.domain;

import za.co.knonchalant.pokewhat.HandComparator;
import za.co.knonchalant.pokewhat.HandGeneration;
import za.co.knonchalant.pokewhat.domain.lookup.EBetResult;
import za.co.knonchalant.pokewhat.domain.lookup.EGameState;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private static final List<Integer> STAGES = Arrays.asList(3, 1, 1);
    private static final int MIN_PLAYERS = 2;
    private static final int IN_HAND_CARDS = 2;

    private final List<Player> players = new ArrayList<>();
    private final Map<Player, Hand> playerHands = new HashMap<>();
    private final List<RoundBets> roundBets = new ArrayList<>();

    private Hand openCards;
    private Deck deck;

    private final double blind;

    // start dealer index at -1 so the first dealer is the first joined player
    int dealerIndex = -1;

    public Game(double blind) {
        this.blind = blind;
    }

    private EGameState gameState = EGameState.WAITING_FOR_PLAYERS;

    public void addPlayer(Player player) {
        if (!gameState.preGame()) {
            return;
        }

        players.add(player);
        if (players.size() < MIN_PLAYERS) {
            gameState = EGameState.WAITING_FOR_PLAYERS;
        } else {
            gameState = EGameState.NOT_STARTED;
        }
    }

    public void dealCards() {
        if (!playerHands.isEmpty()) {
            return;
        }

        for (Player player : players) {
            Hand hand = new Hand();

            for (int i = 0; i < IN_HAND_CARDS; i++) {
                hand.add(deck.takeCard());
            }

            playerHands.put(player, hand);
        }
    }

    public GameState start() {
        if (gameState == EGameState.WAITING_FOR_PLAYERS) {
            return buildGameState();
        }

        if (gameState != EGameState.NOT_STARTED) {
            return buildGameState();
        }

        deck = new Deck();
        openCards = new Hand();
        for (int number : STAGES) {
            for (int i = 0; i < number; i++) {
                openCards.add(deck.takeCard());
            }
        }

        dealerIndex = incrementPlayerIndex(dealerIndex);

        advanceGameState();
        newBettingRound();

        return buildGameState();
    }

    public void setupBlinds() {
        if (currentBets() != null) {
            return;
        }

        int bigIndex = incrementPlayerIndex(dealerIndex);
        int smallIndex = incrementPlayerIndex(bigIndex);

        roundBets.add(new RoundBets(blind, players.get(bigIndex), players.get(smallIndex), players));
    }

    private void advanceGameState() {
        gameState = gameState.advance();
        if (gameState != null) {
            gameState.event(this);
        }
    }

    private void newBettingRound() {
        RoundBets roundBets = currentBets();

        if (roundBets == null) {
            setupBlinds();
        } else {
            if (roundBets.isRoundDone()) {
                List<Player> activeAfterRound = roundBets.getActiveAfterRound();

                Player nextPlayer = getFirstActivePlayer(activeAfterRound);

                roundBets.next(nextPlayer);
                activeAfterRound = roundBets.getActiveAfterRound();

                if (roundBets.isAllIn() && activeAfterRound.size() != 1) {
                    this.roundBets.add(new RoundBets(activeAfterRound, nextPlayer));
                }
            }
        }
    }

    private Player getFirstActivePlayer(List<Player> activeAfterRound) {
        Player nextPlayer = null;
        if (activeAfterRound.size() != 1) {
            int nextIndex = dealerIndex + 1;
            while (nextPlayer == null) {
                nextPlayer = players.get(nextIndex % players.size());
                if (!activeAfterRound.contains(nextPlayer)) {
                    nextPlayer = null;
                    nextIndex++;
                }
            }
        }
        return nextPlayer;
    }

    private int incrementPlayerIndex(int currentIndex) {
        return (currentIndex + 1) % players.size();
    }

    private GameState buildGameState() {
        return new GameState(gameState, currentCards());
    }

    private List<Card> currentCards() {
        List<Card> allCards = openCards.getCards();
        List<Card> currentCards = new ArrayList<>();

        int cardIndex = 0;
        EGameState state = EGameState.FIRST_STATE;

        do {
            state = state.advance();

            if (state != null) {
                for (int j = 0; j < state.getCardsToReveal(); j++) {
                    currentCards.add(allCards.get(cardIndex));
                    cardIndex++;
                }
            }
        } while (state != null && state != gameState);

        return currentCards;
    }

    public EBetResult bet(Player player, double betAmount) {
        RoundBets roundBets = currentBets();

        if (roundBets == null) {
            return EBetResult.OUT_OF_TURN;
        }

        if (currentRoundDone()) {
            return EBetResult.OUT_OF_TURN;
        }

        if (!getCurrentPlayer().equals(player)) {
            return EBetResult.OUT_OF_TURN;
        }

        return roundBets.placeBet(player, betAmount);
    }

    public boolean currentRoundDone() {
        RoundBets roundBets = currentBets();

        if (roundBets == null) {
            return false;
        }

        return roundBets.isRoundDone();
    }

    private RoundBets currentBets() {
        if (roundBets.isEmpty()) {
            return null;
        }

        return roundBets.get(roundBets.size() - 1);
    }

    public EGameState getState() {
        return gameState;
    }

    public Player getDealer() {
        return players.get(dealerIndex);
    }

    public Player getCurrentPlayer() {
        return currentBets().getCurrentPlayer();
    }

    public int getPlayerCount() {
        return players.size();
    }

    public GameState nextRound() {
        RoundBets roundBets = currentBets();

        if (roundBets == null || !roundBets.isRoundDone()) {
            return buildGameState();
        }

        newBettingRound();
        advanceGameState();

        return buildGameState();
    }

    public Hand handFor(Player playerOne) {
        return playerHands.get(playerOne);
    }

    public GameResult getResult() {
        if (gameState != EGameState.DONE) {
            return null;
        }

        Map<Hand, Player> bestHands = new HashMap<>();
        for (Player player : players) {
            Hand best = HandGeneration.getBest(openCards.getCards(), playerHands.get(player).getCards());
            bestHands.put(best, player);
        }

        List<HandResult> compare = HandComparator.compare(bestHands.keySet());

        List<HandResult> topRankedHands = compare.stream().filter(c -> c.getHandResult() == compare.get(0).getHandResult()).collect(Collectors.toList());
        if (topRankedHands.size() > 1) {
            topRankedHands = topRankedHands.stream().filter(HandResult::isTieBreakWinner).collect(Collectors.toList());
        }

        Map<Player, HandResult> winningPlayers = new HashMap<>();
        for (HandResult topRankedHand : topRankedHands) {
            winningPlayers.put(bestHands.get(topRankedHand.getHand()), topRankedHand);
        }

        return new GameResult(winningPlayers);
    }

    public double getCurrentPool() {
        RoundBets roundBets = currentBets();

        if (roundBets == null) {
            return 0;
        }

        return roundBets.getTotalBetPool();
    }
}
