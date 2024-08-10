package model;

import model.exceptions.InvalidApproxLengthException;
import model.exceptions.InvalidBooleanSelectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsTest {

    private Settings settings;

    @BeforeEach
    public void beforeEach() {
        settings = new Settings();
    }

    @Test
    public void testConstructor() {
        assertFalse(settings.getAreSentencesEnabled());
        assertTrue(settings.getAreWordsEnabled());
        assertFalse(settings.getAreNumbersEnabled());
        assertEquals(50, settings.getApproxLength());
        assertFalse(settings.getIsAutomaticSaveEnabled());
    }

    @Test
    public void testSetters() throws InvalidBooleanSelectionException {
        try {
            settings.setApproxLength(4123);
        } catch (InvalidApproxLengthException invalidApproxLengthException) {
            fail("InvalidApproxLengthException not supposed to be thrown");
        }
        settings.setAreWordsEnabled(false);
        settings.setAreNumbersEnabled(false);
        settings.setAreSentencesEnabled(true);

        assertEquals(4123, settings.getApproxLength());
        assertEquals(false, settings.getAreNumbersEnabled());
        assertEquals(false, settings.getAreWordsEnabled());
        assertEquals(true, settings.getAreSentencesEnabled());

        settings.setAreWordsEnabled(true);
        settings.setAreSentencesEnabled(false);
        assertEquals(true, settings.getAreWordsEnabled());
        assertEquals(false, settings.getAreSentencesEnabled());
    }

    @Test
    public void testToString() throws InvalidBooleanSelectionException {
        try {
            settings.setApproxLength(42342);
        } catch (InvalidApproxLengthException invalidApproxLengthException) {
            fail("InvalidApproxLengthException not supposed to be thrown");
        }
        settings.setAreWordsEnabled(true);
        settings.setAreNumbersEnabled(false);
        settings.setAreSentencesEnabled(true);
        String settingsString = "Are sentences enabled: " + true + "\n"
                + "Are words enabled: " + true + "\n"
                + "Are numbers enabled: " + false + "\n"
                + "Is automatic saving enabled: " + false + "\n"
                + "Approx length to generate: " + 42342 + "\n";
        assertEquals(settingsString, settings.toString());
    }

    @Test
    public void testInvalidApproxLength() {
        try {
            settings.setApproxLength(0);
            fail("InvalidApproxLengthException not thrown");
        } catch (InvalidApproxLengthException invalidApproxLengthException) {
            //pass
        }
    }

    @Test
    public void testContentEquals() throws InvalidApproxLengthException {
        Settings settings2 = new Settings();
        assertTrue(settings.contentEquals(settings2));
        assertTrue(settings2.contentEquals(settings));

        settings.setApproxLength(234543);
        assertFalse(settings.contentEquals(settings2));
        assertFalse(settings2.contentEquals(settings));
    }

    @Test
    public void testInvalidBooleanSelectionException() {
        settings.setAreWordsEnabled(false);
        settings.setAreNumbersEnabled(false);
        settings.setAreSentencesEnabled(false);
        try {
            settings.checkForInvalidBooleanSelection();
            fail("InvalidBooleanSelectionException should have been thrown!");
        } catch (InvalidBooleanSelectionException e) {
            //pass
        }
    }

    @Test
    public void testNoInvalidBooleanSelectionException() {
        try {
            settings.setAreWordsEnabled(true);
            settings.setAreNumbersEnabled(true);
            settings.setAreSentencesEnabled(true);
            settings.checkForInvalidBooleanSelection();
            settings.setAreWordsEnabled(true);
            settings.setAreNumbersEnabled(false);
            settings.setAreSentencesEnabled(false);
            settings.checkForInvalidBooleanSelection();
            settings.setAreWordsEnabled(false);
            settings.setAreNumbersEnabled(true);
            settings.setAreSentencesEnabled(false);
            settings.checkForInvalidBooleanSelection();
            settings.setAreWordsEnabled(false);
            settings.setAreNumbersEnabled(false);
            settings.setAreSentencesEnabled(true);
            settings.checkForInvalidBooleanSelection();
        } catch (InvalidBooleanSelectionException e) {
            fail("InavlidBooleanSelectionException should not be thrown!");
        }
    }
}
