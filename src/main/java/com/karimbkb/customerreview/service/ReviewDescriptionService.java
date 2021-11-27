package com.karimbkb.customerreview.service;

import com.karimbkb.customerreview.dto.ReviewDescriptionDto;
import com.karimbkb.customerreview.exceptions.ReviewDescriptionNotFoundException;
import com.karimbkb.customerreview.models.ReviewDescription;
import com.karimbkb.customerreview.repositories.ReviewDescriptionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ReviewDescriptionService {
  private final ReviewDescriptionRepository reviewDescriptionRepository;

  @Autowired
  public ReviewDescriptionService(ReviewDescriptionRepository reviewDescriptionRepository) {
    this.reviewDescriptionRepository = reviewDescriptionRepository;
  }

  @Transactional
  public ReviewDescription createReviewDescription(ReviewDescriptionDto reviewDescriptionDto) {
    ReviewDescription reviewDescription = new ReviewDescription();
    reviewDescription.setReviewId(reviewDescriptionDto.getReviewId());
    reviewDescription.setCustomerId(reviewDescriptionDto.getCustomerId());
    reviewDescription.setTitle(reviewDescriptionDto.getTitle());
    reviewDescription.setDescription(reviewDescriptionDto.getDescription());
    reviewDescription.setFirstName(reviewDescriptionDto.getFirstName());
    reviewDescription.setLastName(reviewDescriptionDto.getLastName());
    reviewDescription.setRating(reviewDescriptionDto.getRating());
    reviewDescription.setStatus(
        ReviewDescription.StatusEnum.valueOf(reviewDescriptionDto.getStatus()));

    return reviewDescriptionRepository.save(reviewDescription);
  }

  @Transactional
  public ReviewDescription updateReviewDescription(
      ReviewDescriptionDto reviewDescriptionDto, Long id) {
    return reviewDescriptionRepository
        .findById(id)
        .map(
            reviewDescription -> {
              BeanUtils.copyProperties(
                  reviewDescriptionDto, reviewDescription, "reviewDescriptionId", "version");
              reviewDescriptionRepository.save(reviewDescription);
              return reviewDescription;
            })
        .orElseThrow(() -> ReviewDescriptionNotFoundException.withId(id));
  }
}
