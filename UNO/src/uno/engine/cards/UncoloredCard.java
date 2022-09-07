package uno.engine.cards;

/**
 * UncoloredCard -- extends Card to specifically cater to wild
 * and wilddraw4 functionalities and elements.
 * @author Pranav Narahari.
 */
public class UncoloredCard extends Card {
    /**
     * Initializes uncolored card.
     * @param value Card value.
     * @param id Card unique identifier.
     */
    public UncoloredCard(CardValue value, int id) {
        this.setValue(value);;
        this.setColor(CardColor.NONE);
        this.id = id;
    }

    /**
     * Prints card to System.out.
     */
    public void printCard() {
        System.out.println(this.getValue());
    }

    /**
     * Converts card into string.
     * @return formatted card as string.
     */
    public String toString() {return this.getValue().toString();}
}
