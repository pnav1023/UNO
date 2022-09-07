package uno.tests;

import org.junit.Test;
import uno.engine.cards.Card;
import uno.engine.cards.ColoredCard;
import uno.engine.Game;
import uno.engine.cards.UncoloredCard;

public class CreateCardTest {

    @Test
    public void createCard() {
        Card coloredCard = new ColoredCard(Card.CardColor.BLUE, Card.CardValue.ZERO, 3000);
        Card uncoloredCard = new UncoloredCard(Card.CardValue.WILD_DRAW_FOUR, 3000); // id: 3000 is for testing purposes and has no meaning
        assert (coloredCard.getColor() == Card.CardColor.BLUE && coloredCard.getValue() == Card.CardValue.ZERO);
        assert (uncoloredCard.getValue() == Card.CardValue.WILD_DRAW_FOUR);
    }

    @Test
    public void cardToString() {
        Card coloredCard = new ColoredCard(Card.CardColor.BLUE, Card.CardValue.ZERO, 3000);
        Card uncoloredCard = new UncoloredCard(Card.CardValue.WILD_DRAW_FOUR, 3000); // id: 3000 is for testing purposes and has no meaning
        coloredCard.printCard();
        uncoloredCard.printCard();
        assert (coloredCard.toString().equals(Card.CardColor.BLUE.toString() + " " + Card.CardValue.ZERO.toString()));
        assert (uncoloredCard.toString().equals(Card.CardValue.WILD_DRAW_FOUR.toString()));
    }

    @Test
    public void checkValidCardColor() {
        assert (null == Game.checkIsValidColor("pink"));
        assert (null == Game.checkIsValidColor("10232"));
        assert (null == Game.checkIsValidColor("\n"));
        assert (Card.CardColor.BLUE == Game.checkIsValidColor("blue"));
        assert (Card.CardColor.GREEN == Game.checkIsValidColor("green"));
        assert (Card.CardColor.YELLOW == Game.checkIsValidColor("yellow"));
        assert (Card.CardColor.RED == Game.checkIsValidColor("red"));
    }
}
