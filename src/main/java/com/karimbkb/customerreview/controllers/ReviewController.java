package com.karimbkb.customerreview.controllers;

import com.github.fge.jsonpatch.JsonPatch;
import com.karimbkb.customerreview.dto.ReviewCreateDTO;
import com.karimbkb.customerreview.dto.ReviewDTO;
import com.karimbkb.customerreview.exceptions.ReviewNotFoundException;
import com.karimbkb.customerreview.service.ReviewService;
import com.karimbkb.customerreview.validator.ReviewControllerPatchValidator;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewControllerPatchValidator reviewControllerPatchValidator;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, HttpMethod httpMethod) {
        if(httpMethod == HttpMethod.PATCH) {
            webDataBinder.addValidators(reviewControllerPatchValidator);
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<ReviewDTO>> getReviews(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok(reviewService.getAllReviews(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a review by id")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable UUID id) {
        return reviewService
                .loadReviewById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ReviewNotFoundException.withId(id));
    }

    @PostMapping
    @ResponseStatus(OK)
    @ApiOperation(value = "Create a new review")
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewCreateDTO reviewCreateDTO, UriComponentsBuilder uriBuilder) {
        ReviewDTO review = reviewService.createReview(reviewCreateDTO);
        URI location = uriBuilder.path("api/v1/review/{id}").buildAndExpand(review.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(review);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete review for given id")
    public void deleteReview(@PathVariable final UUID id) {
        reviewService.deleteReview(id);
    }

    @ResponseStatus(OK)
    @PatchMapping("/{id}")
    @ApiOperation(value = "Patch review for given id")
    public void updateReview(@PathVariable("id") UUID id, @Valid @RequestBody JsonPatch patch) throws NotFoundException {
        reviewService.patchReview(id, patch);
    }
}
