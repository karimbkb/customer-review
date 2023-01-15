package com.karimbkb.customerreview.exceptions;

public class InputValidationException extends RuntimeException {
    public InputValidationException() {
        super("The input parameters aren't valid.");
    }

    public InputValidationException(String message) {
        super(message);
    }
}