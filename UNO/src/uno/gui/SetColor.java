package uno.gui;

import uno.engine.cards.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SetColor -- GUI for displaying the colors the player can choose from after they play a wild or wild draw 4.
 * @author Pranav Narahari.
 */
public class SetColor implements ActionListener {
    /**
     * Boolean for whether or the player made their selection.
     */
    public boolean hasConfirmedSelection = false;

    private JFrame frame;
    private JPanel panel;
    private JComboBox choices;
    private JButton confirmBtn;
    private JLabel testLabel;

    private Card.CardColor[] validColors = {Card.CardColor.BLUE, Card.CardColor.GREEN, Card.CardColor.RED, Card.CardColor.YELLOW};
    private Card.CardColor selectedColor;

    /**
     * Creates frame.
     */
    public SetColor() {
        frame = new JFrame("Set Color");
    }

    /**
     * Resets frame and prompts user to set color.
     */
    public void resetFrame() {
        frame.getContentPane().removeAll();
        frame.repaint();
        hasConfirmedSelection = false;
        rebuildFrame();
    }

    public Card.CardColor getSelectedColor() {
        return selectedColor;
    }

    /**
     * Called automatically whenever a button is pressed.
     * @param e Specific button that was clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        selectedColor = (Card.CardColor) choices.getSelectedItem();
        testLabel.setText(selectedColor.toString()+" selected");
        hasConfirmedSelection = true;
    }

    private void rebuildFrame() {
        panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        JLabel setColorLabel = new JLabel("Set Color to ");
        confirmBtn = new JButton("Confirm");
        confirmBtn.addActionListener(this);

        testLabel = new JLabel();

        choices = new JComboBox(validColors);

        panel.add(setColorLabel);
        panel.add(choices);
        panel.add(confirmBtn);
        panel.add(testLabel);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(700, 500));
        frame.setVisible(true);
    }
}
