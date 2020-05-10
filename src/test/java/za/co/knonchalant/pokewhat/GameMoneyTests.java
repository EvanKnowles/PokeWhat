package za.co.knonchalant.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knonchalant.pokewhat.domain.Game;
import za.co.knonchalant.pokewhat.domain.GameResult;
import za.co.knonchalant.pokewhat.domain.Player;
import za.co.knonchalant.pokewhat.domain.lookup.EBetResult;
import za.co.knonchalant.pokewhat.domain.lookup.EGameState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        while (game.nextRound().getState() != EGameState.DONE){
            Assertions.assertTrue(game.currentRoundDone());
        }

        List<GameResult> results = game.getResult();
        for (GameResult result : results) {
            System.out.println("Winners:");
            System.out.println(result.getWinners());
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

        while (game.nextRound().getState() != EGameState.DONE){
            Assertions.assertTrue(game.currentRoundDone());
        }

        List<GameResult> results = game.getResult();
        for (GameResult result : results) {
            System.out.println("Winners of " + result.getDescription() + " pot:");
            System.out.println(result.getWinners());
            System.out.println("Winning: " + result.getAmountPerPlayer());
        }
    }
}
