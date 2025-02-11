package org.v2com.exceptions;

public class DataPersistException extends RuntimeException {
    public DataPersistException( ) {
        super("Erro ao persistir.");
    }
}
