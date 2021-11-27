package com.karimbkb.customerreview.service;

import com.karimbkb.customerreview.models.ReviewDescription;
import com.karimbkb.customerreview.repositories.ReviewDescriptionRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PreUpdate;
import java.util.ConcurrentModificationException;
import java.util.Optional;

@NoArgsConstructor
public class VerifyVersionReviewDescription {

  ReviewDescriptionRepository reviewDescriptionRepository;

  @Autowired
  public VerifyVersionReviewDescription(ReviewDescriptionRepository reviewDescriptionRepository) {
    this.reviewDescriptionRepository = reviewDescriptionRepository;
  }

  @PreUpdate
  protected void verifyVersionOnReviewDescription(ReviewDescription reviewDescription) {
    Optional<ReviewDescription> reviewDescriptionResult =
            reviewDescriptionRepository.findByReviewDescriptionId(reviewDescription.getReviewDescriptionId());

    if (reviewDescriptionResult.isPresent()
            && reviewDescription.getVersion() != reviewDescriptionResult.get().getVersion()) {
      throw new ConcurrentModificationException(
              "Review description with id "
                      + reviewDescription.getReviewDescriptionId()
                      + " can not be updated.");
    }
  }
}
