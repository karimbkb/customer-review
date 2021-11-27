package com.karimbkb.customerreview.service;

import com.karimbkb.customerreview.repositories.ReviewDescriptionRepository;
import com.karimbkb.customerreview.repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private ReviewDescriptionRepository reviewDescriptionRepository;

  @InjectMocks
  private ReviewService reviewService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createReviewWhenReviewExists() {
//      reviewRepository.findByStoreIdAndProductId(
//              reviewDto.getStoreId(), reviewDto.getProductId());
    }

    @Test
    void updateReview() {
    }
}