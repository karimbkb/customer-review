package com.karimbkb.customerreview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.karimbkb.customerreview.domain.ReviewDescription;
import com.karimbkb.customerreview.domain.ReviewDescription.StatusEnum;
import com.karimbkb.customerreview.dto.ReviewDescriptionCreateDTO;
import com.karimbkb.customerreview.dto.ReviewDescriptionDTO;
import com.karimbkb.customerreview.repositories.ReviewDescriptionRepository;
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

import static com.karimbkb.customerreview.test.util.TestDataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewDescriptionServiceTest {

    @Mock
    private ReviewDescriptionRepository reviewDescriptionRepository;

    @Mock
    private MapperFacade mapperFacade;

    @InjectMocks
    private ReviewDescriptionService reviewDescriptionService;

    @Test
    void shouldCreateReviewDescription() {
        // given
        ReviewDescription reviewDescription = buildReviewDescription();
        ReviewDescriptionDTO reviewDescriptionDTO = buildReviewDescriptionDTO();
        ReviewDescriptionCreateDTO reviewDescriptionCreateDTO = buildReviewDescriptionCreateDTO();

        when(mapperFacade.map(reviewDescriptionCreateDTO, ReviewDescription.class)).thenReturn(reviewDescription);
        when(reviewDescriptionRepository.save(reviewDescription)).thenReturn(reviewDescription);
        when(mapperFacade.map(reviewDescription, ReviewDescriptionDTO.class)).thenReturn(reviewDescriptionDTO);

        // when
        ReviewDescriptionDTO reviewDescriptionDTOResult = reviewDescriptionService.createReviewDescription(reviewDescriptionCreateDTO);

        // then
        assertThat(reviewDescriptionDTOResult.getReviewId()).isEqualTo(REVIEW_ID);
        assertThat(reviewDescriptionDTOResult.getTitle()).isEqualTo(TITLE);
        assertThat(reviewDescriptionDTOResult.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(reviewDescriptionDTOResult.getCustomerId()).isEqualTo(CUSTOMER_ID);
        assertThat(reviewDescriptionDTOResult.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(reviewDescriptionDTOResult.getLastName()).isEqualTo(LAST_NAME);
        assertThat(reviewDescriptionDTOResult.getRating()).isEqualTo(RATING);
        assertThat(reviewDescriptionDTOResult.getStatus()).isEqualTo(StatusEnum.pending);
    }

    @Test
    void shouldNotCreateReviewDescriptionAndThrowException() {
        // given
        ReviewDescription reviewDescription = buildReviewDescription();
        ReviewDescriptionCreateDTO reviewDescriptionCreateDTO = buildReviewDescriptionCreateDTO();

        when(mapperFacade.map(reviewDescriptionCreateDTO, ReviewDescription.class)).thenReturn(reviewDescription);
        when(reviewDescriptionRepository.save(reviewDescription)).thenThrow(DataIntegrityViolationException.class);

        // when + then
        assertThatThrownBy(() -> reviewDescriptionService.createReviewDescription(reviewDescriptionCreateDTO))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldGetAllReviewDescriptions() {
        // given
        ReviewDescription reviewDescription = buildReviewDescription();
        ReviewDescriptionDTO reviewDescriptionDTO = buildReviewDescriptionDTO();
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<ReviewDescription> reviewDescriptions = new PageImpl<>(List.of(reviewDescription), PageRequest.of(0, 20), 20);

        when(reviewDescriptionRepository.findAll(pageRequest)).thenReturn(reviewDescriptions);
        when(mapperFacade.map(reviewDescription, ReviewDescriptionDTO.class)).thenReturn(reviewDescriptionDTO);

        // when
        Page<ReviewDescriptionDTO> reviewDescriptionDTOResult = reviewDescriptionService.getAllReviewDescriptions(pageRequest);

        // then
        reviewDescriptionDTOResult.forEach(dto -> {
            assertThat(dto.getId()).isEqualTo(REVIEW_DESCRIPTION_ID);
            assertThat(dto.getReviewId()).isEqualTo(REVIEW_ID);
            assertThat(dto.getTitle()).isEqualTo(TITLE);
            assertThat(dto.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(dto.getRating()).isEqualTo(RATING);
            assertThat(dto.getStatus()).isEqualTo(StatusEnum.pending);
            assertThat(dto.getFirstName()).isEqualTo(FIRST_NAME);
            assertThat(dto.getLastName()).isEqualTo(LAST_NAME);
        });
    }

    @Test
    void shouldGetAllReviewDescriptionsByReviewId() {
        // given
        ReviewDescription reviewDescription = buildReviewDescription();
        when(reviewDescriptionRepository.findAllByReviewId(REVIEW_ID)).thenReturn(List.of(reviewDescription));

        // when
        List<ReviewDescription> reviewDescriptions = reviewDescriptionService.getAllReviewDescriptionsByReviewId(REVIEW_ID);

        // then
        assertThat(reviewDescriptions)
                .hasSize(1)
                .isEqualTo(List.of(reviewDescription));
        verifyNoMoreInteractions(reviewDescriptionRepository);
    }

    @Test
    void shouldDeleteReviewDescription() {
        // when
        reviewDescriptionService.deleteReviewDescription(REVIEW_DESCRIPTION_ID);

        // then
        verify(reviewDescriptionRepository).deleteById(REVIEW_DESCRIPTION_ID);
        verifyNoMoreInteractions(reviewDescriptionRepository);
    }

    @Test
    void shouldDeleteAllReviewDescriptionsById() {
        // when
        reviewDescriptionService.deleteAllReviewDescriptionsById(List.of(REVIEW_DESCRIPTION_ID));

        // then
        verify(reviewDescriptionRepository).deleteAllByIdInBatch(List.of(REVIEW_DESCRIPTION_ID));
        verifyNoMoreInteractions(reviewDescriptionRepository);
    }

    @Test
    void shouldGetReviewDescriptionById() {
        // given
        ReviewDescription reviewDescription = buildReviewDescription();
        ReviewDescriptionDTO reviewDescriptionDTO = buildReviewDescriptionDTO();
        when(reviewDescriptionRepository.findById(REVIEW_DESCRIPTION_ID)).thenReturn(Optional.of(reviewDescription));
        when(mapperFacade.map(reviewDescription, ReviewDescriptionDTO.class)).thenReturn(reviewDescriptionDTO);

        // when
        Optional<ReviewDescriptionDTO> reviewDescriptionDTOResult = reviewDescriptionService.getReviewDescriptionById(REVIEW_DESCRIPTION_ID);

        // then
        assertThat(reviewDescriptionDTOResult)
                .isPresent()
                .isEqualTo(Optional.of(reviewDescriptionDTO));
    }

    @Test
    void shouldUpdateReviewDescription() throws IOException, NotFoundException {
        // given
        ReviewDescription reviewDescription = buildReviewDescription();
        ReviewDescription reviewDescriptionPatched = buildReviewDescription();
        reviewDescriptionPatched.setTitle("A better title");
        reviewDescriptionPatched.setDescription("A better description for this product.");
        reviewDescriptionPatched.setVersion(2);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(new File(Paths.get("src/test/resources/requests/review-description/patch-review-description.json").toUri()));
        JsonPatch jsonPatch = JsonPatch.fromJson(json);

        when(reviewDescriptionRepository.findById(REVIEW_DESCRIPTION_ID)).thenReturn(Optional.of(reviewDescription));

        // when
        try (MockedStatic<PatchUtil> mockedPatchUtil = Mockito.mockStatic(PatchUtil.class)) {
            mockedPatchUtil
                    .when(() -> PatchUtil.applyPatch(any(JsonPatch.class), any(ReviewDescription.class), eq(ReviewDescription.class)))
                    .thenReturn(reviewDescriptionPatched);

            reviewDescriptionService.patchReviewDescription(REVIEW_DESCRIPTION_ID, jsonPatch);

            // then
            verify(reviewDescriptionRepository).save(reviewDescriptionPatched);
        }
    }
}