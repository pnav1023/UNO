package uno.tests;

import org.junit.Before;
import org.junit.Test;
import uno.engine.Game;
import uno.engine.cards.Card;
import uno.engine.players.Player;

import java.util.List;

import static java.lang.Math.abs;

public class CardRulesTest {

    public Game uno;

    @Before
    public void setUp() {
        uno = new Game();
    }

    @Test
    public void checkAdditionAndSubtractionWorks() {
        uno.setUpGame(0,3,0);
        boolean playedArithmetic = false;
        while (!playedArithmetic) {
            String turnType = "";
            while (!uno.isGameOver() && !turnType.equalsIgnoreCase("arithmetic")) {
                turnType = uno.playTurnAI();
            }
            if (uno.getGameState().isGameOver) {
                uno = new Game();
                uno.setUpGame(0,3,0);
            } else if (turnType.equalsIgnoreCase("arithmetic")) {
                playedArithmetic = true;
            }
        }
        List<Card> discardPile = uno.getDecks().getDiscardPile();

        Card arithmeticCard1 = discardPile.get(0);
        Card arithmeticCard2 = discardPile.get(1);
        Card beforeArithmeticTurn = discardPile.get(2);

        System.out.println(arithmeticCard1.toString() + " -- " +arithmeticCard2.toString());
        System.out.println(beforeArithmeticTurn.toString());

        assert (arithmeticCard1.getColor() == arithmeticCard2.getColor()
                && uno.getGameState().currCard.getId() == arithmeticCard1.getId()
                && (   arithmeticCard1.toInt() + arithmeticCard2.toInt() == beforeArithmeticTurn.toInt()
                || abs( arithmeticCard1.toInt() - arithmeticCard2.toInt()) == beforeArithmeticTurn.toInt()));
    }

    @Test
    public void checkReverseFunctionality() {
        uno.setUpGame(0,3,0);
        List<Player> players = uno.getPlayers();
        boolean foundReverse = false;
        Player playsReverse = null;
        while (!foundReverse) {
            while (uno.getDecks().getLastDiscardedCard().getValue() != Card.CardValue.REVERSE
                    && !uno.getGameState().isGameOver) {
                playsReverse = uno.getGameState().getCurrPlayer();
                uno.playTurnAI();
                uno.endTurn();
            }
            if (uno.getGameState().isGameOver) {
                uno = new Game();
                uno.setUpGame(0,3,0);
                players = uno.getPlayers();
            } else if (uno.getDecks().getLastDiscardedCard().getValue() == Card.CardValue.REVERSE) {
                foundReverse = true;
            }
        }

        Player playerBeforeReverse = players.get((players.indexOf(playsReverse) - 1 + players.size()) % players.size());
        Player currPlayer = uno.getGameState().getCurrPlayer();

        System.out.println(playerBeforeReverse.getPlayerName());
        System.out.println(playsReverse.getPlayerName());
        System.out.println(currPlayer.getPlayerName());

        assert (playerBeforeReverse.getPlayerName().equals(currPlayer.getPlayerName()));
    }

    @Test
    public void checkDrawTwoModifiesGameState() {
        uno.setUpGame(0,3,0);
        boolean foundDraw2 = false;
        while (!foundDraw2) {
            while (uno.getDecks().getLastDiscardedCard().getValue() != Card.CardValue.DRAW_TWO
                    && !uno.getGameState().isGameOver) {
                uno.playTurnAI();
                uno.endTurn();
            }

            if (uno.getGameState().isGameOver) {
                uno = new Game();
                uno.setUpGame(0,3,0);
            } else if (uno.getDecks().getLastDiscardedCard().getValue() == Card.CardValue.DRAW_TWO) {
                foundDraw2 = true;
            }
        }
        assert ((uno.getGameState().currPenalty == 2)
                && (uno.getGameState().currCard.getValue() == Card.CardValue.DRAW_TWO));
    }

    @Test
    public void checkSkipFunctionality() {
        uno.setUpGame(0,3,0);
        boolean foundSkip = false;
        Player actualPlayedSkip = null;
        List<Player> players = uno.getPlayers();

        while (!foundSkip) {
            while (uno.getDecks().getLastDiscardedCard().getValue() != Card.CardValue.SKIP
                    && !uno.getGameState().isGameOver) {
                actualPlayedSkip = uno.getGameState().getCurrPlayer();
                uno.playTurnAI();
                uno.endTurn();
            }
            if (uno.getGameState().isGameOver) {
                uno = new Game();
                uno.setUpGame(0,3,0);
                System.out.println("\nNEW GAME");
                players = uno.getPlayers();
            } else if (uno.getDecks().getLastDiscardedCard().getValue() == Card.CardValue.SKIP) {
                foundSkip = true;
            }
        }

        Player currPlayer = uno.getGameState().getCurrPlayer();
        Player expectedPlayedSkip = null;

        if (uno.getGameState().isClockwise) {
            expectedPlayedSkip = players.get((players.indexOf(currPlayer) - 2 + players.size()) % players.size());
        } else {
            expectedPlayedSkip = players.get((players.indexOf(currPlayer) + 2) % players.size());
        }
        System.out.println(expectedPlayedSkip.getPlayerName()+ " index: "+players.indexOf(expectedPlayedSkip));
        System.out.println(actualPlayedSkip.getPlayerName()+ " index: "+players.indexOf(actualPlayedSkip));
        System.out.println(currPlayer.getPlayerName() + " index: "+players.indexOf(currPlayer));
        assert (expectedPlayedSkip.getPlayerName().equals(actualPlayedSkip.getPlayerName()));
    }

    @Test
    public void checkWildFunctionality() {
        uno.setUpGame(0, 3, 0);
        boolean foundWild = false;
        while (!foundWild) {
            while (uno.getDecks().getLastDiscardedCard().getValue() != Card.CardValue.WILD
                    && !uno.getGameState().isGameOver) {
                uno.playTurnAI();
                uno.endTurn();
            }

            if (uno.getGameState().isGameOver) {
                uno = new Game();
                uno.setUpGame(0, 3, 0);
            } else if (uno.getDecks().getLastDiscardedCard().getValue() == Card.CardValue.WILD) {
                foundWild = true;
            }
        }
        assert (uno.getGameState().currCard.getValue() == Card.CardValue.WILD);
    }


    @Test
    public void checkWildDraw4Functionality() {
        uno.setUpGame(0, 3, 0);
        boolean foundWildDraw4 = false;
        int penaltyBeforeDrawFour = 0;
        while (!foundWildDraw4) {
            while (uno.getDecks().getLastDiscardedCard().getValue() != Card.CardValue.WILD_DRAW_FOUR
                    && !uno.getGameState().isGameOver) {
                penaltyBeforeDrawFour = uno.getGameState().currPenalty;
                uno.playTurnAI();
                uno.endTurn();
            }

            if (uno.getGameState().isGameOver) {
                uno = new Game();
                uno.setUpGame(0, 3, 0);
            } else if (uno.getDecks().getLastDiscardedCard().getValue() == Card.CardValue.WILD_DRAW_FOUR) {
                foundWildDraw4 = true;
            }
        }
        System.out.println(uno.getGameState().currColor);
        System.out.println(uno.getGameState().currPenalty);
        assert ((uno.getGameState().currPenalty == penaltyBeforeDrawFour+4)
                && (uno.getGameState().currCard.getValue() == Card.CardValue.WILD_DRAW_FOUR));
    }

}
