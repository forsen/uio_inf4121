package hangman;

/**
 * This is the supported commands for the menu functionality.
 * @author  Unknown
 */
public enum Command {
    /**
     * restarts the game
     */
    restart,
    /**
     * shows the highscore list
     */
    top,
    /**
     * exits the program
     */
    exit,
    /**
     * solve the next unsolved letter in the guessable word
     */
    help
}
