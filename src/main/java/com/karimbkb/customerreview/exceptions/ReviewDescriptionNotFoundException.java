package com.karimbkb.customerreview.exceptions;

import java.util.UUID;

public class ReviewDescriptionNotFoundException extends RuntimeException {
    public ReviewDescriptionNotFoundException(String message) {
        super(message);
    }

    public static ReviewDescriptionNotFoundException withId(UUID id) {
        return new ReviewDescriptionNotFoundException("Review description with id [" + id + "] was not found.");
    }

}
