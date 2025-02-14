package org.v2com.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("No users found.");
    }
}
