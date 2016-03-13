package hangman;

import java.io.*;
import java.util.ArrayList;

/**
 * File read/writer for the highscore list for Hangman
 * @author Unknown
 */
public class FileReadWriter {
    private ObjectOutputStream output;
    private ObjectInputStream input;
    ArrayList<Players> myArr = new ArrayList<>();
    private final String fileName = "players.ser";

    /**
     * Adds palyers name and score to the highscore file
     * @param scores Number of mistakes
     * @param name Name of player
     */
    public void addRecords(int scores, String name) {
        Players players = new Players(name, scores); // object to be written to

        // if datafile does not exist, use ObjectOutputStream, else use the private class AppendingObjectOutputStream (see notes in class description)
        File dataFile = new File(fileName);
        if(!dataFile.exists()) {
            try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(dataFile, true))) {
                output.writeObject(players);
            } catch (IOException ioException) {
                System.err.println("Error writing to file.");
            }
        } else {
            try (ObjectOutputStream output = new AppendingObjectOutputStream(new FileOutputStream(dataFile, true))) {
                output.writeObject(players);
            } catch (IOException ioException) {
                System.err.println("Error writing to file.");
            }
        }
    }

    /**
     * Read records and write the to System Out.
     */
    public void readRecords() {
        Players records;
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName)))
        {
            while (true) {
                records = (Players) input.readObject();
                myArr.add(records);
                System.out.printf("DEBUG: %-10d%-12s\n", records.getScores(), records.getName());
            }

        } // end try
        catch (EOFException endOfFileException) {
            // expected exception to break while true loop
        } catch (ClassNotFoundException classNotFoundException) {
            System.err.println("Unable to create object.");
        } catch (IOException ioException) {
            System.err.println("Unable to read file.");
        }
    }

    /**
     * Sort scoreboard (highscore) and print the result to System Out
     */
    public void printAndSortScoreBoard() {
        Players temp;
        int n = myArr.size();
        for (int pass = 1; pass < n; pass++) {
            for (int i = 0; i < n - pass; i++) {
                if (myArr.get(i).getScores() > myArr.get(i + 1).getScores()) {

                    temp = myArr.get(i);
                    myArr.set(i, myArr.get(i + 1));
                    myArr.set(i + 1, temp);
                }
            }
        }

        System.out.println("Scoreboard:");
        for (int i = 0; i < myArr.size(); i++) {
            System.out.printf("%d. %s ----> %d", i, myArr.get(i).getName(), myArr.get(i).getScores());
        }
    }

    /*
        ObjectOutputStream can not be used to append to a file, as it write a new header for each new instance. By creating this subclass we make sure
        no header is written. This one should only be used when the file already exists, as the first write to a new file should include headers.
     */

    private class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException{
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException{
            reset();
        }
    }
}
