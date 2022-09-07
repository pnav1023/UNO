package uno.engine.players;

import java.util.ArrayList;

/**
 * HumanPlayer -- extends Player to specifically create
 * players that input their selections via the GUI. In other words, all
 * actual players playing are created at HumanPlayer's.
 * @author Pranav Narahari
 */
public class HumanPlayer extends Player {
    /**
     * Initializes Human player.
     * @param name Player's name.
     */
    public HumanPlayer(String name) {
        this.playerName = name;
        hand = new ArrayList<>();
    }
}
