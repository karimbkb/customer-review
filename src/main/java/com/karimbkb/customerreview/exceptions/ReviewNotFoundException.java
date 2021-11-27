package com.karimbkb.customerreview.exceptions;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String message) {
        super(message);
    }

    public static ReviewNotFoundException withId(Long id) {
        return new ReviewNotFoundException("Review with id [" + id + "] was not found.");
    }

}
