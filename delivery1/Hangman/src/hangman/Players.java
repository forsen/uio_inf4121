package hangman;

import java.io.Serializable;

/**
 * Player for the highscore list
 */
public class Players implements Serializable {

    private String name;
    private int scores;

    /**
     * Constructed with name and score of player
     * @param name Name of the player
     * @param scores Number of mistakes
     */
    public Players(String name, int scores) {
        this.name = name;
        this.scores = scores;
    }

    /**
     *
     * @return name of player
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return player's score
     */
    public int getScores() {
        return scores;
    }

}
