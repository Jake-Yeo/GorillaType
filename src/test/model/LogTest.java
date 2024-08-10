package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

public class LogTest {

    private Log log;
    private Prompt prompt;
    private UserResults userResults;

    @BeforeEach
    public void beforeEach() throws FileNotFoundException {
        prompt = new Prompt(true, false, true, 51);
        userResults = new UserResults();
        prompt.setPrompt("hi");
        userResults.updateUserResults(prompt, "hi");
        log = new Log(prompt, userResults);
    }

    @Test
    public void testConstructor() {
        assertEquals(prompt, log.getPrompt());
        assertEquals(userResults, log.getUserResults());
    }

    @Test
    public void testToString() {
        assertEquals(prompt.toString() + "\n\n" + userResults.toString(), log.toString());
    }

    @Test
    public void testContentEquals() throws FileNotFoundException {
        prompt = new Prompt(true, true, true, 51);
        prompt.setPrompt("im get rich boller");
        userResults = new UserResults();
        userResults.updateUserResults(prompt, "im get rich boller");
        userResults.setEndTime(new Time(23423L));
        userResults.setStartTime(new Time(123L));
        log = new Log(prompt, userResults);

        Prompt prompt2 = new Prompt(true, true, true, 51);
        prompt2.setPrompt("im get rich boller");
        UserResults userResults2 = new UserResults();
        userResults2.updateUserResults(prompt2, "im get rich boller");
        userResults2.setEndTime(new Time(23423L));
        userResults2.setStartTime(new Time(123L));
        Log log2 = new Log(prompt2, userResults2);

        assertTrue(log.contentEquals(log2));
        assertTrue(log2.contentEquals(log));
        prompt.setPrompt("jaoiefjioawefjioaewjfio");
        assertFalse(log.contentEquals(log2));
        assertFalse(log2.contentEquals(log));
    }
}
