package model.exceptions;

// Represents an exception that should be thrown when the username the user tries to signup with is
// shorter than the set minimum
public class UsernameTooShortException extends SignupLoginException {
    public UsernameTooShortException() {
        super();
    }
}
