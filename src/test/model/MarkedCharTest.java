package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class MarkedCharTest {

    @Test
    public void testContructor() {
        MarkedChar markedChar = new MarkedChar('a', CharType.UNMARKED);
        assertEquals('a', markedChar.getCharacter());
        assertEquals(CharType.UNMARKED, markedChar.getCharType());
    }

    @Test
    public void testEquals() {
        MarkedChar markedChar = new MarkedChar('a', CharType.UNMARKED);
        MarkedChar markedChar1 = new MarkedChar('a', CharType.UNMARKED);
        MarkedChar markedChar2 = new MarkedChar('A', CharType.UNMARKED);
        MarkedChar markedChar3 = new MarkedChar('A', CharType.CORRECT);
        assertTrue(markedChar1.equals(markedChar));
        assertFalse(markedChar1.equals(markedChar2));
        assertFalse(markedChar3.equals(markedChar2));
        assertEquals(0, markedChar1.hashCode());
    }
}
