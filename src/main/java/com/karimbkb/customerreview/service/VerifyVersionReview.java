package com.karimbkb.customerreview.service;

import com.karimbkb.customerreview.domain.Review;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;

@Component
public class VerifyVersionReview {

    @PrePersist
    public void setVersionBeforeSave(Review review) {
        if (review.getVersion() == null || review.getVersion() <= 0) {
            review.setVersion(1);
        }
    }
}
