package org.v2com.providers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandlerProvider implements ExceptionMapper {

    @Override
    public Response toResponse(Throwable throwable) {
        if(throwable instanceof EntityNotFoundException){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Resource error", throwable.getMessage()))
                    .build();
        }else if (throwable instanceof SecurityException){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Service error", throwable.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Unexpected error", throwable.getMessage()))
                .build();
    }

    public static class ErrorResponse{
        private final String error;
        private final String details;

        public ErrorResponse(String error, String details){
            this.error = error;
            this.details = details;
        }

        public String getError(){
            return error;
        }

        public String getDetails(){
            return details;
        }
    }
}
