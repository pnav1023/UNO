package uno.engine;

import uno.engine.cards.Card;
import uno.engine.cards.ColoredCard;
import uno.engine.cards.UncoloredCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Decks -- Holds draw pile, discard pile and all functionality
 * associated with the draw and discard pile.
 * @author Pranav Narahari.
 */
public class Decks {
    /**
     * State of the game.
     */
    public GameState gameState;

    /**
     * Pile for which player's draw from.
     */
    private List<Card> drawPile;

    /**
     * Pile for which player's place their selected card if valid
     */
    private List<Card> discardPile;

    /**
     * Initializes Decks.
     * @param gameState Current game state.
     *
     */
    public Decks(GameState gameState) {
        this.gameState = gameState;
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();

    }

    /**
     * Draw's card.
     * @return Card drawn.
     */
    public Card removeFromDrawPile() {
        if (drawPile.size() == 0) {
            restockDrawPile();
        }
        return drawPile.remove(0);

    }

    /**
     * Method to replenish draw pile when empty.
     */
    private void restockDrawPile() {
        drawPile.addAll(discardPile.subList(1, discardPile.size()));
        Card lastDiscarded = discardPile.get(0);
        discardPile.clear();
        discardPile.add(lastDiscarded);
        shuffleDrawPile();
    }

    /**
     * @return Card at the top of the Discard Pile.
     */
    public Card getLastDiscardedCard() {
        return discardPile.get(0);
    }

    /**
     * Creates entire deck of cards.
     *
     */
    public void createDrawPile() {
        createColoredCards();
        createUncoloredCards();
    }

    /**
     * Creates all red, green, yellow, and blue cards and adds them to the draw pile.
     */
    private void createColoredCards() {
        int idNum = 8;
        for (Card.CardColor color : Card.CardColor.values()) {
            if (color == Card.CardColor.NONE) {
                continue;
            }
            for (Card.CardValue value : Card.CardValue.values()) {
                if (value != Card.CardValue.WILD && value != Card.CardValue.WILD_DRAW_FOUR) {
                    if (value != Card.CardValue.ZERO) {
                        drawPile.add(new ColoredCard(color, value, idNum++));
                    }
                    drawPile.add(new ColoredCard(color, value, idNum++));
                }
            }
        }

    }

    /**
     * Creates 4 wild cards and 4 wild draw fours and adds them to the draw pile.
     */
    private void createUncoloredCards() {
        int idNum = 0;
        for (int i = 0; i < 4; i++) {
            drawPile.add(new UncoloredCard(Card.CardValue.WILD, idNum++));
            drawPile.add(new UncoloredCard(Card.CardValue.WILD_DRAW_FOUR, idNum++));
        }

    }

    /**
     * @param cardToDiscard Card to add to the top of the Discard Pile.
     */
    public void addToDiscardPile(Card cardToDiscard) {
        discardPile.add(0, cardToDiscard);
    }

    /**
     * Randomizes order of draw pile.
     */
    public void shuffleDrawPile() {
        Random rand = new Random();
        List<Card> shuffledPile = getDrawPile();
        for (int i = 0; i < getDrawPile().size(); i++) {
            int randomIndex = rand.nextInt(getDrawPile().size());
            Collections.swap(shuffledPile, 0, randomIndex);
        }
        drawPile = shuffledPile;
    }

    public List<Card> getDrawPile() {
        return drawPile;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    /**
     * Sets the initial discard card at the beginning of the game
     * to a valid card, i.e. a numbered card.
     */
    public void setValidStartingCard() {
        discardPile.add(0, drawPile.remove(0));
        Random rand = new Random();
        while (discardPile.get(0).getValue() == Card.CardValue.WILD_DRAW_FOUR
                || discardPile.get(0).getValue() == Card.CardValue.WILD
                || discardPile.get(0).getValue() == Card.CardValue.REVERSE
                || discardPile.get(0).getValue() == Card.CardValue.DRAW_TWO
                || discardPile.get(0).getValue() == Card.CardValue.SKIP) {
            int randomIndex = rand.nextInt(drawPile.size());
            drawPile.add(randomIndex, discardPile.remove(0));
            discardPile.add(0, drawPile.remove(0));
        }
        gameState.setCurrCard(discardPile.get(0));
    }

}
