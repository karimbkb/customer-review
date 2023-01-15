package com.karimbkb.customerreview.exceptions;

import java.util.UUID;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String message) {
        super(message);
    }

    public static ReviewNotFoundException withId(UUID id) {
        return new ReviewNotFoundException("Review with id [" + id + "] was not found.");
    }

}
