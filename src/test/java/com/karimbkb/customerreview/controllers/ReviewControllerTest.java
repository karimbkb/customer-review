package com.karimbkb.customerreview.controllers;

import com.karimbkb.customerreview.models.Review;
import com.karimbkb.customerreview.models.ReviewDescription;
import com.karimbkb.customerreview.repositories.ReviewDescriptionRepository;
import com.karimbkb.customerreview.repositories.ReviewRepository;
import com.karimbkb.customerreview.service.ReviewService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@Import(ReviewService.class)
class ReviewControllerTest {

  @MockBean private ReviewRepository reviewRepository;

  @MockBean private ReviewDescriptionRepository reviewDescriptionRepository;

  @Autowired
  private ReviewService reviewService;

  @Autowired private MockMvc mockMvc;

  @Test
  void getReview() throws Exception {
    ReviewDescription reviewDescription = buildReviewDescription();
    Review review = buildReview(reviewDescription);

    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

    mockMvc
        .perform(get("/api/v1/reviews/1").header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.reviewId").value(1L))
        .andExpect(jsonPath("$.productId").value(9723))
        .andExpect(jsonPath("$.storeId").value(3))
        .andExpect(jsonPath("$.reviewDescriptions.size()").value(1))
        .andExpect(jsonPath("$.reviewDescriptions[0].reviewId").value(1L))
        .andExpect(jsonPath("$.reviewDescriptions[0].title").value("Title"))
        .andExpect(jsonPath("$.reviewDescriptions[0].description").value("Nice book"))
        .andExpect(jsonPath("$.reviewDescriptions[0].customerId").value(1234))
        .andExpect(jsonPath("$.reviewDescriptions[0].createdAt").value("2021-02-06T12:45:00"))
        .andExpect(jsonPath("$.reviewDescriptions[0].updatedAt").value("2021-02-06T12:45:00"))
        .andExpect(jsonPath("$.reviewDescriptions[0].rating").value(4))
        .andExpect(jsonPath("$.reviewDescriptions[0].status").value("pending"))
        .andReturn();
  }

  @Test
  void getReviewsWithPageAndSize() throws Exception {
    ReviewDescription reviewDescription = buildReviewDescription();
    Review review = buildReview(reviewDescription);

    Page<Review> reviews = new PageImpl<>(List.of(review), PageRequest.of(0, 20), 20);
    when(reviewRepository.findAll(PageRequest.of(0, 20))).thenReturn(reviews);

    mockMvc
        .perform(
            get("/api/v1/reviews")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "20"))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.content.size()").value(1))
        .andExpect(jsonPath("$.content[0].reviewId").value(1L))
        .andExpect(jsonPath("$.content[0].productId").value(9723))
        .andExpect(jsonPath("$.content[0].storeId").value(3))
        .andExpect(jsonPath("$.content[0].reviewDescriptions.size()").value(1))
        .andExpect(jsonPath("$.content[0].reviewDescriptions[0].reviewId").value(1L))
        .andExpect(jsonPath("$.content[0].reviewDescriptions[0].title").value("Title"))
        .andExpect(jsonPath("$.content[0].reviewDescriptions[0].description").value("Nice book"))
        .andExpect(jsonPath("$.content[0].reviewDescriptions[0].customerId").value(1234))
        .andExpect(
            jsonPath("$.content[0].reviewDescriptions[0].createdAt").value("2021-02-06T12:45:00"))
        .andExpect(
            jsonPath("$.content[0].reviewDescriptions[0].updatedAt").value("2021-02-06T12:45:00"))
        .andExpect(jsonPath("$.content[0].reviewDescriptions[0].rating").value(4))
        .andExpect(jsonPath("$.content[0].reviewDescriptions[0].status").value("pending"))
        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.pageable.pageSize").value(20))
        .andReturn();
  }

  @Test
  @WithMockUser
  void createReviewWithReviewExists() throws Exception {
    ReviewDescription reviewDescription = buildReviewDescription();
    Review review = buildReview(reviewDescription);

    when(reviewRepository.findByStoreIdAndProductId(4, 1221)).thenReturn(Optional.of(review));
    when(reviewDescriptionRepository.save(reviewDescription)).thenReturn(reviewDescription);
    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

    mockMvc
        .perform(
            post("/api/v1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getStringFromResources("requests/create-review.json")))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", Matchers.containsString("/api/v1/reviews/1")));
  }

  @Test
  @WithMockUser
  void deleteReview() throws Exception {
    doNothing().when(reviewRepository).deleteById(1L);
    doNothing().when(reviewDescriptionRepository).deleteAllByReviewId(1L);

    mockMvc
        .perform(
            delete("/api/v1/reviews/{reviewId}", 1)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
        .andExpect(status().is(204))
        .andReturn();

    verify(reviewRepository).deleteById(1L);
    verify(reviewDescriptionRepository).deleteAllByReviewId(1L);
  }

  @Test
  void updateReview() {}

  private ReviewDescription buildReviewDescription() {
    ReviewDescription reviewDescription = new ReviewDescription();
    reviewDescription.setReviewId(1L);
    reviewDescription.setTitle("Title");
    reviewDescription.setDescription("Nice book");
    reviewDescription.setCustomerId(1234);
    reviewDescription.setRating(4);
    reviewDescription.setCreatedAt(
        LocalDateTime.of(LocalDate.of(2021, 2, 6), LocalTime.of(12, 45)));
    reviewDescription.setUpdatedAt(
        LocalDateTime.of(LocalDate.of(2021, 2, 6), LocalTime.of(12, 45)));
    reviewDescription.setStatus(ReviewDescription.StatusEnum.pending);
    return reviewDescription;
  }

  Review buildReview(ReviewDescription reviewDescription) {
    Review review = new Review();
    review.setReviewId(1L);
    review.setProductId(9723);
    review.setStoreId(3);
    review.setReviewDescriptions(List.of(reviewDescription));
    return review;
  }

  private static String getStringFromResources(String filename) throws IOException {
    return IOUtils.toString(
        Objects.requireNonNull(
            ReviewControllerTest.class.getClassLoader().getResourceAsStream(filename)),
        Charset.defaultCharset());
  }
}
