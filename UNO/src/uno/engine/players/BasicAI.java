package uno.engine.players;

import uno.engine.cards.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * BasicAI -- extends PlayerAI to specifically create
 * players that lack strategy.
 * @author Pranav Narahari
 */
public class BasicAI extends PlayerAI {

    /**
     * Initializes BasicAI player.
     * @param name Player's name.
     */
    public BasicAI(String name) {
        this.playerName = name;
        hand = new ArrayList<>();
        setAsAI();
    }

    /**
     * AI selects a valid card at random.
     * @param validCards Subset of hand where all of the cards are valid to select.
     * @return Randomly selected card.
     */
    public Card selectCard(List<Card> validCards) {
        Random rand = new Random();
        int randCardIdx = rand.nextInt(validCards.size());
        return validCards.get(randCardIdx);
    }

    /**
     * AI sets color at random.
     * @return Randomly selected color.
     */
    public Card.CardColor setColor() {
        List<Card.CardColor> colors = new ArrayList<>();
        colors.add(Card.CardColor.BLUE);
        colors.add(Card.CardColor.RED);
        colors.add(Card.CardColor.GREEN);
        colors.add(Card.CardColor.YELLOW);

        Random rand = new Random();
        int randColorIndex = rand.nextInt(colors.size());
        return colors.get(randColorIndex);
    }
}
