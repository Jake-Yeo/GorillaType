package model.exceptions;

// Represents an exception that should be thrown when the password length is shorter than the set minimum
public class PasswordTooShortException extends SignupLoginException {
    public PasswordTooShortException() {
        super();
    }
}
