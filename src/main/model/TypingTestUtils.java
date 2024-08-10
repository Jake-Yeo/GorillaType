package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// A class containing methods that will calculate wpm, start time, end time, generate error locations in typing,
// calculate accuracy etc.
public class TypingTestUtils {

    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String END = "\u001B[0m";

    private static final int RAND_INT_UPPER_BOUND = 999999;
    private static final int AMT_RAND_SETTINGS = 3;

    private List<String> wordsList;
    private List<String> sentencesList;

    // Effects: Takes in a boolean value and will initalize the words, sentences, and other lists if true. If not
    // the lists will be empty.
    public TypingTestUtils(boolean initializeLists) throws FileNotFoundException {
        wordsList = new ArrayList<>();
        sentencesList = new ArrayList<>();
        if (initializeLists) {
            Scanner scanner = new Scanner(new File("data/words.txt"));
            while (scanner.hasNextLine()) {
                wordsList.add(scanner.nextLine());
            }
            scanner = new Scanner(new File("data/sentences.txt"));
            while (scanner.hasNextLine()) {
                sentencesList.add(scanner.nextLine());
            }
            scanner.close();
        }
    }

    // Effects: Returns the char as a correct char if both given chars are the same, wrong otherwise
    public MarkedChar markGivenChar(char correctChar, char inputtedChar) {
        if (correctChar == inputtedChar) {
            return new MarkedChar(correctChar, CharType.CORRECT);
        } else {
            return new MarkedChar(correctChar, CharType.WRONG);
        }
    }

    // Effects: Returns the array of chars as an array of unmarked chars.
    public ArrayList<MarkedChar> markGivenChar(String incompletedChars) {
        ArrayList<MarkedChar> markedChars = new ArrayList<>();
        for (char character: incompletedChars.toCharArray()) {
            markedChars.add(new MarkedChar(character, CharType.UNMARKED));
        }
        return markedChars;
    }

    // Effects: Takes prompt and userResults and will generate an array of MarkedChars which indicates if the char
    // is wrong, correct, or unmarked and then returns that array.
    public ArrayList<MarkedChar> generateErrorLocations(UserResults userResults, Prompt prompt) {
        char[] promptArray = prompt.getPrompt().toCharArray();
        char[] userResultsArray = userResults.getUserInput().toCharArray();

        ArrayList<MarkedChar> markedChars = new ArrayList<>();

        for (int i = 0; i < promptArray.length; i++) {
            if (i < userResultsArray.length) {
                markedChars.add(markGivenChar(promptArray[i], userResultsArray[i]));
            } else {
                markedChars.addAll(markGivenChar(prompt.getPrompt().substring(i)));
                break;
            }
        }
        return markedChars;
    }

    // Requires userResults.getTestDuration() > 0
    // Modifies: userResults
    // Effects: Takes userResults object and calculates the words per minutes rounded to the nearest number
    // which it then returns
    public int calculateWpm(UserResults userResults) {
        double wpm = (double) userResults.getLength() / 5.0 / ((double) userResults.getTestDuration() / 60000.0);
        return (int) Math.round(wpm);
    }

    // Requires: 0 < prompt.getPrompt().length() < userResults.getUserInput().length()
    // Effects: Takes the prompt and the userResults and calculates the users typing accuracy as a percentage
    // rounded to the nearest number. Will compare only as many chars as the user has typed so far
    // It uses the formula length/5/min
    public int calculateAccuracy(Prompt prompt, UserResults userResults) {
        char[] promptArray =  prompt.getPrompt().toCharArray();
        char[] resultsArray = userResults.getUserInput().toCharArray();

        double totalCorrectChars = 0;

        for (int i = 0; i < resultsArray.length; i++) {
            if (promptArray[i] == resultsArray[i]) {
                totalCorrectChars++;
            }
        }
        long accuracy = Math.round((totalCorrectChars / resultsArray.length) * 100);
        return (int) accuracy;
    }

    // Effects: Will look at the Account settings given and generate a randomized prompt object.
    public Prompt generatePrompt(Settings settings) throws IOException {
        Random random = new Random();
        Prompt prompt = new Prompt(settings.getAreSentencesEnabled(), settings.getAreWordsEnabled(),
                settings.getAreNumbersEnabled(), settings.getApproxLength());
        String promptToUse = "";
        while (promptToUse.trim().length() < prompt.getApproxLength()) {
            int whichTypeToGenerate = random.nextInt(AMT_RAND_SETTINGS);
            if (whichTypeToGenerate == 0 && prompt.getAreNumbersEnabled()) {
                promptToUse += random.nextInt(RAND_INT_UPPER_BOUND) + " ";
            } else if (whichTypeToGenerate == 1 && prompt.getAreWordsEnabled()) {
                promptToUse += wordsList.get(random.nextInt(wordsList.size())) + " ";
            } else if (whichTypeToGenerate == 2 && prompt.getAreSentencesEnabled()) {
                promptToUse += sentencesList.get(random.nextInt(sentencesList.size())) + " ";
            }
        }
        prompt.setPrompt(promptToUse.trim());
        EventLog.getInstance().logEvent(new Event("New prompt generated"));
        return prompt;
    }
}
