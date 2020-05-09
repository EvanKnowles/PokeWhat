package za.co.knonchalant.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knonchalant.pokewhat.domain.*;
import za.co.knonchalant.pokewhat.domain.lookup.EBetResult;
import za.co.knonchalant.pokewhat.domain.lookup.EGameState;

import java.util.List;

public class FullGameTest {

    @Test
    public void testGame() {
        Game game = new Game(1d);
        Player playerOne = new Player("Player One", 20);
        Player playerTwo = new Player("Player Two", 20);

        Assertions.assertEquals(EGameState.WAITING_FOR_PLAYERS, game.getState());

        game.addPlayer(playerOne);
        Assertions.assertEquals(EGameState.WAITING_FOR_PLAYERS, game.getState());
        Assertions.assertEquals(1, game.getPlayerCount());

        game.addPlayer(playerTwo);
        Assertions.assertEquals(2, game.getPlayerCount());
        Assertions.assertEquals(EGameState.NOT_STARTED, game.getState());

        GameState gameState = game.start();
        Assertions.assertEquals(EGameState.BLINDS, gameState.getState());
        Assertions.assertEquals(0, gameState.getCurrentCards().size());
        Assertions.assertEquals(19, playerOne.getMoney());
        Assertions.assertEquals(18, playerTwo.getMoney());

        Assertions.assertEquals(playerOne, game.getDealer());
        Assertions.assertEquals(playerOne, game.getCurrentPlayer());

        // no adding players after start
        game.addPlayer(playerOne);
        Assertions.assertEquals(2, game.getPlayerCount());

        // start bets for blinds
        Assertions.assertEquals(EBetResult.OUT_OF_TURN, game.bet(playerTwo, 0));

        Assertions.assertEquals(EBetResult.SAW, game.bet(playerOne, 1));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerTwo, 0));
        Assertions.assertTrue(game.currentRoundDone());

        GameState preFlopState = game.nextRound();
        Assertions.assertEquals(EGameState.PRE_FLOP, game.getState());

        // no cards yet, just cards for the player
        Assertions.assertEquals(0, preFlopState.getCurrentCards().size());

        // players should have cards now
        List<Card> playerOneCards = game.handFor(playerOne).getCards();
        Assertions.assertEquals(2, playerOneCards.size());
        System.out.println("Player one cards: " + playerOneCards);

        List<Card> playerTwoCards = game.handFor(playerTwo).getCards();
        Assertions.assertEquals(2, playerTwoCards.size());
        System.out.println("Player two cards: " + playerTwoCards);

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerTwo, 0));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerOne, 0));
        Assertions.assertTrue(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.OUT_OF_TURN, game.bet(playerOne, 10));
        Assertions.assertTrue(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.OUT_OF_TURN, game.bet(playerTwo, 10));
        Assertions.assertTrue(game.currentRoundDone());

        GameState state = game.nextRound();
        Assertions.assertEquals(3, state.getCurrentCards().size());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerTwo, 0));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerOne, 0));
        Assertions.assertTrue(game.currentRoundDone());

        GameState postBlinds = game.nextRound();
        Assertions.assertEquals(4, postBlinds.getCurrentCards().size());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerTwo, 0));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerOne, 0));
        Assertions.assertTrue(game.currentRoundDone());

        GameState postflop = game.nextRound();
        Assertions.assertEquals(5, postflop.getCurrentCards().size());
        System.out.println(postflop.getCurrentCards());

        Assertions.assertEquals(EBetResult.RAISED, game.bet(playerTwo, 2));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.SAW, game.bet(playerOne, 2));
        Assertions.assertTrue(game.currentRoundDone());

        GameState done = game.nextRound();
        Assertions.assertEquals(5, done.getCurrentCards().size());
        Assertions.assertEquals(EGameState.DONE, game.getState());

        GameResult result = game.getResult();
        System.out.println("Winners:");
        System.out.println(result.getWinners());
    }

    @Test
    public void testThreePlayerGame() {
        Game game = new Game(1d);
        Player playerOne = new Player("Player One", 20);
        Player playerTwo = new Player("Player Two", 20);
        Player playerThree = new Player("Player Three", 20);

        game.addPlayer(playerOne);
        game.addPlayer(playerTwo);
        game.addPlayer(playerThree);

        game.start();

        Assertions.assertEquals(EBetResult.SAW, game.bet(playerThree, 1));
        Assertions.assertEquals(EBetResult.SAW, game.bet(playerOne, 2));
        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerTwo, 0));

        game.nextRound();
        List<Card> playerOneCards = game.handFor(playerOne).getCards();
        System.out.println("Player one cards: " + playerOneCards);

        List<Card> playerTwoCards = game.handFor(playerTwo).getCards();
        System.out.println("Player two cards: " + playerTwoCards);

        List<Card> playerThreeCards = game.handFor(playerThree).getCards();
        System.out.println("Player three cards: " + playerThreeCards);

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerTwo, 0));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerThree, 0));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerOne, 0));
        Assertions.assertTrue(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.OUT_OF_TURN, game.bet(playerOne, 10));
        Assertions.assertTrue(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.OUT_OF_TURN, game.bet(playerTwo, 10));
        Assertions.assertTrue(game.currentRoundDone());

        GameState state = game.nextRound();
        Assertions.assertEquals(3, state.getCurrentCards().size());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerTwo, 0));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerThree, 0));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerOne, 0));
        Assertions.assertTrue(game.currentRoundDone());

        GameState postBlinds = game.nextRound();
        Assertions.assertEquals(4, postBlinds.getCurrentCards().size());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerTwo, 0));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerThree, 0));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.CHECKED, game.bet(playerOne, 0));
        Assertions.assertTrue(game.currentRoundDone());

        GameState postflop = game.nextRound();
        Assertions.assertEquals(5, postflop.getCurrentCards().size());
        System.out.println(postflop.getCurrentCards());

        Assertions.assertEquals(EBetResult.RAISED, game.bet(playerTwo, 2));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.SAW, game.bet(playerThree, 2));
        Assertions.assertFalse(game.currentRoundDone());

        Assertions.assertEquals(EBetResult.SAW, game.bet(playerOne, 2));
        Assertions.assertTrue(game.currentRoundDone());

        GameState done = game.nextRound();
        Assertions.assertEquals(5, done.getCurrentCards().size());
        Assertions.assertEquals(EGameState.DONE, game.getState());

        GameResult result = game.getResult();
        System.out.println("Winners:");
        System.out.println(result.getWinners());
    }
}
