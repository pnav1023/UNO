package uno.engine.players;

import uno.engine.cards.Card;

import java.util.List;

/**
 * Player -- Holds all relevant details for an AI player in the game of UNO.
 * Also, player actions, i.e. selecting cards, discarding cards, selecting color etc., are
 * implemented within this class. Types of Players, i.e. AI, HumanPlayers, are derived from this class.
 * @author Pranav Narahari
 */
public abstract class PlayerAI extends Player {
    protected void setAsAI() {
        isAI = true;
    }

    /**
     * Allows AI's derived from this class to implement
     * their own strategies for selecting a card.
     * @param validCards Subset of hand where all of the cards are valid to select.
     * @return Selected card.
     */
    public abstract Card selectCard(List<Card> validCards);

    /**
     * Allows AI's derived from this class to implement
     * their own strategies for selecting a color.
     * @return Selected color.
     */
    public abstract Card.CardColor setColor();
}
