package org.v2com.providers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.v2com.exceptions.*;

import java.util.Set;

@Provider
public class GlobalExceptionHandlerProvider implements ExceptionMapper<Throwable> {

    private static final Set<Class<?>> NOT_FOUND_EXCEPTIONS = Set.of(
            BookNotFoundException.class,
            LoanNotFoundException.class,
            UserNotFoundException.class,
            ReserveNotFoundException.class,
            BookUnavailableToReserveException.class,
            BookUnavailableToLoanException.class,
            UserInactiveException.class,
            EntityNotFoundException.class
    );

    @Override
    public Response toResponse(Throwable throwable) {
        if (NOT_FOUND_EXCEPTIONS.contains(throwable.getClass())) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("NOT_FOUND", throwable.getMessage()))
                    .build();
        }

        if (throwable instanceof SecurityException) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse("ACCESS_DENIED", throwable.getMessage()))
                    .build();
        }

        if(throwable instanceof NotFoundException){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("NOT_FOUND", throwable.getMessage()))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("INTERNAL_ERROR", throwable.getMessage()))
                .build();
    }

    public static class ErrorResponse {
        private final String error;
        private final String details;

        public ErrorResponse(String error, String details) {
            this.error = error;
            this.details = details;
        }

        public String getError() {
            return error;
        }

        public String getDetails() {
            return details;
        }
    }
}
