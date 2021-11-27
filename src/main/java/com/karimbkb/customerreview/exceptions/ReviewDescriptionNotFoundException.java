package com.karimbkb.customerreview.exceptions;

public class ReviewDescriptionNotFoundException extends RuntimeException {
    public ReviewDescriptionNotFoundException(String message) {
        super(message);
    }

    public static ReviewDescriptionNotFoundException withId(Long id) {
        return new ReviewDescriptionNotFoundException("Review description with id [" + id + "] was not found.");
    }

}
