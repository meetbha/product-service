package com.product.exception;

import com.product.util.Constants;
import com.product.util.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;

import java.util.Collections;

@Slf4j
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        log.error("Failed to process request with exception {}", exception.getMessage(), exception);
        ErrorResponse errorResponse;
        if (exception instanceof ProductNotFoundException || exception instanceof DuplicateDataFoundException) {
            errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(),
                    Constants.VALIDATION_FAILED, Collections.singletonList(exception.getMessage()));
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
        if (exception instanceof HibernateException) {
            errorResponse = new ErrorResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    Constants.INTERNAL_SERVER_ERROR, Collections.singletonList(Constants.INTERNAL_SERVER_ERROR));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
        errorResponse = new ErrorResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Constants.INTERNAL_SERVER_ERROR, Collections.singletonList(exception.getMessage()));
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
    }
}
