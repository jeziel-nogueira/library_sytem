package org.v2com.exceptions;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(String loanId) {
        super("Loan Not Found. ID: " + loanId);
    }
}
