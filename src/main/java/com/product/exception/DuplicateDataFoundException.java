package com.product.exception;

public class DuplicateDataFoundException extends RuntimeException {

    public DuplicateDataFoundException(String message) {
        super(message);
    }
}
