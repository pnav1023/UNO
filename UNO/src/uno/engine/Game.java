package uno.engine;

import uno.engine.cards.Card;
import uno.engine.players.*;

import java.util.*;

import static java.lang.Math.abs;

/**
 * Game --- Holds relevant information for the game of UNO that is to be shared
 * to all players, i.e. the decks, player list, game state.
 * @author Pranav Narahari
 */
public class Game {
    /**
     * Holds all relevant game info that may change with every turn
     */
    private GameState gameState;

    /**
     * Holds draw pile and discard pile.
     */
    private Decks decks;

    /**
     * List of all players.
     */
    private List<Player> players;

    /**
     * Initializes game object.
     */
    public Game() {
        gameState = new GameState();
        decks = new Decks(gameState);
        players = new ArrayList<>();
    }

    /**
     * Deals each player 7 cards.
     */
    public void dealCards() {
        for (int j = 0; j < players.size(); j++) {
            for (int i = 0; i < 7; i++) {
                Card drawn = decks.removeFromDrawPile();
                players.get(j).addToHand(drawn);
            }
        }
    }

    /**
     * Sets up everything in the game of Uno prior to the start of the first player's turn
     * @param numOfHumanPlayers Selected number of human players.
     * @param numOfBasicAI Selected number of Basic AI players.
     * @param numOfStrategicAI Selected number of Strategic AI players.
     * @return True for successful set up, false otherwise.
     */
    public boolean setUpGame(int numOfHumanPlayers, int numOfBasicAI, int numOfStrategicAI) {
        if (!isValidNumOfPlayers(numOfHumanPlayers, numOfBasicAI, numOfStrategicAI)) {
            return false;
        }
        initializePlayers(numOfHumanPlayers, numOfBasicAI, numOfStrategicAI);
        decks.createDrawPile();
        decks.shuffleDrawPile();
        decks.setValidStartingCard();
        dealCards();
        gameState.setCurrPlayer(players.get(0));
        return true;
    }

    /**
     * Executes turn based on what is chosen by the human player.
     * @param turnType Draw card or select card
     * @param cardSelected Card selected or null.
     * @return Turn type.
     */
    public String playTurn(String turnType, Card cardSelected) {
        if (turnType.equals("draw card"))  {
            playerDrawsCard();
        } else if (turnType.equals("select card") && !isValidCard(cardSelected)) {
            return "invalid";
        } else if (turnType.equals("select card") && isValidCard(cardSelected)) {
            playerDiscardsCard(cardSelected);
        }
        return turnType;
    }

    /**
     * Executes turn based on what is chosen by the AI.
     * @return Turn type
     */
    public String playTurnAI() {
        List<Card> validCards = getPlayersValidCards();
        PlayerAI currAI = (PlayerAI) gameState.getCurrPlayer();

        if (validCards.size() != 0) {
            aiSelectsCard(validCards, currAI);
        } else {
            List<Card> arithmeticValidCards = getArithmeticValidCards();
            if (arithmeticValidCards.size() != 0) {
                aiUsesArithmeticRule(arithmeticValidCards);
                return "arithmetic";
            } else {
                aiDrawsCard(currAI);
            }
        }
        return "valid";
    }

    private void aiDrawsCard(PlayerAI currAI) {
        Card drawn = playerDrawsCard();
        if (((drawn.getValue() == Card.CardValue.WILD)
                || (drawn.getValue() == Card.CardValue.WILD_DRAW_FOUR)) && isValidCard(drawn)){
            setColor(currAI.setColor());
        }
    }

    private void aiUsesArithmeticRule(List<Card> arithmeticValidCards) {
        Card card1 = arithmeticValidCards.get(0);
        Card card2 = arithmeticValidCards.get(1);
        decks.addToDiscardPile(card1);
        decks.addToDiscardPile(card2);
        gameState.getCurrPlayer().discardCard(card1);
        gameState.getCurrPlayer().discardCard(card2);
        updateGameState(card2);
    }

    private void aiSelectsCard(List<Card> validCards, PlayerAI currAI) {
        Card cardSelected = currAI.selectCard(validCards);
        playerDiscardsCard(cardSelected);
        if ((cardSelected.getValue() == Card.CardValue.WILD)
                || (cardSelected.getValue() == Card.CardValue.WILD_DRAW_FOUR)) {
            setColor(currAI.setColor());
        }
    }

    /**
     * @return A subset of the current players hand that are valid if played.
     */
    public List<Card> getPlayersValidCards() {
        List<Card> validCards = new ArrayList<>();
        List<Card> playersCards = gameState.getCurrPlayer().getHand();
        for (Card card: playersCards) {
            if (isValidCard(card)) {
                validCards.add(card);
            }
        }
        return validCards;
    }

    /**
     * @param chosenColorStr user inputted color
     * @return Card.CardColor if user inputs a valid color,
     * or null if the user inputs an invalid color
     */
    public static Card.CardColor checkIsValidColor(String chosenColorStr) {
        if (chosenColorStr.equalsIgnoreCase("blue")) {
            return Card.CardColor.BLUE;
        } else if (chosenColorStr.equalsIgnoreCase("green")) {
            return  Card.CardColor.GREEN;
        } else if (chosenColorStr.equalsIgnoreCase("red")) {
            return  Card.CardColor.RED;
        } else if (chosenColorStr.equalsIgnoreCase("yellow")) {
            return  Card.CardColor.YELLOW;
        }
        return null;
    }

    /**
     * @return Two cards that exist in the current players hand.
     * That are valid under the arithmetic rules: addition and subtraction.
     */
    private List<Card> getArithmeticValidCards() {
        List<Card> pairOfCards = new ArrayList<>();
        if (gameState.currCard.toInt() == -1) {
            return pairOfCards;
        }
        for (Card card1: gameState.getCurrPlayer().getHand()) {
            if (card1.toInt() == -1) {
                continue;
            }
            for (Card card2: gameState.getCurrPlayer().getHand()) {
                if (card1.getId() == card2.getId()
                        || card2.toInt() == -1
                        || card1.getColor() != card2.getColor()) {
                    continue;
                }
                if (       card1.toInt() + card2.toInt() == gameState.currCard.toInt()
                        || abs(card1.toInt() - card2.toInt()) == gameState.currCard.toInt()) {
                    pairOfCards.add(card1);
                    pairOfCards.add(card2);
                    return pairOfCards;
                }
            }
        }
        return pairOfCards;
    }

    /**
     * @param checkIfValid Card to check.
     * @return True if playable card, false otherwise.
     */
    public boolean isValidCard(Card checkIfValid) {
        if (gameState.currPenalty == 0 ) {
            // last card was not draw2 or draw4
            if (   gameState.currColor == checkIfValid.getColor()
                    || gameState.currValue == checkIfValid.getValue()
                    || checkIfValid.getValue() == Card.CardValue.WILD
                    || checkIfValid.getValue() == Card.CardValue.WILD_DRAW_FOUR) {
                return true;
            }
        } else {
            if (gameState.currCard.getValue() == checkIfValid.getValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Game functionality if player decides to draw a card.
     * If the card is playable, then card is automatically played. If
     * not, the card is added to the players hand and necessary penalty is added.
     * @return Card drawn.
     */
    public Card playerDrawsCard() {
        Card drawn = decks.removeFromDrawPile();
        if (isValidCard(drawn)) {
            playerDiscardsCard(drawn);
        } else {
            gameState.getCurrPlayer().addToHand(drawn);
            givePlayerPenalty();
        }
        return drawn;
    }

    /**
     * Game functionality if to discard player's selected card.
     */
    private void playerDiscardsCard(Card cardSelected) {
        decks.addToDiscardPile(cardSelected);
        gameState.getCurrPlayer().discardCard(cardSelected);
        updateGameState(cardSelected);
    }

    /**
     * Functionality that makes the current player take the
     * card penalty if they don't have a valid card to play.
     */
    private void givePlayerPenalty() {
        for (int i = 0; i < gameState.currPenalty; i++) {
            Card drawn = decks.removeFromDrawPile();
            gameState.getCurrPlayer().addToHand(drawn);
        }
        gameState.currPenalty = 0;
    }

    /**
     * Initializes all players.
     * @param numOfHumanPlayers Selected number of human players.
     * @param numOfBasicAI Selected number of Basic AI players.
     * @param numOfStrategicAI Selected number of Strategic AI players.
     */
    private void initializePlayers(int numOfHumanPlayers, int numOfBasicAI, int numOfStrategicAI) {
        int playerNumber = 1;
        for (int i = 0; i < numOfHumanPlayers; i++) {
            players.add(new HumanPlayer("Player"+playerNumber++));
        }
        for (int i = 0; i < numOfBasicAI; i++) {
            players.add(new BasicAI("Basic AI - Player"+playerNumber++));
        }
        for (int i = 0; i < numOfStrategicAI; i++) {
            players.add(new StrategicAI("Strategic AI - Player"+playerNumber++));
        }
        Collections.shuffle(players);
    }

    /**
     * Checks if inputted number of players is valid.
     * @param numOfHumanPlayers Selected number of human players.
     * @param numOfBasicAI Selected number of Basic AI players.
     * @param numOfStrategicAI Selected number of Strategic AI players.
     * @return True is valid number of players, false otherwise.
     */
    private boolean isValidNumOfPlayers(int numOfHumanPlayers, int numOfBasicAI, int numOfStrategicAI) {
        if (numOfHumanPlayers < 0 || numOfBasicAI < 0 || numOfStrategicAI < 0) {
            return false;
        } else if (numOfHumanPlayers > 10 || numOfBasicAI > 10 || numOfStrategicAI > 10) {
            return false;
        } else if ( (numOfHumanPlayers+numOfBasicAI+numOfStrategicAI) < 2) {
            System.out.println("Too few players");
            return false;
        } else if ( (numOfHumanPlayers+numOfBasicAI+numOfStrategicAI) > 10) {
            System.out.println("Too many players");
            return false;
        }
        return true;
    }

    /**
     * Gets the next player based on the direction in gameState.
     * @param currPlayer Current player.
     * @return The next player.
     */
    private Player getNextPlayer(Player currPlayer) {
        if (gameState.isClockwise) {
            if (gameState.currCard.getValue() == Card.CardValue.SKIP) {
                return players.get((players.indexOf(currPlayer)+2)%players.size());
            } else {
                return players.get((players.indexOf(currPlayer)+1)%players.size());
            }
        } else {
            if (gameState.currCard.getValue() == Card.CardValue.SKIP) {
                return players.get((players.indexOf(currPlayer)-2+players.size())%players.size());
            } else {
                return players.get((players.indexOf(currPlayer)-1+players.size())%players.size());
            }
        }

    }

    public Decks getDecks() {
        return decks;
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Checks gameState to see if game is over.
     * @return True is game is over, false otherwise.
     */
    public boolean isGameOver() {
        return gameState.isGameOver;
    }

    /**
     * Stores the last card played and updates the gameState.
     * for special cards, like reverse, draw two, wild, and wild draw four.
     * @param playersSelectedCard Card played by current player.
     */
    public void updateGameState(Card playersSelectedCard) {
        if (gameState.getCurrPlayer().getHand().size() == 0) {
            gameState.isGameOver = true;
            return;
        }
        gameState.setCurrCard(playersSelectedCard);
        if (playersSelectedCard.getValue() == Card.CardValue.REVERSE) {
            playedReverse();
        } else if (playersSelectedCard.getValue() == Card.CardValue.DRAW_TWO) {
            playedDrawTwo();
        } else if (playersSelectedCard.getValue() == Card.CardValue.WILD_DRAW_FOUR) {
            playedWildDrawFour();
        }
    }


    /**
     * Enables current player to set the current color
     * and adds 4 to the current penalty.
     */
    private void playedWildDrawFour() {
        gameState.currPenalty += 4;
    }

    /**
     * Reverses direction of players.
     */
    private void playedReverse() {
        gameState.isClockwise = !gameState.isClockwise;
    }

    /**
     * Adds 2 to the current penalty total.
     */
    private void playedDrawTwo() {
        gameState.currPenalty += 2;
    }

    /**
     * Sets next player as current player.
     */
    public void endTurn() {
        if (!gameState.isGameOver) {
            gameState.setCurrPlayer(getNextPlayer(gameState.getCurrPlayer()));
        }
    }

    /**
     * Updates game state with selected color.
     * @param color Color selected.
     */
    public void setColor(Card.CardColor color) {
        gameState.currColor = color;
    }
}
