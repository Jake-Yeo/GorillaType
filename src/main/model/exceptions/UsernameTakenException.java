package model.exceptions;

// Represents an exception that should be thrown if the user tries to signup with a taken username
public class UsernameTakenException extends SignupLoginException {

    public UsernameTakenException() {
        super();
    }
}
