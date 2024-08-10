package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

public class UserResultsTest {

    private UserResults userResults;
    private long startTimeL;
    private long endTimeL;
    private Prompt prompt1;

    @BeforeEach
    public void beforeEach() throws FileNotFoundException {
        prompt1 = new Prompt(true, true, true, 50);
        startTimeL = System.currentTimeMillis();
        userResults = new UserResults();
    }

    @Test
    public void testConstructor() {
        assertEquals(startTimeL, userResults.getStartTime().getTime());
    }

    @Test
    public void testGetTestDuration() throws InterruptedException {
        Thread.sleep(200);
        prompt1.setPrompt("no dude");
        userResults.updateUserResults(prompt1, "hi");
        endTimeL = System.currentTimeMillis();
        boolean diffLessThan5 = ((int) (endTimeL - startTimeL) - userResults.getTestDuration() < 5);
        assertTrue(diffLessThan5);
    }

    @Test
    public void testToString() throws InterruptedException {
        Thread.sleep(200);
        prompt1.setPrompt("hi There this is a tesT.");
        userResults.updateUserResults(prompt1, "hi there this is a test.");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        sdf.setTimeZone(TimeZone.getTimeZone("PST"));

        assertEquals("User Input: hi there this is a test.\n" +
                "Accuracy: 92%\n" +
                "Words Per Minute: " + userResults.getWpm() + "\n" +
                "Start Time: " + sdf.format(userResults.getStartTime()) + " PST\n" +
                "End Time: " + sdf.format(userResults.getEndTime()) + " PST\n" +
                "Duration (seconds): 0", userResults.toString());
    }

    @Test
    public void testContentEquals() throws FileNotFoundException {
        prompt1 = new Prompt(false, true, true, 50);
        prompt1.setPrompt("ketchup is good!!!!!!!!");
        userResults = new UserResults();
        userResults.updateUserResults(prompt1, "ketchup is not good");

        Prompt prompt2 = new Prompt(false, true, true, 50);
        prompt2.setPrompt("ketchup is good!!!!!!!!!!");
        UserResults userResults2 = new UserResults();
        userResults2.updateUserResults(prompt2, "ketchup is not good");

        assertTrue(userResults.contentEquals(userResults2));
        assertTrue(userResults2.contentEquals(userResults));

        prompt2 = new Prompt(false, true, true, 50);
        prompt2.setPrompt("ketchup is not good");
        userResults2 = new UserResults();
        userResults2.updateUserResults(prompt2, "ketchup is not good");

        assertFalse(userResults.contentEquals(userResults2));
        assertFalse(userResults2.contentEquals(userResults));
    }
}
