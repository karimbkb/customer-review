package com.karimbkb.customerreview.controllers;

import com.karimbkb.customerreview.dto.ReviewDescriptionDto;
import com.karimbkb.customerreview.exceptions.ReviewDescriptionNotFoundException;
import com.karimbkb.customerreview.models.ReviewDescription;
import com.karimbkb.customerreview.repositories.ReviewDescriptionRepository;
import com.karimbkb.customerreview.service.ReviewDescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("api/v1/reviewDescription")
public class ReviewDescriptionController {
  private final ReviewDescriptionRepository reviewDescriptionRepository;
  private final ReviewDescriptionService reviewDescriptionService;

  @Autowired
  public ReviewDescriptionController(ReviewDescriptionRepository reviewDescriptionRepository, ReviewDescriptionService reviewDescriptionService) {
    this.reviewDescriptionRepository = reviewDescriptionRepository;
    this.reviewDescriptionService = reviewDescriptionService;
  }

  @GetMapping
  public ResponseEntity<Iterable<ReviewDescription>> getReviewDescriptions() {
    return ResponseEntity.ok(reviewDescriptionRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReviewDescription> getReviewDescription(@PathVariable Long id) {
    return reviewDescriptionRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> ReviewDescriptionNotFoundException.withId(id));
  }

  @PostMapping
  public ResponseEntity<ReviewDescription> createReviewDescription(@RequestBody final ReviewDescriptionDto reviewDescriptionDto, UriComponentsBuilder uriBuilder) {
    ReviewDescription reviewDescriptionResult = reviewDescriptionService.createReviewDescription(reviewDescriptionDto);

    URI location = uriBuilder.path("/{id}").buildAndExpand(reviewDescriptionResult.getReviewId()).toUri();
    return ResponseEntity.created(location).body(reviewDescriptionResult);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ReviewDescription> deleteReviewDescription(@PathVariable final Long id) {
    reviewDescriptionRepository.deleteById(id);
    return ResponseEntity.status(204).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ReviewDescription> updateReviewDescription(@RequestBody final ReviewDescriptionDto reviewDescriptionDto, @PathVariable final Long id) {
    ReviewDescription reviewDescription = reviewDescriptionService.updateReviewDescription(reviewDescriptionDto, id);
    return ResponseEntity.ok(reviewDescription);
  }
}
