package uno.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * StartUpGame -- GUI to allow user to set up uno game based on number of actual players and AI.
 * @author Pranav Narahari.
 */
public class StartUpGame implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JPanel inputPanel;

    private JLabel buttonFeedback;
    private JTextField noOfActualPlayersField;
    private JTextField noOfBasicAIField;
    private JTextField noOfStrategicAIField;

    private int numOfActualPlayers;
    private int numOfBasicAIPlayers;
    private int numOfStrategicAIPlayers;
    private boolean isValidNumOfPlayers = false;

    /**
     * Create start up menu frame.
     */
    public StartUpGame() {
        frame = new JFrame("Uno Menu");
    }

    /**
     * Resets start up menu everytime a user choose to play another game.
     */
    public void resetFrame() {
        frame.getContentPane().removeAll();
        frame.repaint();
        isValidNumOfPlayers = false;

        rebuildFrame();
    }

    /**
     * UI update for user if given invalid input.
     */
    public void invalidNumOfPlayers() {
        buttonFeedback.setText("Invalid option. Choose between 2 and 10 total players.");
    }

    /**
     * Called automatically whenever a button is pressed.
     * @param e Specific button that was clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            numOfActualPlayers = Integer.parseInt(noOfActualPlayersField.getText());
            numOfBasicAIPlayers = Integer.parseInt(noOfBasicAIField.getText());
            numOfStrategicAIPlayers = Integer.parseInt(noOfStrategicAIField.getText());
            buttonFeedback.setText("Starting Game...");
            isValidNumOfPlayers = true;
        } catch (Exception exception) {
            invalidNumOfPlayers();
        }
        frame.pack();
    }

    private void rebuildFrame() {
        noOfActualPlayersField = new JFormattedTextField();
        noOfBasicAIField = new JFormattedTextField();
        noOfStrategicAIField = new JFormattedTextField();
        buttonFeedback = new JLabel();
        panel = new JPanel();
        inputPanel = new JPanel();

        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(this);

        panel.add(new JLabel("Uno Home Screen."));
        panel.add(inputPanel);
        panel.add(buttonFeedback);
        inputPanel.add(new JLabel("Number of Actual Players: "));
        inputPanel.add(noOfActualPlayersField);
        inputPanel.add(new JLabel("Number of Basic AI Players: "));
        inputPanel.add(noOfBasicAIField);
        inputPanel.add(new JLabel("Number of Strategic AI Players: "));
        inputPanel.add(noOfStrategicAIField);
        inputPanel.add(startGameButton);

        panel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        inputPanel.setSize(new Dimension(400, 120));
        noOfActualPlayersField.setPreferredSize(new Dimension(100, 20));
        noOfBasicAIField.setPreferredSize(new Dimension(100, 20));
        noOfStrategicAIField.setPreferredSize(new Dimension(100, 20));

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public int getNumOfActualPlayers() {
        return numOfActualPlayers;
    }

    public boolean isValidNumOfPlayers() {
        return isValidNumOfPlayers;
    }

    public int getNumOfBasicAIPlayers() {
        return numOfBasicAIPlayers;
    }

    public int getNumOfStrategicAIPlayers() {
        return numOfStrategicAIPlayers;
    }
}
