package com.karimbkb.customerreview.service;

import com.karimbkb.customerreview.models.Review;
import lombok.NoArgsConstructor;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@NoArgsConstructor
public class VerifyVersionReview {

  @PrePersist
  public void setVersion(Review review) {
    review.setVersion(1);
  }

  @PreUpdate
  protected void verifyVersionOnReview(Review review) {
    review.setVersion(review.getVersion()+1);
  }
}
