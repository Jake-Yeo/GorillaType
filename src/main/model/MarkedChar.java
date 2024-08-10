package model;

// Represents three types of colored chars.
public class MarkedChar {

    private CharType charType;
    private char character;

    // Effects: Creates a new MarkedChar
    public MarkedChar(char character, CharType charType) {
        this.character = character;
        this.charType = charType;
    }

    @Override
    public boolean equals(Object o) {
        MarkedChar that = (MarkedChar) o;

        if (character != that.character) {
            return false;
        }
        return charType == that.charType;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public CharType getCharType() {
        return charType;
    }

    public char getCharacter() {
        return character;
    }
}
