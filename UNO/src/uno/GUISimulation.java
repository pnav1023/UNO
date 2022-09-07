package uno;

import uno.engine.Game;
import uno.engine.cards.Card;
import uno.gui.GameBoard;
import uno.gui.SetColor;
import uno.gui.StartUpGame;
import uno.gui.WinnerScene;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * GUISimulation -- GUI game loop.
 * @author Pranav Narahari.
 */
public class GUISimulation {
    private static Game uno;
    private static StartUpGame startUp;
    private static GameBoard gameBoard;
    private static SetColor setColor;
    private static WinnerScene winnerScene;
    private static String isValidSelection = "";

    /**
     * Functionality of game loop.
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        do {
            uno = new Game();

            startUpGameGUI();
            setUpGameBoard();

            while(!uno.getGameState().isGameOver) {
                playersTurnGUI();
                if (uno.getGameState().getCurrPlayer().getIsAI()) {
                    uno.playTurnAI();
                    uno.endTurn();
                } else {
                    if (gameBoard.turnType.equals("draw card")) {
                        selectedDrawCard();
                        isValidSelection = gameBoard.turnType;
                    } else if (gameBoard.getCardSelected().getValue() == Card.CardValue.WILD || gameBoard.getCardSelected().getValue() == Card.CardValue.WILD_DRAW_FOUR) {
                        isValidSelection = selectedWildCard();
                    } else {
                        isValidSelection = uno.playTurn(gameBoard.turnType, gameBoard.getCardSelected());
                    }
                    if (!isValidSelection.equals("invalid")) {
                        uno.endTurn();
                    }
                }
            }
            showWinnerGUI();
        } while (winnerScene.playAgain);
        System.out.println("Finished");
    }

    private static void setUpGameBoard() throws IOException {
        if (gameBoard == null) {
            gameBoard = new GameBoard();
        }
        gameBoard.resetGameBoard(uno.getGameState());
    }

    private static void showWinnerGUI() throws InterruptedException {
        if (winnerScene == null) {
            winnerScene = new WinnerScene();
        }
        winnerScene.resetWinnerScene(uno.getGameState());
        while (!winnerScene.madeSelection) {
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static void playersTurnGUI() throws InterruptedException, IOException {
        gameBoard.madeSelection = false;
        gameBoard.resetGameBoard(uno.getGameState());
        while (!gameBoard.hasMadeSelection()) {
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static String selectedWildCard() throws InterruptedException {
        String isValidSelection;
        Card.CardColor setColor = setColorSim();
        isValidSelection = uno.playTurn(gameBoard.turnType, gameBoard.getCardSelected());
        uno.setColor(setColor);
        return isValidSelection;
    }

    private static void selectedDrawCard() throws InterruptedException {
        Card cardDrawn = uno.playerDrawsCard();
        if ((cardDrawn.getValue() == Card.CardValue.WILD || cardDrawn.getValue() == Card.CardValue.WILD_DRAW_FOUR)
                && uno.isValidCard(cardDrawn)) {
            Card.CardColor setColor = setColorSim();
            uno.setColor(setColor);
        }
    }

    private static void startUpGameGUI() throws InterruptedException {
        if (startUp == null) {
            startUp = new StartUpGame();
        }
        startUp.resetFrame();
        while (!startUp.isValidNumOfPlayers()) {
            System.out.println("Sleeping");
            TimeUnit.SECONDS.sleep(1);
        }
        boolean setValidNumOfPlayers = false;
        while (!setValidNumOfPlayers) {
            setValidNumOfPlayers = uno.setUpGame(startUp.getNumOfActualPlayers(),
                                                 startUp.getNumOfBasicAIPlayers(),
                                                 startUp.getNumOfStrategicAIPlayers());
            if (!setValidNumOfPlayers) {
                startUp.invalidNumOfPlayers();
            }
        }
    }

    private static Card.CardColor setColorSim() throws InterruptedException {
        if (setColor == null) {
            setColor = new SetColor();
        }
        setColor.resetFrame();
        while (!setColor.hasConfirmedSelection) {
            TimeUnit.SECONDS.sleep(1);
        }
        return setColor.getSelectedColor();
    }
}
