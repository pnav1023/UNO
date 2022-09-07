package uno.engine.cards;

/**
 * ColoredCard --- extends Card to specifically cater to number, skip, reverse,
 * and draw2 Card functionalities and elements.
 * @author Pranav Narahari
 */
public class ColoredCard extends Card {
    /**
     * Initializes colored card.
     * @param color Card color.
     * @param value Card value.
     * @param id Card unique identifier.
     */
    public ColoredCard(CardColor color, CardValue value, int id) {
        this.setValue(value);
        this.setColor(color);
        this.id = id;
    }

    /**
     * Prints card to System.out.
     */
    public void printCard() {
        System.out.println(this.getColor()+" "+this.getValue());
    }

    /**
     * Converts card into string.
     * @return formatted card as string.
     */
    public String toString() {
        return this.getColor().toString()+" "+this.getValue().toString();
    }
}
