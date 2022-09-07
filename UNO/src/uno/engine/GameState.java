package uno.engine;

import uno.engine.cards.Card;
import uno.engine.players.Player;

/**
 * GameState -- Holds all relevant information that is updates on a turn-by-turn basis.
 * @author Pranav Narahari.
 */
public class GameState {
    /**
     * Current color.
     */
    public Card.CardColor currColor;

    /**
     * Current value.
     */
    public Card.CardValue currValue;

    /**
     * Current card.
     */
    public Card currCard;

    /**
     * Current Penalty.
     */
    public int currPenalty;

    /**
     * Current Player.
     */
    private Player currPlayer;

    /**
     * Boolean to check if draw pile is empty.
     */
    public boolean isDrawPileEmpty;

    /**
     * Boolean for direction.
     */
    public boolean isClockwise;

    /**
     * Boolean for if game is over.
     */
    public boolean isGameOver;

    /**
     * Initializes the game state to what to the
     * state at the beginning of the game.
     */
    public GameState() {
        isDrawPileEmpty = false;
        currPenalty = 0;
        isClockwise = true;
        isGameOver = false;
        currPenalty = 0;
    }

    /**
     * Sets current card, current value, and current color.
     * @param currCard
     */
    public void setCurrCard(Card currCard) {
        this.currCard = currCard;
        this.currColor = currCard.getColor();
        this.currValue = currCard.getValue();
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(Player newCurrPlayer) {
        currPlayer = newCurrPlayer;
    }
}
