package org.v2com.exceptions;

public class BookUnavailableToLoanException extends RuntimeException {
    public BookUnavailableToLoanException(String bookId) {super("Unavailable Book. ID: " + bookId);}
}
