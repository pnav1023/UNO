package uno.tests;

import org.junit.Before;
import org.junit.Test;
import uno.engine.Game;
import uno.engine.players.Player;

import java.util.List;

public class PlayerTest {
    public Game uno;

    @Before
    public void setUp() {
        uno = new Game();
    }

    @Test
    public void setPlayerAmount() {
        uno.setUpGame(5, 3, 1);
        assert (uno.getPlayers().size() == 9);
    }

    @Test
    public void setTooFewPlayers() {
        uno.setUpGame(-1, 1, 0);
        assert (uno.getPlayers().size() == 0);
    }

    @Test
    public void setTooManyPlayers() {
        uno.setUpGame(12, 0, 0);
        assert (uno.getPlayers().size() == 0);
    }

    @Test
    public void checkAIParamIsSet() {
        uno.setUpGame(5, 3, 2);
        int numOfActualPlayers = 0;
        int numOfAI = 0;
        List<Player> players = uno.getPlayers();
        for (Player player: players) {
            if (player.getIsAI()) {
                numOfAI++;
            } else if (!player.getIsAI()) {
                numOfActualPlayers++;
            }
        }
        assert((numOfAI == 5) && (numOfActualPlayers == 5));
    }
}
