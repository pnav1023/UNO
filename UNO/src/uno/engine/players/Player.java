package uno.engine.players;

import uno.engine.cards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Player -- Holds all relevant details for a single player in the game of UNO.
 * Also, player actions, i.e. selecting cards, discarding cards, etc., are
 * implemented within this class. Types of Players, i.e. AI, HumanPlayers, are derived from this class.
 * @author Pranav Narahari
 */
public abstract class Player {
    /**
     * Players current cards.
     */
    protected ArrayList<Card> hand;
    /**
     * Player's Name.
     */
    protected String playerName;

    /**
     * Whether or not player is an AI.
     */
    protected boolean isAI = false;

    /**
     * @return True is AI, false otherwise.
     */
    public boolean getIsAI() {
        return isAI;
    }

    /**
     * Discard card from hand.
     * @param playersSelectedCard Selected card to discard.
     */
    public void discardCard(Card playersSelectedCard) {
        hand.remove(playersSelectedCard);
    }

    /**
     * Adds drawn card to hand.
     * @param drawn Card drawn.
     */
    public void addToHand(Card drawn) {
        hand.add(drawn);
    }

    public List<Card> getHand() {
        return hand;
    }
    public String getPlayerName() {
        return playerName;
    }
}
