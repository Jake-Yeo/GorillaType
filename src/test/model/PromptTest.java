package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PromptTest {
    private Prompt prompt1;
    private Prompt prompt2;

    @BeforeEach
    public void beforeEach() {
        prompt1 = new Prompt(true, true, true, 50);
        prompt2 = new Prompt(true, false, true, 51);
    }

    @Test
    public void testConstructor() {
        assertTrue(prompt1.getAreNumbersEnabled());
        assertTrue(prompt1.getAreSentencesEnabled());
        assertTrue(prompt1.getAreWordsEnabled());
        assertEquals("", prompt1.getPrompt());
        assertEquals(50, prompt1.getApproxLength());

        assertTrue(prompt2.getAreNumbersEnabled());
        assertTrue(prompt2.getAreSentencesEnabled());
        assertFalse(prompt2.getAreWordsEnabled());
        assertEquals("", prompt2.getPrompt());
        assertEquals(51, prompt2.getApproxLength());
    }

    @Test
    public void testToString() {
        assertEquals("Prompt: \n" +
                "Are Sentences Enabled: true\n" +
                "Are Words Enabled: true\n" +
                "Are Numbers Enabled: true\n" +
                "Approx Length: 50\n" +
                "Actual Length: 0", prompt1.toString());
    }

    @Test
    public void testGetFinalValue() {
        assertEquals(50, Prompt.MIN_PROMPT_SIZE);
    }

    @Test
    public void testContentEquals() {
        prompt1 = new Prompt(false, false, true, 52);
        prompt2 = new Prompt(false, false, true, 52);
        prompt1.setPrompt("hello!");
        prompt2.setPrompt("hello!");
        assertTrue(prompt1.contentEquals(prompt2));
        assertTrue(prompt2.contentEquals(prompt1));

        prompt2.setPrompt("jello");
        assertFalse(prompt1.contentEquals(prompt2));
        assertFalse(prompt2.contentEquals(prompt1));
    }
}