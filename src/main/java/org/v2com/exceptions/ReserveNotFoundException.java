package org.v2com.exceptions;

public class ReserveNotFoundException extends RuntimeException {
    public ReserveNotFoundException(String reserveId) {
        super("Reserve Not Found. ID: " + reserveId);
    }
}
