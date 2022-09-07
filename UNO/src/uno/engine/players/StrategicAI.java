package uno.engine.players;

import uno.engine.cards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * StrategicAI -- extends PlayerAI to specifically create
 * players that use strategy while playing.
 * @author Pranav Narahari
 */
public class StrategicAI extends PlayerAI {

    /**
     * Initializes StrategicAI player.
     * @param name Player's name.
     */
    public StrategicAI(String name) {
        this.playerName = name;
        hand = new ArrayList<>();
        setAsAI();
    }

    /**
     * AI will attempt to use a numbered card prior to using a non-numbered card.
     * This way the AI saves their best cards for last.
     * @param validCards Subset of hand where all of the cards are valid to select.
     * @return Selected card.
     */
    public Card selectCard(List<Card> validCards) {
        for (Card card: validCards) {
            if ((card.getValue() != Card.CardValue.WILD_DRAW_FOUR)
                    && (card.getValue() != Card.CardValue.DRAW_TWO)
                    && (card.getValue() != Card.CardValue.SKIP)
                    && (card.getValue() != Card.CardValue.REVERSE)
                    && (card.getValue() != Card.CardValue.WILD)) {
                return card;
            }
        }
        return validCards.get(0);
    }

    /**
     * AI selects the color to the most abundant color in their hand.
     * @return Selected color.
     */
    public Card.CardColor setColor() {
        return getMostAbundantColor();
    }

    private Card.CardColor getMostAbundantColor() {
        int numOfRedCards = 0;
        int numOfBlueCards = 0;
        int numOfGreenCards = 0;
        int numOfYellowCards = 0;
        int currentMaxNum = numOfBlueCards;
        Card.CardColor mostPlentifulColor = Card.CardColor.BLUE;
        for (Card card: getHand()) {
            if (card.getColor() == Card.CardColor.BLUE) {
                numOfBlueCards++;
                if (currentMaxNum < numOfBlueCards) {
                    currentMaxNum = numOfBlueCards;
                    mostPlentifulColor = Card.CardColor.BLUE;
                }
            } else if (card.getColor() == Card.CardColor.RED) {
                numOfRedCards++;
                if (currentMaxNum < numOfRedCards) {
                    currentMaxNum = numOfRedCards;
                    mostPlentifulColor = Card.CardColor.RED;
                }
            } else if (card.getColor() == Card.CardColor.GREEN) {
                numOfGreenCards++;
                if (currentMaxNum < numOfGreenCards) {
                    currentMaxNum = numOfGreenCards;
                    mostPlentifulColor = Card.CardColor.GREEN;
                }
            } else if (card.getColor() == Card.CardColor.YELLOW) {
                numOfYellowCards++;
                if (currentMaxNum < numOfYellowCards) {
                    currentMaxNum = numOfYellowCards;
                    mostPlentifulColor = Card.CardColor.YELLOW;
                }
            }
        }
        return mostPlentifulColor;
    }
}
