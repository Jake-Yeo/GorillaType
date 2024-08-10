package model.exceptions;

// Represents an exception that should be thrown when the user tries to login with wrong credentials
public class UserOrPassWrongException extends SignupLoginException {
    public UserOrPassWrongException() {
        super();
    }
}
