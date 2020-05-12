package za.co.knowles.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knowles.pokewhat.domain.*;
import za.co.knowles.pokewhat.domain.lookup.EBetResult;
import za.co.knowles.pokewhat.domain.lookup.EGameState;
import za.co.knowles.pokewhat.domain.lookup.ESuit;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static za.co.knowles.pokewhat.domain.lookup.ERank.*;

public class GameMoneyTests {

    @Test
    public void testTwoPlayersAllIn() {
        Player playerOne = new Player("Player One", 20);
        Player playerTwo = new Player("Player Two", 30);

        Game game = new Game(1d);
        game.addPlayer(playerOne);
        game.addPlayer(playerTwo);

        game.start();
        assertEquals(3d, game.getCurrentPool());

        // start bets for blinds
        assertEquals(EBetResult.ALL_IN, game.bet(playerOne, 22));
        assertEquals(0, playerOne.getMoney());

        assertEquals(EBetResult.SAW, game.bet(playerTwo, 18));
        assertEquals(10, playerTwo.getMoney());

        assertEquals(40d, game.getCurrentPool());

        Assertions.assertTrue(game.currentRoundDone());

        while (game.nextRound().getState() != EGameState.DONE) {
            Assertions.assertTrue(game.currentRoundDone());
        }

        List<GameResult> results = game.getResult();
        for (GameResult result : results) {
            System.out.println("Winners:");
            System.out.println(result.getWinners());
        }
    }

    @Test
    public void testTwoPlayersAllInFold() {
        Player playerOne = new Player("Player One", 20);
        Player playerTwo = new Player("Player Two", 30);

        Game game = new Game(1d);
        game.addPlayer(playerOne);
        game.addPlayer(playerTwo);

        game.start();
        assertEquals(3d, game.getCurrentPool());

        // start bets for blinds
        assertEquals(EBetResult.ALL_IN, game.bet(playerOne, 22));
        assertEquals(0, playerOne.getMoney());

        assertEquals(EBetResult.FOLDED, game.bet(playerTwo, 0));
        assertEquals(28, playerTwo.getMoney());

        assertEquals(22d, game.getCurrentPool());

        Assertions.assertTrue(game.currentRoundDone());
        GameState gameState = game.nextRound();

        Assertions.assertSame(EGameState.DONE, gameState.getState());

        List<GameResult> results = game.getResult();
        printResults(results);
    }

    @Test
    public void testTwoPlayersFold() {
        Player playerOne = new Player("Player One", 20);
        Player playerTwo = new Player("Player Two", 30);

        Game game = new Game(1d);
        game.addPlayer(playerOne);
        game.addPlayer(playerTwo);

        game.start();
        assertEquals(3d, game.getCurrentPool());

        // start bets for blinds
        assertEquals(EBetResult.RAISED, game.bet(playerOne, 3));
        assertEquals(16, playerOne.getMoney());

        assertEquals(EBetResult.FOLDED, game.bet(playerTwo, 0));
        assertEquals(28, playerTwo.getMoney());

        assertEquals(6d, game.getCurrentPool());

        Assertions.assertTrue(game.currentRoundDone());
        GameState gameState = game.nextRound();

        Assertions.assertSame(EGameState.DONE, gameState.getState());

        List<GameResult> results = game.getResult();
        Assertions.assertEquals(playerOne, getWinner(results, 0));

        printResults(results);
    }

    @Test
    public void testTwoPlayersFoldImmediately() {
        Player playerOne = new Player("Player One", 20);
        Player playerTwo = new Player("Player Two", 30);

        Game game = new Game(1d);
        game.addPlayer(playerOne);
        game.addPlayer(playerTwo);

        game.start();
        assertEquals(3d, game.getCurrentPool());

        // start bets for blinds
        assertEquals(EBetResult.FOLDED, game.bet(playerOne, 0));
        assertEquals(19, playerOne.getMoney());
        Assertions.assertTrue(game.currentRoundDone());

        assertEquals(EBetResult.OUT_OF_TURN, game.bet(playerTwo, 0));
        assertEquals(28, playerTwo.getMoney());

        assertEquals(3d, game.getCurrentPool());

        Assertions.assertTrue(game.currentRoundDone());
        GameState gameState = game.nextRound();

        Assertions.assertSame(EGameState.DONE, gameState.getState());

        List<GameResult> results = game.getResult();
        Assertions.assertEquals(playerTwo, getWinner(results, 0));

        printResults(results);
    }

    private void printResults(List<GameResult> results) {
        for (GameResult result : results) {
            System.out.println("Winners of " + result.getDescription() + " pot:");
            System.out.println(result.getWinners());
            System.out.println("Winning: " + result.getAmountPerPlayer());
        }
    }

    @Test
    public void testThreePlayersAllIn() {
        Player playerOne = new Player("Player One", 30);
        Player playerTwo = new Player("Player Two", 40);
        Player playerThree = new Player("Player Three", 20);

        Game game = new Game(1d);
        game.addPlayer(playerOne);
        game.addPlayer(playerTwo);
        game.addPlayer(playerThree);

        game.start();
        assertEquals(3d, game.getCurrentPool());

        // start bets for blinds
        assertEquals(EBetResult.ALL_IN, game.bet(playerThree, 22));
        assertEquals(0, playerThree.getMoney());

        assertEquals(EBetResult.SAW, game.bet(playerOne, 20));
        assertEquals(10, playerOne.getMoney());

        assertEquals(EBetResult.SAW, game.bet(playerTwo, 18));
        assertEquals(20, playerTwo.getMoney());

        assertEquals(60d, game.getCurrentPool());

        Assertions.assertTrue(game.currentRoundDone());
        game.nextRound();
        Assertions.assertFalse(game.currentRoundDone());

        // it would have been player three next, but they're all in
        Player currentPlayer = game.getCurrentPlayer();
        Assertions.assertNotSame(currentPlayer, playerThree);

        // now to resolve the side betting between player one and two
        // player two can bet all player one's money
        assertEquals(EBetResult.RAISED, game.bet(playerTwo, 10));
        assertEquals(10, playerTwo.getMoney());

        assertEquals(EBetResult.ALL_IN, game.bet(playerOne, 22));
        assertEquals(0, playerOne.getMoney());

        while (game.nextRound().getState() != EGameState.DONE) {
            Assertions.assertTrue(game.currentRoundDone());
        }

        List<GameResult> results = game.getResult();
        printResults(results);
    }

    @Test
    public void testThreePlayersTwoAllIn() {
        Player playerOne = new Player("Player One", 30);
        Player playerTwo = new Player("Player Two", 40);
        Player playerThree = new Player("Player Three", 20);

        List<Card> cards = new ArrayList<>();

        // open cards
        cards.add(THREE.of(ESuit.SPADES));
        cards.add(FIVE.of(ESuit.SPADES));
        cards.add(SIX.of(ESuit.DIAMONDS));
        cards.add(SEVEN.of(ESuit.SPADES));
        cards.add(EIGHT.of(ESuit.DIAMONDS));

        // player one - pair of Aces
        cards.add(ACE.of(ESuit.CLUBS));
        cards.add(ACE.of(ESuit.SPADES));

        // player two - pair of sevens
        cards.add(SEVEN.of(ESuit.HEARTS));
        cards.add(TWO.of(ESuit.CLUBS));

        // player three - straight
        cards.add(FOUR.of(ESuit.SPADES));
        cards.add(ACE.of(ESuit.DIAMONDS));

        Game game = new Game(1d, new Deck(cards));

        game.addPlayer(playerOne);
        game.addPlayer(playerTwo);
        game.addPlayer(playerThree);

        game.start();
        assertEquals(3d, game.getCurrentPool());

        // start bets for blinds
        assertEquals(EBetResult.ALL_IN, game.bet(playerThree, 22));
        assertEquals(0, playerThree.getMoney());

        assertEquals(EBetResult.ALL_IN, game.bet(playerOne, 30));
        assertEquals(0, playerOne.getMoney());

        assertEquals(EBetResult.SAW, game.bet(playerTwo, 28));
        assertEquals(10, playerTwo.getMoney());

        assertEquals(80d, game.getCurrentPool());

        Assertions.assertTrue(game.currentRoundDone());

        while (game.nextRound().getState() != EGameState.DONE) {
            Assertions.assertTrue(game.currentRoundDone());
        }

        List<GameResult> results = game.getResult();
        assertEquals(2, results.size());

        assertEquals(playerThree, getWinner(results, 0));
        assertEquals(playerOne, getWinner(results, 1));

        printResults(results);
    }

    @Test
    public void testFourPlayersTwoAllIn() {
        Player playerOne = new Player("Player One", 30);
        Player playerTwo = new Player("Player Two", 40);
        Player playerThree = new Player("Player Three", 20);
        Player playerFour = new Player("Player Four", 40);

        List<Card> cards = new ArrayList<>();

        // open cards
        cards.add(THREE.of(ESuit.SPADES));
        cards.add(FIVE.of(ESuit.SPADES));
        cards.add(SIX.of(ESuit.DIAMONDS));
        cards.add(SEVEN.of(ESuit.SPADES));
        cards.add(EIGHT.of(ESuit.DIAMONDS));

        // player one - middle straight
        cards.add(NINE.of(ESuit.CLUBS));
        cards.add(ACE.of(ESuit.SPADES));

        // player two - pair of sevens
        cards.add(SEVEN.of(ESuit.HEARTS));
        cards.add(TWO.of(ESuit.CLUBS));

        // player three - highest straight
        cards.add(NINE.of(ESuit.SPADES));
        cards.add(TEN.of(ESuit.DIAMONDS));

        // player four -  lowest straight
        cards.add(FOUR.of(ESuit.SPADES));
        cards.add(ACE.of(ESuit.DIAMONDS));

        Game game = new Game(1d, new Deck(cards));

        game.addPlayer(playerOne);
        game.addPlayer(playerTwo);
        game.addPlayer(playerThree);
        game.addPlayer(playerFour);

        game.start();
        assertEquals(3d, game.getCurrentPool());

        // start bets for blinds
        assertEquals(EBetResult.ALL_IN, game.bet(playerThree, 22));
        assertEquals(0, playerThree.getMoney());

        assertEquals(EBetResult.SAW, game.bet(playerFour, 20));
        assertEquals(20, playerFour.getMoney());

        assertEquals(EBetResult.ALL_IN, game.bet(playerOne, 30));
        assertEquals(0, playerOne.getMoney());

        assertEquals(EBetResult.SAW, game.bet(playerTwo, 28));
        assertEquals(10, playerTwo.getMoney());

        assertEquals(EBetResult.SAW, game.bet(playerFour, 10));
        assertEquals(10, playerFour.getMoney());


        Assertions.assertTrue(game.currentRoundDone());
        assertEquals(110d, game.getCurrentPool());

        game.nextRound();
        Assertions.assertFalse(game.currentRoundDone());

        assertEquals(EBetResult.ALL_IN, game.bet(playerTwo, 10));
        assertEquals(0, playerTwo.getMoney());

        assertEquals(EBetResult.ALL_IN, game.bet(playerFour, 10));
        assertEquals(0, playerFour.getMoney());
        Assertions.assertTrue(game.currentRoundDone());
        assertEquals(20d, game.getCurrentPool());

        while (game.nextRound().getState() != EGameState.DONE) {
            Assertions.assertTrue(game.currentRoundDone());
        }

        List<GameResult> results = game.getResult();
        assertEquals(3, results.size());

        assertEquals(playerThree, getWinner(results, 0));
        assertEquals(playerOne, getWinner(results, 1));
        assertEquals(playerFour, getWinner(results, 2));

        printResults(results);
    }

    @Test
    public void testThreePlayersAllInFold() {
        Player playerOne = new Player("Player One", 30);
        Player playerTwo = new Player("Player Two", 40);
        Player playerThree = new Player("Player Three", 20);

        List<Card> cards = new ArrayList<>();

        // open cards
        cards.add(THREE.of(ESuit.SPADES));
        cards.add(FIVE.of(ESuit.SPADES));
        cards.add(SIX.of(ESuit.DIAMONDS));
        cards.add(SEVEN.of(ESuit.SPADES));
        cards.add(EIGHT.of(ESuit.DIAMONDS));

        // player one
        cards.add(FOUR.of(ESuit.SPADES));
        cards.add(ACE.of(ESuit.DIAMONDS));

        // player two
        cards.add(SEVEN.of(ESuit.HEARTS));
        cards.add(TWO.of(ESuit.CLUBS));

        // player three
        cards.add(ACE.of(ESuit.CLUBS));
        cards.add(ACE.of(ESuit.SPADES));

        Game game = new Game(1d, new Deck(cards));
        game.addPlayer(playerOne);
        game.addPlayer(playerTwo);
        game.addPlayer(playerThree);

        game.start();
        assertEquals(3d, game.getCurrentPool());

        // start bets for blinds
        assertEquals(EBetResult.ALL_IN, game.bet(playerThree, 22));
        assertEquals(0, playerThree.getMoney());

        assertEquals(EBetResult.SAW, game.bet(playerOne, 20));
        assertEquals(10, playerOne.getMoney());

        assertEquals(EBetResult.SAW, game.bet(playerTwo, 18));
        assertEquals(20, playerTwo.getMoney());

        assertEquals(60d, game.getCurrentPool());

        Assertions.assertTrue(game.currentRoundDone());
        game.nextRound();
        Assertions.assertFalse(game.currentRoundDone());

        // it would have been player three next, but they're all in
        Player currentPlayer = game.getCurrentPlayer();
        Assertions.assertNotSame(currentPlayer, playerThree);

        // now to resolve the side betting between player one and two
        // player two can bet all player one's money
        assertEquals(EBetResult.RAISED, game.bet(playerTwo, 10));
        assertEquals(10, playerTwo.getMoney());

        assertEquals(EBetResult.FOLDED, game.bet(playerOne, 0));
        assertEquals(10, playerOne.getMoney());

        while (game.nextRound().getState() != EGameState.DONE) {
            Assertions.assertTrue(game.currentRoundDone());
        }

        List<GameResult> results = game.getResult();

        // player one folded, can't win anything.
        for (GameResult result : results) {
            for (Player player : result.getWinners().keySet()) {
                Assertions.assertNotEquals(player, playerOne);
            }
        }

        assertEquals(playerThree, getWinner(results, 0));
        assertEquals(playerTwo, getWinner(results, 1));

        printResults(results);
    }

    private Player getWinner(List<GameResult> results, int i) {
        return results.get(i).getWinners().keySet().iterator().next();
    }
}
