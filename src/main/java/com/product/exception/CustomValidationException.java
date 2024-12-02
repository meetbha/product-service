package com.product.exception;

import com.product.util.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static com.product.util.Constants.VALIDATION_FAILED;

@Slf4j
@Provider
public class CustomValidationException implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException exception) {
        log.error("Failed to process request with exception {}", exception.getMessage(), exception);
        ErrorResponse errorResponse;
        List<String> messages = extractMessages((ConstraintViolationException) exception);
        errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(),
                VALIDATION_FAILED, messages);
        return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
    }

    private List<String> extractMessages(ConstraintViolationException exception) {
        return exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }
}
