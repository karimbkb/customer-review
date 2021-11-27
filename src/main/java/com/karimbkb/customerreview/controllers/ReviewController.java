package com.karimbkb.customerreview.controllers;

import com.karimbkb.customerreview.dto.ReviewDto;
import com.karimbkb.customerreview.exceptions.ReviewNotFoundException;
import com.karimbkb.customerreview.models.Review;
import com.karimbkb.customerreview.repositories.ReviewRepository;
import com.karimbkb.customerreview.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ConcurrentModificationException;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequestMapping("api/v1/reviews")
public class ReviewController {
  private final ReviewRepository reviewRepository;
  private final ReviewService reviewService;

  @Autowired
  public ReviewController(ReviewRepository reviewRepository, ReviewService reviewService) {
    this.reviewRepository = reviewRepository;
    this.reviewService = reviewService;
  }

  @RequestMapping(method = RequestMethod.OPTIONS)
  ResponseEntity<?> options() {

    return ResponseEntity.ok()
        .allow(
            HttpMethod.GET,
            HttpMethod.POST,
            HttpMethod.HEAD,
            HttpMethod.OPTIONS,
            HttpMethod.PUT,
            HttpMethod.DELETE)
        .build();
  }

  @GetMapping
  @Secured("ROLE_myrole")
  public ResponseEntity<Iterable<Review>> getReviews(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "20") int size) {
    return ResponseEntity.ok(reviewRepository.findAll(PageRequest.of(page, size)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Review> getReview(@PathVariable Long id) {
    return reviewRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> ReviewNotFoundException.withId(id));
  }

  @PostMapping
  public ResponseEntity<Review> createReview(
      @Valid @RequestBody final ReviewDto reviewDto, UriComponentsBuilder uriBuilder) {
    Review review = reviewService.createReview(reviewDto);

    URI location =
        uriBuilder.path("api/v1/reviews/{id}").buildAndExpand(review.getReviewId()).toUri();
    return ResponseEntity.created(location).body(review);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Review> deleteReview(@PathVariable final Long id) {
    reviewRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Review> updateReview(
      @RequestBody final ReviewDto reviewDto, @PathVariable final Long id) {
    Optional<Review> reviewResult = reviewRepository.findByReviewId(id);
    if (reviewResult.isPresent() && reviewDto.getVersion() != reviewResult.get().getVersion()) {
      throw new ConcurrentModificationException(
          "Review with id "
              + id
              + " can not be updated. The given version of "
              + reviewDto.getVersion()
              + " does not equal the actual version of "
              + reviewResult.get().getVersion());
    }

    Review review = reviewService.updateReview(reviewDto, id);
    return ResponseEntity.ok(review);
  }
}
