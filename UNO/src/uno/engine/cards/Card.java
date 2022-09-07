package uno.engine.cards;

/**
 * Card --- abstract class to serve as framework for
 * UncoloredCard and ColoredCard
 * @author Pranav Narahari
 */
public abstract class Card {
    /**
     * CardColor --- enums for types of card colors.
     */
    public enum CardColor {
        RED, BLUE, YELLOW, GREEN, NONE
    }

    /**
     * CardValue --- enums for types of card values.
     */
    public enum CardValue {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT,
        NINE, REVERSE, SKIP, DRAW_TWO, WILD_DRAW_FOUR, WILD
    }

    private CardValue value;
    private CardColor color;

    /**
     * Identifier which is unique for every card.
     */
    protected int id;

    /**
     * Prints card to System.out.
     */
    public abstract void printCard();

    /**
     * Converts card into string
     * @return formatted card as string
     */
    public abstract String toString();

    /**
     * @return Card's unique identifier.
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return Value of card as integer if numbered card, -1 otherwise.
     */
    public int toInt () {
        if (value == Card.CardValue.ZERO) {
            return 0;
        } else if (value == Card.CardValue.ONE) {
            return 1;
        } else if (value == Card.CardValue.TWO) {
            return 2;
        } else if (value == Card.CardValue.THREE) {
            return 3;
        } else if (value == Card.CardValue.FOUR) {
            return 4;
        } else if (value == Card.CardValue.FIVE) {
            return 5;
        } else if (value == Card.CardValue.SIX) {
            return 6;
        } else if (value == Card.CardValue.SEVEN) {
            return 7;
        } else if (value == Card.CardValue.EIGHT) {
            return 8;
        } else if (value == Card.CardValue.NINE) {
            return 9;
        } else {
            return -1;
        }
    }

    public CardValue getValue() {
        return this.value;
    }

    public CardColor getColor() {
        return this.color;
    }

    public void setValue(CardValue value) {
        this.value = value;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }
}


