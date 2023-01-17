package com.karimbkb.customerreview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.karimbkb.customerreview.domain.Review;
import com.karimbkb.customerreview.domain.ReviewDescription;
import com.karimbkb.customerreview.dto.ReviewCreateDTO;
import com.karimbkb.customerreview.dto.ReviewDTO;
import com.karimbkb.customerreview.repositories.ReviewRepository;
import com.karimbkb.customerreview.util.PatchUtil;
import javassist.NotFoundException;
import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.karimbkb.customerreview.test.util.TestDataUtil.*;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MapperFacade mapperFacade;

    @Mock
    private ReviewDescriptionService reviewDescriptionService;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void shouldCreateReview() {
        // given
        Review review = buildReview();
        ReviewDTO reviewDTO = buildReviewDTO();
        ReviewCreateDTO reviewCreateDTO = buildReviewCreateDTO();

        when(mapperFacade.map(reviewCreateDTO, Review.class)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);
        when(mapperFacade.map(review, ReviewDTO.class)).thenReturn(reviewDTO);

        // when
        ReviewDTO reviewDTOResult = reviewService.createReview(reviewCreateDTO);

        // then
        assertThat(reviewDTOResult.getId()).isEqualTo(REVIEW_ID);
        assertThat(reviewDTOResult.getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(reviewDTOResult.getStoreId()).isEqualTo(STORE_ID);
    }

    @Test
    void shouldNotCreateReviewAndThrowException() {
        // given
        Review review = buildReview();
        ReviewCreateDTO reviewCreateDTO = buildReviewCreateDTO();

        when(mapperFacade.map(reviewCreateDTO, Review.class)).thenReturn(review);
        when(reviewRepository.save(review)).thenThrow(DataIntegrityViolationException.class);

        // when + then
        assertThatThrownBy(() -> reviewService.createReview(reviewCreateDTO))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldLoadReviewById() {
        // given
        Review review = buildReview();
        ReviewDTO reviewDTO = buildReviewDTO();

        when(reviewRepository.findById(REVIEW_ID)).thenReturn(of(review));
        when(mapperFacade.map(review, ReviewDTO.class)).thenReturn(reviewDTO);

        // when
        Optional<ReviewDTO> reviewDTOResult = reviewService.loadReviewById(REVIEW_ID);

        // then
        reviewDTOResult.ifPresent(dto -> {
            assertThat(dto.getId()).isEqualTo(REVIEW_ID);
            assertThat(dto.getProductId()).isEqualTo(PRODUCT_ID);
            assertThat(dto.getStoreId()).isEqualTo(STORE_ID);
        });
    }

    @Test
    void shouldGetAllReviews() {
        // given
        Review review = buildReview();
        ReviewDTO reviewDTO = buildReviewDTO();
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Review> reviews = new PageImpl<>(List.of(review), PageRequest.of(0, 20), 20);

        when(reviewRepository.findAll(pageRequest)).thenReturn(reviews);
        when(mapperFacade.map(review, ReviewDTO.class)).thenReturn(reviewDTO);

        // when
        Iterable<ReviewDTO> reviewDTOResult = reviewService.getAllReviews(pageRequest);

        // then
        reviewDTOResult.forEach(dto -> {
            assertThat(dto.getId()).isEqualTo(REVIEW_ID);
            assertThat(dto.getProductId()).isEqualTo(PRODUCT_ID);
            assertThat(dto.getStoreId()).isEqualTo(STORE_ID);
        });
    }

    @Test
    void shouldDeleteReview() {
        // given
        List<ReviewDescription> reviewDescriptions = List.of(buildReviewDescription());
        List<UUID> reviewDescriptionId = List.of(REVIEW_DESCRIPTION_ID);

        when(reviewDescriptionService.getAllReviewDescriptionsByReviewId(REVIEW_ID)).thenReturn(reviewDescriptions);

        // when
        reviewService.deleteReview(REVIEW_ID);

        // then
        verify(reviewRepository).deleteById(REVIEW_ID);
        verifyNoMoreInteractions(reviewRepository);
        verify(reviewDescriptionService).deleteAllReviewDescriptionsById(reviewDescriptionId);
        verifyNoMoreInteractions(reviewDescriptionService);
    }

    @Test
    void shouldUpdateReview() throws IOException, NotFoundException {
        // given
        Review review = buildReview();
        Review reviewPatched = buildReview();
        reviewPatched.setProductId(UUID.fromString("225925f1-f16e-4572-9a3a-0c76182819d4"));
        reviewPatched.setStoreId(4);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(new File(Paths.get("src/test/resources/requests/review/patch-review.json").toUri()));
        JsonPatch jsonPatch = JsonPatch.fromJson(json);

        when(reviewRepository.findById(REVIEW_ID)).thenReturn(of(review));

        // when
        try (MockedStatic<PatchUtil> mockedPatchUtil = Mockito.mockStatic(PatchUtil.class)) {
            mockedPatchUtil
                    .when(() -> PatchUtil.applyPatch(any(JsonPatch.class), any(Review.class), eq(Review.class)))
                    .thenReturn(reviewPatched);

            reviewService.patchReview(REVIEW_ID, jsonPatch);

            // then
            verify(reviewRepository).save(reviewPatched);
        }
    }
}