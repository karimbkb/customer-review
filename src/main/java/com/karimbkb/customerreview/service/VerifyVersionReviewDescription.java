package com.karimbkb.customerreview.service;

import com.karimbkb.customerreview.domain.ReviewDescription;
import com.karimbkb.customerreview.repositories.ReviewDescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.PreUpdate;
import java.util.ConcurrentModificationException;

@Service
@RequiredArgsConstructor
public class VerifyVersionReviewDescription {

    ReviewDescriptionRepository reviewDescriptionRepository;

    @PreUpdate
    protected void verifyVersionOnReviewDescription(ReviewDescription reviewDescription) {
        reviewDescriptionRepository.findOneByReviewIdAndCustomerId(reviewDescription.getReviewId(), reviewDescription.getCustomerId())
                .ifPresent(reviewDescriptionResult -> {
                    if (reviewDescriptionResult.getVersion() >= reviewDescription.getVersion()) {
                        throw new ConcurrentModificationException("Review description with id " + reviewDescription.getId() + " can not be updated. " +
                                "Version is equal or greater than current version");
                    }
                });
    }
}
