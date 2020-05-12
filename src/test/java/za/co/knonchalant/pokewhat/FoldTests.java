package za.co.knonchalant.pokewhat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import za.co.knonchalant.pokewhat.domain.Game;
import za.co.knonchalant.pokewhat.domain.GameResult;
import za.co.knonchalant.pokewhat.domain.GameState;
import za.co.knonchalant.pokewhat.domain.Player;
import za.co.knonchalant.pokewhat.domain.lookup.EBetResult;
import za.co.knonchalant.pokewhat.domain.lookup.EGameState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FoldTests {

    @Test
    public void testTwoPlayersFoldEarly() {
        Player playerOne = new Player("Player One", 20);
        Player playerTwo = new Player("Player Two", 30);

        Game game = new Game(1d);
        game.addPlayer(playerOne);
        game.addPlayer(playerTwo);

        game.start();
        assertEquals(3d, game.getCurrentPool());

        // start bets for blinds
        game.fold(playerTwo);

        Assertions.assertTrue(game.currentRoundDone());
        GameState gameState = game.nextRound();

        Assertions.assertSame(EGameState.DONE, gameState.getState());

        List<GameResult> results = game.getResult();
        Assertions.assertEquals(playerOne, getWinner(results, 0));

        printResults(results);
    }
    private Player getWinner(List<GameResult> results, int i) {
        return results.get(i).getWinners().keySet().iterator().next();
    }
    private void printResults(List<GameResult> results) {
        for (GameResult result : results) {
            System.out.println("Winners of " + result.getDescription() + " pot:");
            System.out.println(result.getWinners());
            System.out.println("Winning: " + result.getAmountPerPlayer());
        }
    }

}
