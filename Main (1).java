import java.io.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        Scanner scan = new Scanner(System.in);
        System.out.println("Type NEW for a new game, or CONTINUE to restore an old game.");
        String newContinue = scan.nextLine();
        if(newContinue.equals("NEW")){
            WordJumble wordJumble = new WordJumble();
            playGame(wordJumble);
        }
        else if(newContinue.equals("CONTINUE")){
            playGame(restoreGame());
        }
    }

    public static void playGame (WordJumble wordjumble) throws IOException {
        String enterQuitSave;
        String save = "";
        Scanner scan = new Scanner(System.in);
        System.out.println("Letters: " + wordjumble.getLetters());
        System.out.println("Found Words: " + wordjumble.getGuessedWordsAsString());
        System.out.println("Current Score: " + wordjumble.getScore());
        System.out.println("Enter a word using those letters or QUIT to stop. SAVE to save progress.");
        enterQuitSave = scan.nextLine();
        while (!enterQuitSave.equals("QUIT")) {
            if(enterQuitSave.equals("SAVE")){
                saveGame(wordjumble.getLetters(),wordjumble.getGuessedWords());
                save = "SAVE";
            }
            enterQuitSave = enterQuitSave.toLowerCase();
            if (!wordjumble.hasLetters(enterQuitSave)) {
                if(!enterQuitSave.equals(save)) {
                    System.out.println("You used letters you don't have.");
                }
            } else if (!checkWord(enterQuitSave)) {
                if(!enterQuitSave.equals(save)) {
                    System.out.println("Not a valid word.");
                }
            } else if (wordjumble.alreadyGuessed(enterQuitSave)) {
                if(!enterQuitSave.equals(save)) {
                    System.out.println("Already guessed.");
                }
            } else {
                wordjumble.saveGuess(enterQuitSave);
                System.out.println("Valid Word. You score " + wordjumble.scoreWord(enterQuitSave.length()));
            }
            System.out.println("Letters: " + wordjumble.getLetters());
            System.out.println("Found Words: " + wordjumble.getGuessedWordsAsString());
            System.out.println("Current Score: " + wordjumble.getScore());
            System.out.println("Enter a word using those letters or QUIT to stop. SAVE to save progress.");
            enterQuitSave = scan.nextLine();
        }
    }



    public static boolean checkWord(String word) throws IOException {
        try{
            File textFile = new File("src/2of12inf.txt");
            Scanner fileScan = new Scanner(textFile);
            String line;
            while(fileScan.hasNext()) {
                line = fileScan.next();
                if (line.equals(word)) {
                    return true;
                }
            }
        }
        catch(IOException e){
            System.out.println("Error" + e);
        }
        return false;
    }


    public static void saveGame(String letters, ArrayList<String> guessedWords ) throws IOException{
        Scanner scan = new Scanner(System.in);
        System.out.println("Name your session.");
        String sessionName = scan.nextLine();
        sessionName = sessionName + ".txt";
        File file = new File(sessionName);
        if (file.createNewFile()) {
            PrintStream myWriter = new PrintStream(file);
            myWriter.println(letters);
            for(String word : guessedWords){
                myWriter.println(word);
            }
            System.out.println("File has been created.");
        } else {
            System.out.println("Unable to save state.");
        }
    }


    public static WordJumble restoreGame() throws IOException {
            Scanner scan = new Scanner(System.in);
            System.out.println("What game do you want to restore?");
            String filename = scan.nextLine();
            ArrayList<String> restore = new ArrayList<String>();
            String letters;
            BufferedReader savedGame = new BufferedReader(new FileReader(filename));
            letters = savedGame.readLine();
            String guessedWords;
            do {
                guessedWords = savedGame.readLine();
                if(guessedWords == null){
                    break;
                }
                restore.add(guessedWords);
            }
            while (guessedWords != null);
        return new WordJumble(letters,restore);
    }
}
