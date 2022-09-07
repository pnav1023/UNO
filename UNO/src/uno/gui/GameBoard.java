package uno.gui;


import uno.engine.GameState;
import uno.engine.cards.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GameBoard -- GUI for displaying the state of the game for each player on their turn.
 * @author Pranav Narahari.
 */
public class GameBoard implements ActionListener {
    /**
     * Card selected by player.
     */
    public Card cardSelected;

    /**
     * Player's selected type of turn, i.e. draw card or select card.
     */
    public String turnType;

    /**
     * Boolean for whether or the player made their selection.
     */
    public boolean madeSelection = false;

    private JFrame frame;
    private JPanel gameStatePanel;
    private JPanel boardPanel;
    private JPanel playerActionBtns;
    private JPanel playersCards;
    private JLabel btnTest;

    private GameState currGameState;
    private List<JButton> playersHandBtns;

    /**
     * Creates game board by creating the frame.
     */
    public GameBoard() {
        frame = new JFrame("Game Board");
    }

    /**
     * Updates the game board for each player's turn.
     * @param gameState Current game state.
     * @throws IOException
     */
    public void resetGameBoard(GameState gameState) throws IOException {
        this.currGameState = gameState;
        frame.getContentPane().removeAll();
        frame.repaint();
        rebuildGameBoard();
    }

    public Card getCardSelected() {
        return cardSelected;
    }

    public boolean hasMadeSelection() {
        return madeSelection;
    }

    private void setCurrCardPanel() {
        JPanel currCardPanel = new JPanel();
        currCardPanel.setLayout(new BoxLayout(currCardPanel, BoxLayout.PAGE_AXIS));

        JLabel lastPlayedStr = new JLabel("Last Played Card: "+currGameState.currCard.toString());
        JLabel lastPlayedCard = new JLabel();
        try {
            ImageIcon lastPlayedIcon = new ImageIcon(getClass().getResource("/card-images/"
                    +currGameState.currCard.getColor().toString().toLowerCase()+"/"
                    +currGameState.currCard.getValue().toString().toLowerCase()+".png"));
            Image lastPlayedImg =
                    lastPlayedIcon.getImage().getScaledInstance(100, 150, java.awt.Image.SCALE_SMOOTH);
            lastPlayedCard.setIcon(new ImageIcon(lastPlayedImg));
        } catch (Exception e) {
            System.out.println("Failed card: "+currGameState.currCard.toString());
            System.out.println("Failed Path: /card-images/"
                    +currGameState.currCard.getColor().toString().toLowerCase()+"/"
                    +currGameState.currCard.getValue().toString().toLowerCase()+".png");
            e.printStackTrace();
        }

        currCardPanel.add(lastPlayedStr);
        currCardPanel.add(lastPlayedCard);

        gameStatePanel.add(currCardPanel);
    }

    private void setGameStatePanel() throws IOException {
        gameStatePanel = new JPanel(new FlowLayout());

        setCurrCardPanel();

        JPanel colorAndPenalty = new JPanel();
        colorAndPenalty.setLayout(new BoxLayout(colorAndPenalty, BoxLayout.PAGE_AXIS));

        colorAndPenalty.add(new JLabel("Current Color: "+currGameState.currColor));
        colorAndPenalty.add(new JLabel("Outstanding Penalty: "+currGameState.currPenalty+" cards"));

        gameStatePanel.add(colorAndPenalty);

        boardPanel.add(gameStatePanel);
    }

    private void setPlayerActionBtns() {
        playerActionBtns = new JPanel();
        playerActionBtns.setLayout(new FlowLayout());

        JButton drawCardBtn = new JButton("Draw card");
        JButton revealCardsBtn = new JButton("Reveal cards");
        JButton hideCardsBtn = new JButton("Hide cards");

        drawCardBtn.addActionListener(this);
        revealCardsBtn.addActionListener(this);
        hideCardsBtn.addActionListener(this);

        playerActionBtns.add(drawCardBtn);
        playerActionBtns.add(revealCardsBtn);
        playerActionBtns.add(hideCardsBtn);

        boardPanel.add(playerActionBtns);
    }

    private void setPlayersCardsPanel() {
        playersHandBtns = new ArrayList<>();
        playersCards = new JPanel();
        playersCards.setLayout(new BoxLayout(playersCards, BoxLayout.PAGE_AXIS));
        createPlayersHandGUI();
    }

    private void createPlayersHandGUI() {
        int numInRow = 0;
        JPanel row = new JPanel(new FlowLayout());
        for (Card card: currGameState.getCurrPlayer().getHand()) {
            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.PAGE_AXIS));
            JButton cardBtn = new JButton(card.getId()+": "+card.toString());
            cardBtn.addActionListener(this);
            try {
                ImageIcon lastPlayedIcon = new ImageIcon(getClass().getResource("/card-images/"+card.getColor().toString().toLowerCase()+"/"+card.getValue().toString().toLowerCase()+".png"));
                Image lastPlayedImg = lastPlayedIcon.getImage().getScaledInstance(66, 100, Image.SCALE_SMOOTH);
                cardBtn.setIcon(new ImageIcon(lastPlayedImg));
            } catch (Exception e) {
                System.out.println("Failed card: "+card.toString());
                System.out.println("Failed Path: /card-images/"+card.getColor().toString().toLowerCase()+"/"+card.getValue().toString().toLowerCase()+".png");
                e.printStackTrace();
            }
            cardPanel.add(cardBtn);
            row.add(cardPanel);
            numInRow++;
            if (numInRow == 6) {
                playersCards.add(row);
                row = new JPanel(new FlowLayout());
                numInRow = 0;
            }
            playersHandBtns.add(cardBtn);
        }
        playersCards.add(row);
    }

    private void setPlayerNameLabel() {
        JPanel playerNamePanel = new JPanel(new FlowLayout());
        playerNamePanel.add(new JLabel(currGameState.getCurrPlayer().getPlayerName()+"'s Turn"));
        boardPanel.add(playerNamePanel);
    }

    private void rebuildGameBoard() throws IOException {
        boardPanel = new JPanel();
        btnTest = new JLabel();

        boardPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.PAGE_AXIS));


        setGameStatePanel();
        setPlayerNameLabel();
        setPlayersCardsPanel();
        if (currGameState.getCurrPlayer().getIsAI()) {
            JPanel aiPanel = new JPanel(new FlowLayout());
            JButton continueBtn = new JButton("Press to let AI play.");
            continueBtn.setActionCommand("continue");
            continueBtn.addActionListener(this);
            aiPanel.add(continueBtn);
            boardPanel.add(aiPanel);
        } else {
            setPlayerActionBtns();
        }
        JPanel turnFeedback = new JPanel(new FlowLayout());
        turnFeedback.add(btnTest);
        boardPanel.add(turnFeedback);

        frame.add(boardPanel);
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
        if (e.getActionCommand().equals("Reveal cards")) {
            boardPanel.add(playersCards, 2);
            frame.pack();
        } else if (e.getActionCommand().equals("Hide cards")) {
            boardPanel.remove(playersCards);
            frame.pack();
        } else if (e.getActionCommand().equals("Skip")) {
            turnType = e.getActionCommand().toLowerCase();
            madeSelection = true;
        } else if (e.getActionCommand().equals("Draw card")) {
            turnType = e.getActionCommand().toLowerCase();
            System.out.println(turnType+" should be here");
            madeSelection = true;
        } else if (e.getActionCommand().equalsIgnoreCase("continue")) {
            madeSelection = true;
        } else {
            userSelectsACard(e.getActionCommand());
            return;
        }
        btnTest.setText(e.getActionCommand());
    }

    private void userSelectsACard(String cardSelectedStr) {
        int idSelected;
        if (cardSelectedStr.charAt(2) == ':') {
            idSelected = Integer.parseInt(cardSelectedStr.substring(0,2));
        } else if (cardSelectedStr.charAt(3) == ':') {
            idSelected = Integer.parseInt(cardSelectedStr.substring(0,3));
        } else {
            idSelected = Integer.parseInt(cardSelectedStr.substring(0,1));
        }

        System.out.println(cardSelectedStr+" --- ["+idSelected+"]");

        for (Card card: currGameState.getCurrPlayer().getHand()) {
            if (card.getId() == idSelected) {
                turnType = "select card";
                cardSelected = card;
                madeSelection = true;
                btnTest.setText("Playing:"+cardSelectedStr.substring(cardSelectedStr.indexOf(':')+1));
                break;
            }
        }
    }
}
