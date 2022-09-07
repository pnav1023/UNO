package uno.gui;

import uno.engine.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * WinnerScene -- GUI for the completion of the game. Gives user option to exit or start new game.
 * @author Pranav Narahari
 */
public class WinnerScene implements ActionListener {
    private JFrame frame;
    private JPanel panel;

    public boolean playAgain = false;
    public boolean madeSelection = false;

    /**
     * Resets winner menu to show winner of most recent game.
     * @param gameState Current game state.
     */
    public void resetWinnerScene(GameState gameState) {
        frame.getContentPane().removeAll();
        frame.repaint();
        madeSelection = false;
        rebuildWinnerScene(gameState);
    }

    /**
     * Initializes winner frame.
     */
    public WinnerScene(){
        frame = new JFrame("You Won!");
    }

    private void rebuildWinnerScene(GameState gameState) {
        panel = new JPanel(new FlowLayout());
        JLabel playerLabel = new JLabel(gameState.getCurrPlayer().getPlayerName()+" is the winner.");
        JButton playAgainBtn = new JButton("Click to start new game");
        JButton exitBtn = new JButton("Click to exit game");

        playAgainBtn.setActionCommand("again");
        playAgainBtn.addActionListener(this);
        exitBtn.setActionCommand("exit");
        exitBtn.addActionListener(this);

        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));

        panel.add(playerLabel);
        panel.add(playAgainBtn);
        panel.add(exitBtn);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Called automatically whenever a button is pressed.
     * @param e Specific button that was clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("again")) {
            playAgain = true;
        } else if (e.getActionCommand().equalsIgnoreCase("exit")) {
            frame.getContentPane().removeAll();
            frame.repaint();

            panel = new JPanel(new FlowLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
            JLabel gameOverLabel = new JLabel("Game Over. GoodBye.");

            panel.add(gameOverLabel);

            frame.add(panel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        }
        madeSelection = true;
    }
}
