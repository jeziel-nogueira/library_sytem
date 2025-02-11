package org.v2com.exceptions;

public class BookUnavailableToReserveException extends RuntimeException {
    public BookUnavailableToReserveException(String bookId) {
        super("Unavailable Reserve Book. ID: " + bookId);
    }
}
