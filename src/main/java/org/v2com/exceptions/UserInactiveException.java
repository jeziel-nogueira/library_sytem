package org.v2com.exceptions;

public class UserInactiveException extends RuntimeException {
    public UserInactiveException(String userId) {
        super("User Inactive, ID: " + userId);
    }
}
