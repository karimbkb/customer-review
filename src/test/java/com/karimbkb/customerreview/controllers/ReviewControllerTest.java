package com.karimbkb.customerreview.controllers;

import com.github.fge.jsonpatch.JsonPatch;
import com.karimbkb.customerreview.domain.Review;
import com.karimbkb.customerreview.domain.ReviewDescription;
import com.karimbkb.customerreview.dto.ReviewCreateDTO;
import com.karimbkb.customerreview.dto.ReviewDTO;
import com.karimbkb.customerreview.repositories.ReviewDescriptionRepository;
import com.karimbkb.customerreview.repositories.ReviewRepository;
import com.karimbkb.customerreview.service.ReviewDescriptionService;
import com.karimbkb.customerreview.service.ReviewService;
import com.karimbkb.customerreview.util.PatchUtil;
import com.karimbkb.customerreview.validator.ReviewControllerPatchValidator;
import ma.glasnost.orika.MapperFacade;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.karimbkb.customerreview.test.util.TestDataUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@Import({ReviewService.class})
public class ReviewControllerTest {

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private ReviewDescriptionRepository reviewDescriptionRepository;

    @MockBean
    private MapperFacade mapperFacade;

    @MockBean
    private ReviewDescriptionService reviewDescriptionService;

    @SpyBean
    private ReviewControllerPatchValidator reviewControllerPatchValidator;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getReview() throws Exception {
        // given
        Review review = buildReview();
        ReviewDTO reviewDTO = buildReviewDTO();

        // when
        when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.of(review));
        when(mapperFacade.map(review, ReviewDTO.class)).thenReturn(reviewDTO);

        // then
        mockMvc
                .perform(get(REVIEW_URL_GET, REVIEW_ID).header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(REVIEW_ID.toString()))
                .andExpect(jsonPath("$.productId").value(PRODUCT_ID.toString()))
                .andExpect(jsonPath("$.storeId").value(3))
                .andReturn();
    }

    @Test
    void getReviewsWithPageAndSize() throws Exception {
        // given
        Review review = buildReview();
        ReviewDTO reviewDTO = buildReviewDTO();
        Page<Review> reviews = new PageImpl<>(List.of(review), PageRequest.of(0, 20), 20);

        // when
        when(reviewRepository.findAll(PageRequest.of(0, 20))).thenReturn(reviews);
        when(mapperFacade.map(review, ReviewDTO.class)).thenReturn(reviewDTO);

        // then
        mockMvc
                .perform(
                        get(REVIEW_URL_GET_PAGINATION)
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                                .param("page", "0")
                                .param("size", "20"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(REVIEW_ID.toString()))
                .andExpect(jsonPath("$.content[0].productId").value(PRODUCT_ID.toString()))
                .andExpect(jsonPath("$.content[0].storeId").value(STORE_ID))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(20))
                .andReturn();
    }

    @Test
    void createReview() throws Exception {
        // given
        ReviewDescription reviewDescription = buildReviewDescription();
        ReviewCreateDTO reviewCreateDTO = buildReviewCreateDTO();
        ReviewDTO reviewDTO = buildReviewDTO();
        Review review = buildReview();

        // when
        when(reviewRepository.findByStoreIdAndProductId(4, PRODUCT_ID)).thenReturn(Optional.of(review));
        when(reviewDescriptionRepository.save(reviewDescription)).thenReturn(reviewDescription);
        when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.of(review));
        when(mapperFacade.map(reviewCreateDTO, Review.class)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);
        when(mapperFacade.map(review, ReviewDTO.class)).thenReturn(reviewDTO);

        // then
        mockMvc
                .perform(
                        post(REVIEW_URL_POST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getStringFromResources("requests/review/create-review.json")))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString("/api/v1/review/" + REVIEW_ID)));
    }

    @Test
    void deleteReview() throws Exception {
        // given
        ReviewDescription reviewDescription = buildReviewDescription();

        // when
        when(reviewDescriptionService.getAllReviewDescriptionsByReviewId(REVIEW_ID)).thenReturn(List.of(reviewDescription));

        mockMvc
                .perform(
                        delete(REVIEW_URL_DELETE, REVIEW_ID)
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
                .andExpect(status().is(204))
                .andReturn();

        // then
        verify(reviewRepository).deleteById(REVIEW_ID);
        verify(reviewDescriptionService).deleteAllReviewDescriptionsById(List.of(REVIEW_DESCRIPTION_ID));
    }

    @Test
    void updateReview() throws Exception {
        // given
        Review review = buildReview();
        Review reviewPatched = buildReview();
        reviewPatched.setStoreId(4);
        reviewPatched.setProductId(UUID.fromString("225925f1-f16e-4572-9a3a-0c76182819d4"));

        // when
        when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.ofNullable(review));

        try (MockedStatic<PatchUtil> mockedPatchUtil = Mockito.mockStatic(PatchUtil.class)) {
            mockedPatchUtil
                    .when(() -> PatchUtil.applyPatch(any(JsonPatch.class), any(Review.class), eq(Review.class)))
                    .thenReturn(reviewPatched);

            mockMvc
                    .perform(
                            patch(REVIEW_URL_GET, REVIEW_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(getStringFromResources("requests/review/patch-review.json")))
                    .andExpect(status().isOk());

            // then
            verify(reviewRepository).save(reviewPatched);
        }
    }
}
