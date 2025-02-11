package org.v2com.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {
        super("Book Not Found.");
    }
}
