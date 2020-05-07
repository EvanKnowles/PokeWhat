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

    private final double blind;

    private RoundBets roundBets;

    private Hand openCards;

    // start dealer index at -1 so the first dealer is the first joined player
    int dealerIndex = -1;
    int currentPlayerIndex;
    private Deck deck;

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
        currentPlayerIndex = incrementPlayerIndex(dealerIndex);

        advanceGameState();
        newBettingRound();

        return buildGameState();
    }

    public void setupBlinds() {
        if (roundBets != null) {
            return;
        }

        int bigIndex = incrementPlayerIndex(dealerIndex);
        int smallIndex = incrementPlayerIndex(bigIndex);

        roundBets = new RoundBets(players.size(), blind, players.get(bigIndex), players.get(smallIndex));
        currentPlayerIndex = smallIndex;
    }

    private void advanceGameState() {
        gameState = gameState.advance();
        if (gameState != null) {
            gameState.event(this);
        }
    }

    private void newBettingRound() {
        if (roundBets.isRoundDone()) {
            currentPlayerIndex = incrementPlayerIndex(dealerIndex);

            roundBets.next();
        }
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

            if(state != null) {
                for (int j = 0; j < state.getCardsToReveal(); j++) {
                    currentCards.add(allCards.get(cardIndex));
                    cardIndex++;
                }
            }
        } while (state != null && state != gameState);

        return currentCards;
    }

    public EBetResult bet(Player player, double betAmount) {
        if (currentRoundDone()) {
            return EBetResult.OUT_OF_TURN;
        }

        if (!getCurrentPlayer().equals(player)) {
            return EBetResult.OUT_OF_TURN;
        }

        currentPlayerIndex = incrementPlayerIndex(currentPlayerIndex);
        return currentBets().placeBet(player, betAmount);
    }

    public boolean currentRoundDone() {
        return currentBets().isRoundDone();
    }

    private RoundBets currentBets() {
        return roundBets;
    }

    public EGameState getState() {
        return gameState;
    }

    public Player getDealer() {
        return players.get(dealerIndex);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getPlayerCount() {
        return players.size();
    }

    public GameState nextRound() {
        if (!currentBets().isRoundDone()) {
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
}
