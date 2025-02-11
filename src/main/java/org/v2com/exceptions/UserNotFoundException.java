package org.v2com.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("User Not Found, ID: " + userId);
    }
}
