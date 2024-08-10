package model.exceptions;

// Represents an exception should be thrown when the entered approx length for the generated prompt is less then the
// set minimum
public class InvalidApproxLengthException extends SettingsException {
    public InvalidApproxLengthException() {
        super();
    }
}
