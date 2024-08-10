package model.exceptions;

// Represents an exception should be thrown when all the booleans which controls the prompt generation are set to false
public class InvalidBooleanSelectionException extends SettingsException {
    public InvalidBooleanSelectionException() {
        super();
    }
}
