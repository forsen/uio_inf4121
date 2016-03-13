package hangman;

import java.util.Random;
import java.util.Scanner;

/**
 * @author Unknown
 */
public class Game {
    // List of guessable words
    private static final String[] wordForGuesing = {"computer", "programmer",
            "software", "debugger", "compiler", "developer", "algorithm",
            "array", "method", "variable"};

    private String guessWord;

    private StringBuffer dashedWord;
    private FileReadWriter filerw;
    private int mistakes;
    private boolean isHelpUsed;

    /**
     * @param autoStart Starts the game if true
     */
    public Game(boolean autoStart) {
        isHelpUsed = false;
        mistakes = 0;
        guessWord = getRandWord();
        dashedWord = getW(guessWord);
        filerw = new FileReadWriter();
        if (autoStart) {
            displayMenu();
        }
    }

    private String getRandWord() {
        Random rand = new Random();
        return wordForGuesing[rand.nextInt(9)];
    }

    /**
     * Starts the game and displays the menu
     */
    public void displayMenu() {
        System.out
                .println("Welcome to �Hangman� game. Please, try to guess my secret word.\n"
                        + "Use 'TOP' to view the top scoreboard, 'RESTART' to start a new game,"
                        + "'HELP' to cheat and 'EXIT' to quit the game.");

        StringBuffer dashBuff = new StringBuffer(dashedWord);
        do {
            findLetterAndPrintIt(dashBuff);
        }while(!dashBuff.toString().equals(guessWord));


        // when the correct word is guessed you'll get the score and information whether help is used or not. If not, you'll be able to sign up for highscore list
        if (!isHelpUsed) {
            System.out.println("You won with " + mistakes + " mistake(s).");
            System.out.println("The secret word is: " + printDashes(dashBuff));

            System.out
                    .println("Please enter your name for the top scoreboard:");
            Scanner input = new Scanner(System.in);
            String playerName = input.next();

            filerw.addRecords(mistakes, playerName);
            filerw.readRecords();
            filerw.printAndSortScoreBoard();
        } else {
            System.out.println("You won with "
                    + mistakes
                    + " mistake(s). but you have cheated. You are not allowed to enter into the scoreboard.");
            System.out.println("The secret word is: " + printDashes(dashBuff));
        }

        new Game(true);
    }

    // collect input and act correctly on the given input (act on menu, correct or incorrect letter)
    private void findLetterAndPrintIt(StringBuffer dashBuff) {
        String letter;

        System.out.println("The secret word is: " + printDashes(dashBuff));
        System.out.println("DEBUG " + guessWord);
        System.out.println("Enter your gues(1 letter alowed): ");

        Scanner input = new Scanner(System.in);
        letter = input.next();

        // checks for single letter within the english alphabet
        if(letter.matches("[a-z]"))
            checkGuessing(letter,dashBuff);
        else {
            menu(letter,dashBuff);
        }
    }

    private void checkGuessing(String letter, StringBuffer dashBuff) {
        int counter = 0;

        // Check if input is a correct letter, and display all occurrences of the correct input
        for (int i = 0; i < guessWord.length(); i++) {
            String currentLetter = Character.toString(guessWord.charAt(i));
            if (letter.equals(currentLetter)) {
                ++counter;
                dashBuff.setCharAt(i, letter.charAt(0));
            }
        }

        // if counter == 0, the letter is not part of the guessword
        if (counter == 0) {
            ++mistakes;
            System.out.printf("Sorry! There are no unrevealed letters \'%s\'. \n", letter);
        } else {
            System.out.printf("Good job! You revealed %d letter(s).\n", counter);
        }
    }

    // All input with length > 1 is passed to this method.
    private void menu(String letter, StringBuffer dashBuff) {
        Command cmd;

        // If input does not match Command enums, an "unknown input, try again" message is written to System Out
        try{
            cmd = Command.valueOf(letter);
        } catch (IllegalArgumentException iae){
            System.out.println("Unknown input, try again");
            return;
        }

        switch(cmd){
            case restart:
                new Game(true);
                break;
            case top:
                filerw.readRecords();
                filerw.printAndSortScoreBoard();
                new Game(true);
                break;
            case exit:
                System.exit(0);
                break;
            case help:
                help(dashBuff);
                break;
        }
    }

    // Finds the next unsolved letter in the guessword and solves it
    private void help(StringBuffer dashBuff){
        isHelpUsed = true;
        int i = 0, j = 0;
        while (j < 1) {
            if (dashBuff.charAt(i) == '_') {
                dashBuff.setCharAt(i, guessWord.charAt(i));
                ++j;
            }
            ++i;
        }
    }

    // Initialises the empty word (all dashes before any correct guess is made)
    private StringBuffer getW(String word) {
        StringBuffer dashes = new StringBuffer("");
        for (int i = 0; i < word.length(); i++) {
            dashes.append("_");
        }
        return dashes;
    }

    // Writes out the current state of the game (the _ for unguessed letters, and the actual letters for the correct input)
    private String printDashes(StringBuffer word) {
        String toDashes = "";

        for (int i = 0; i < word.length(); i++) {
            toDashes += (" " + word.charAt(i));
        }
        return toDashes;
    }
}
