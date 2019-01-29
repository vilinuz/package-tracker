package com.mobiquityinc.packer.exception;

public class APIException extends RuntimeException {
    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(String message, Throwable cause) {
        super(message, cause);
    }
}
