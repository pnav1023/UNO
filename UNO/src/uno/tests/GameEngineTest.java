package uno.tests;

import org.junit.Before;
import org.junit.Test;
import uno.engine.cards.Card;
import uno.engine.Game;
import uno.engine.players.Player;

import java.util.List;
import java.util.Random;

/**
 * GameTest --- Holds all unit-tests for the implementation of UNO.
 * @param <test>
 */

public class GameEngineTest<test> {
    public Game uno;

    private int totalNumOfCards() {
        int cardCount = 0;
        cardCount += uno.getDecks().getDrawPile().size() + uno.getDecks().getDiscardPile().size();
        List<Player> players = uno.getPlayers();
        for (Player player: players) {
            cardCount += player.getHand().size();
        }
        return cardCount;
    }

    @Before
    public void setUp() {
        uno = new Game();
    }

    @Test
    public void createDrawPile() {
        uno.setUpGame(3, 1, 1);
        assert (uno.getDecks().getDrawPile().size() == 107 - 5 * 7);
    }

    @Test
    public void dealCorrectNumberOfCards() {
        uno.setUpGame(3, 4, 1);
        List<Player> players = uno.getPlayers();
        for (Player player : players) {
            assert (player.getHand().size() == 7);
        }
    }

    @Test
    public void testShuffle() {
        uno.setUpGame(0, 5, 3);
        List<Card> drawPile1 = uno.getDecks().getDrawPile();
        uno = new Game();
        uno.setUpGame(0, 5, 3);
        List<Card> drawPile2 = uno.getDecks().getDrawPile();
        boolean foundDifference = false;
        for (int i = 0; i < drawPile2.size(); i++) {
            if (drawPile1.get(i).getId() != drawPile2.get(i).getId()) {
                foundDifference = true;
                break;
            }
        }
        assert (foundDifference);
    }


    @Test
    public void basicGameTest() {
        uno.setUpGame(0, 3, 0);
        while (!uno.isGameOver()) {
            uno.playTurnAI();
            uno.endTurn();
        }
        assert (uno.getGameState().getCurrPlayer().getHand().size() == 0);
    }

    @Test
    public void testRestockCards() {
        uno.setUpGame(0, 5, 2);
        for (int i = 0; i < 200; i++) {
            if (uno.isGameOver()) {
                break;
            } else {
                uno.playTurnAI();
                uno.endTurn();
            }
        }
        assert (uno.getDecks().getDrawPile().size() != 0);
    }

    @Test
    public void checkValidCardPlayed() {
        uno.setUpGame(0, 3, 2);
        int turn = 0;
        while (!uno.isGameOver() && turn != 200) {
            uno.playTurnAI();
            uno.endTurn();
            turn++;
        }

        assert(uno.getGameState().getCurrPlayer().getHand().size() == 0 || turn == 200);
    }

    @Test
    public void checkTotalNumberOfCardsInGame() {
        int numberOfGamesLeft = 100;
        Random rand = new Random();
        int totalCards = 0;
        while (numberOfGamesLeft > 0) {
            int randNumOfBasicAI = rand.nextInt(5);
            int randNumOfStrategicAI = rand.nextInt(5);
            uno.setUpGame(0, randNumOfBasicAI+2, randNumOfStrategicAI);
            totalCards = totalNumOfCards();
            assert(totalCards == 108);
            while(!uno.isGameOver()) {
                uno.playTurnAI();
                uno.endTurn();
                totalCards = totalNumOfCards();
                assert(totalCards == 108);
            }
            numberOfGamesLeft--;
            uno = new Game();
        }
    }

    @Test
    public void checkValidCardsMethodWorks() {
        uno.setUpGame(0, 3, 4);
        Player currPlayer = null;
        while(!uno.isGameOver()){
            currPlayer = uno.getGameState().getCurrPlayer();
            List<Card> validCards = uno.getPlayersValidCards();
            assert(currPlayer.getHand().containsAll(validCards));
            for (Card validCard: validCards) {
                assert (uno.isValidCard(validCard));
            }
            uno.playTurnAI();
            uno.endTurn();
        }
    }

}


