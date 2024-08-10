package model;

import model.exceptions.InvalidApproxLengthException;
import model.exceptions.InvalidBooleanSelectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TypingTestUtilsTest {

    private Prompt prompt;
    private UserResults userResults;
    private final String RED = "\u001B[31m";
    private final String GREEN = "\u001B[32m";
    public static final String END = "\u001B[0m";
    private TypingTestUtils typingTestUtils;

    @BeforeEach
    public void beforeEach() throws FileNotFoundException {
        prompt = new Prompt(true, true, true, 50);
        prompt.setPrompt("hi there");
        userResults = new UserResults();
        userResults.updateUserResults(prompt, "hi there");
        typingTestUtils = new TypingTestUtils(true);
    }

    @Test
    public void testCalculateAccuracy() throws FileNotFoundException {
        prompt.setPrompt("zzzzzzzz");
        assertEquals(0, typingTestUtils.calculateAccuracy(prompt, userResults));
        prompt.setPrompt("hi there");
        assertEquals(100, typingTestUtils.calculateAccuracy(prompt, userResults));
        prompt.setPrompt("Hi There");
        assertEquals(Math.round((6.0/8.0) * 100.0), typingTestUtils.calculateAccuracy(prompt, userResults));
        String length100String = "";
        for (int i = 0; i < 100; i++) {
            prompt.setPrompt(prompt.getPrompt() + "hh");
            length100String += "h";
        }
        userResults = new UserResults();
        userResults.updateUserResults(prompt, length100String);
        prompt.setPrompt(length100String.substring(1) + "f");
        assertEquals(99, typingTestUtils.calculateAccuracy(prompt, userResults));
    }

    @Test
    public void testCalculateWpm() throws InterruptedException, FileNotFoundException {
        userResults = new UserResults();
        Thread.sleep(500); // 0.5 seconds to simulate typing time
        userResults.updateUserResults(prompt, "");
        assertEquals(0, typingTestUtils.calculateWpm(userResults));
        String length100String = "";
        for (int i = 0; i < 100; i++) {
            prompt.setPrompt(prompt.getPrompt() + "hh");
            length100String += "h";
        }
        userResults = new UserResults();
        Thread.sleep(500); // 0.5 seconds to simulate typing time
        userResults.updateUserResults(prompt, length100String);
        int calculatedWpm = typingTestUtils.calculateWpm(userResults);
        //Use a range because the time gives us random wpm within a certain range
        assertTrue(calculatedWpm > 2300 && calculatedWpm < 2500);
    }

    private boolean checkGivenMarkedCharArraysEqual(MarkedChar[] manualCheck, ArrayList<MarkedChar> autoCheck) {
        Object[] autoCheckArray =  autoCheck.toArray();
        boolean isEqual = true;
        for (int i = 0; i < manualCheck.length; i++) {
            if (!manualCheck[i].equals(autoCheckArray[i])) {
                isEqual = false;
            }
        }
        return isEqual;
    }

    @Test
    public void testGenerateErrorLocations() throws FileNotFoundException {
        prompt.setPrompt("zzz");
        userResults = new UserResults();
        userResults.updateUserResults(prompt, "hit");

        prompt.setPrompt("HiT");
        MarkedChar[] markedChars = {new MarkedChar('H', CharType.WRONG),
                new MarkedChar('i', CharType.CORRECT),
                new MarkedChar('T', CharType.WRONG)};
        assertTrue(checkGivenMarkedCharArraysEqual(markedChars,
                typingTestUtils.generateErrorLocations(userResults, prompt)));

        markedChars = new MarkedChar[]{new MarkedChar('h', CharType.CORRECT),
                new MarkedChar('i', CharType.CORRECT),
                new MarkedChar('t', CharType.CORRECT)};
        prompt.setPrompt("hit");
        assertTrue(checkGivenMarkedCharArraysEqual(markedChars,
                typingTestUtils.generateErrorLocations(userResults, prompt)));

        prompt.setPrompt("HIT");
        markedChars = new MarkedChar[]{new MarkedChar('H', CharType.WRONG),
                new MarkedChar('I', CharType.WRONG),
                new MarkedChar('T', CharType.WRONG)};
        assertTrue(checkGivenMarkedCharArraysEqual(markedChars,
                typingTestUtils.generateErrorLocations(userResults, prompt)));

        prompt.setPrompt("HiTTER");
        markedChars = new MarkedChar[]{new MarkedChar('H', CharType.WRONG),
                new MarkedChar('i', CharType.CORRECT),
                new MarkedChar('T', CharType.WRONG),
                new MarkedChar('T', CharType.UNMARKED),
                new MarkedChar('E', CharType.UNMARKED),
                new MarkedChar('R', CharType.UNMARKED)};
        assertTrue(checkGivenMarkedCharArraysEqual(markedChars,
                typingTestUtils.generateErrorLocations(userResults, prompt)));
    }

    @Test
    public void testGeneratePrompt() {
        Settings settings = new Settings();
        settings.setAreWordsEnabled(false);
        settings.setAreSentencesEnabled(false);
        settings.setAreNumbersEnabled(true);
        try {
            settings.setApproxLength(5000);
        } catch (InvalidApproxLengthException e) {
            fail("InvalidApproxLengthException not supposed to bre thrown");
        }
        boolean containsNumber = false;
        try {
            for (char character: typingTestUtils.generatePrompt(settings).getPrompt().toCharArray()) {
                if (Character.isDigit(character)) {
                    containsNumber = true;
                }
            }
        } catch (IOException e) {
            fail("IOException not supposed to be thrown");
        }
        assertTrue(containsNumber);
        settings.setAreWordsEnabled(true);
        settings.setAreSentencesEnabled(false);
        settings.setAreNumbersEnabled(false);
        containsNumber = false;
        try {
            for (char character: typingTestUtils.generatePrompt(settings).getPrompt().toCharArray()) {
                if (Character.isDigit(character)) {
                    containsNumber = true;
                    break;
                }
            }
        } catch (IOException e) {
            fail("IOException not supposed to be thrown");
        }
        try {
            assertTrue(typingTestUtils.generatePrompt(settings).getPrompt().length()
                    >= settings.getApproxLength());
        } catch (IOException e) {
            fail("IOException not supposed to be thrown");
        }
        assertFalse(containsNumber);
        settings.setAreWordsEnabled(false);
        settings.setAreSentencesEnabled(true);
        try {
            assertTrue(typingTestUtils.generatePrompt(settings).getPrompt().length()
                    >= settings.getApproxLength());
        } catch (IOException e) {
            fail("IOException not supposed to be thrown");
        }
    }
}
