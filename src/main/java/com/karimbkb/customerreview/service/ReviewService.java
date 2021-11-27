package com.karimbkb.customerreview.service;

import com.karimbkb.customerreview.dto.ReviewDto;
import com.karimbkb.customerreview.exceptions.ReviewNotFoundException;
import com.karimbkb.customerreview.models.Review;
import com.karimbkb.customerreview.repositories.ReviewRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ReviewService {
  private final ReviewRepository reviewRepository;

  @Autowired
  public ReviewService(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }

  @Transactional
  public Review createReview(ReviewDto reviewDto) {
    Optional<Review> reviewResult =
        reviewRepository.findByStoreIdAndProductId(
            reviewDto.getStoreId(), reviewDto.getProductId());

    if (reviewResult.isPresent()) {
      return reviewResult.get();
    }

    Review review = new Review();
    review.setStoreId(reviewDto.getStoreId());
    review.setProductId(reviewDto.getProductId());
    return reviewRepository.save(review);
  }

  @Transactional
  public Review updateReview(ReviewDto reviewDto, Long id) {
    return reviewRepository
        .findById(id)
        .map(
            review -> {
              BeanUtils.copyProperties(reviewDto, review, "reviewId", "version");
              reviewRepository.save(review);
              return review;
            })
        .orElseThrow(() -> ReviewNotFoundException.withId(id));
  }
}
