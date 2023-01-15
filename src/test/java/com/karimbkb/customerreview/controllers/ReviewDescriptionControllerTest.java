package com.karimbkb.customerreview.controllers;

import com.github.fge.jsonpatch.JsonPatch;
import com.karimbkb.customerreview.domain.ReviewDescription;
import com.karimbkb.customerreview.dto.ReviewDescriptionCreateDTO;
import com.karimbkb.customerreview.dto.ReviewDescriptionDTO;
import com.karimbkb.customerreview.repositories.ReviewDescriptionRepository;
import com.karimbkb.customerreview.repositories.ReviewRepository;
import com.karimbkb.customerreview.service.ReviewDescriptionService;
import com.karimbkb.customerreview.util.PatchUtil;
import ma.glasnost.orika.MapperFacade;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(ReviewDescriptionController.class)
@Import(ReviewDescriptionService.class)
public class ReviewDescriptionControllerTest {

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private ReviewDescriptionRepository reviewDescriptionRepository;

    @MockBean
    private MapperFacade mapperFacade;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getReviewDescription() throws Exception {
        // given
        ReviewDescription reviewDescription = buildReviewDescription();
        ReviewDescriptionDTO reviewDescriptionDTO = buildReviewDescriptionDTO();

        // when
        when(reviewDescriptionRepository.findById(REVIEW_DESCRIPTION_ID)).thenReturn(Optional.of(reviewDescription));
        when(mapperFacade.map(reviewDescription, ReviewDescriptionDTO.class)).thenReturn(reviewDescriptionDTO);

        // then
        mockMvc
                .perform(get(REVIEW_DESCRIPTION_URL_GET, REVIEW_DESCRIPTION_ID).header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(REVIEW_DESCRIPTION_ID.toString()))
                .andExpect(jsonPath("$.reviewId").value(REVIEW_ID.toString()))
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID.toString()))
                .andExpect(jsonPath("$.createdAt").value("2021-02-06T12:45:00"))
                .andExpect(jsonPath("$.updatedAt").value("2021-02-06T12:45:00"))
                .andExpect(jsonPath("$.rating").value(RATING))
                .andExpect(jsonPath("$.status").value(ReviewDescription.StatusEnum.pending.toString()))
                .andReturn();
    }

    @Test
    void getReviewsWithPageAndSize() throws Exception {
        // given
        ReviewDescription reviewDescription = buildReviewDescription();
        ReviewDescriptionDTO reviewDescriptionDTO = buildReviewDescriptionDTO();
        Page<ReviewDescription> reviewDescriptions = new PageImpl<>(List.of(reviewDescription), PageRequest.of(0, 20), 20);

        // when
        when(reviewDescriptionRepository.findAll(PageRequest.of(0, 20))).thenReturn(reviewDescriptions);
        when(mapperFacade.map(reviewDescription, ReviewDescriptionDTO.class)).thenReturn(reviewDescriptionDTO);

        // then
        mockMvc
                .perform(
                        get(REVIEW_DESCRIPTION_URL_GET_PAGINATION)
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                                .param("page", "0")
                                .param("size", "20"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(REVIEW_DESCRIPTION_ID.toString()))
                .andExpect(jsonPath("$.content[0].reviewId").value(REVIEW_ID.toString()))
                .andExpect(jsonPath("$.content[0].title").value(TITLE))
                .andExpect(jsonPath("$.content[0].description").value(DESCRIPTION))
                .andExpect(jsonPath("$.content[0].customerId").value(CUSTOMER_ID.toString()))
                .andExpect(jsonPath("$.content[0].createdAt").value("2021-02-06T12:45:00"))
                .andExpect(jsonPath("$.content[0].updatedAt").value("2021-02-06T12:45:00"))
                .andExpect(jsonPath("$.content[0].rating").value(RATING))
                .andExpect(jsonPath("$.content[0].status").value("pending"))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(20))
                .andReturn();
    }

    @Test
    void createReview() throws Exception {
        // given
        ReviewDescription reviewDescription = buildReviewDescription();
        ReviewDescriptionCreateDTO reviewDescriptionCreateDTO = buildReviewDescriptionCreateDTO();
        ReviewDescriptionDTO reviewDescriptionDTO = buildReviewDescriptionDTO();

        // when
        when(mapperFacade.map(reviewDescriptionCreateDTO, ReviewDescription.class)).thenReturn(reviewDescription);
        when(reviewDescriptionRepository.save(reviewDescription)).thenReturn(reviewDescription);
        when(mapperFacade.map(reviewDescription, ReviewDescriptionDTO.class)).thenReturn(reviewDescriptionDTO);

        // then
        mockMvc
                .perform(
                        post(REVIEW_DESCRIPTION_URL_POST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getStringFromResources("requests/review-description/create-review-description.json")))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString("/api/v1/reviewDescription/" + REVIEW_DESCRIPTION_ID)));
    }

    @Test
    void deleteReviewDescription() throws Exception {
        // when
        mockMvc
                .perform(
                        delete(REVIEW_DESCRIPTION_URL_DELETE, REVIEW_DESCRIPTION_ID)
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
                .andExpect(status().is(204))
                .andReturn();

        // then
        verify(reviewDescriptionRepository).deleteById(REVIEW_DESCRIPTION_ID);
    }

    @Test
    void updateReviewDescription() throws Exception {
        // given
        ReviewDescription reviewDescription = buildReviewDescription();
        ReviewDescription reviewDescriptionPatched = buildReviewDescription();
        reviewDescriptionPatched.setCustomerId(UUID.fromString("c3d49655-063c-40c8-9dc3-d75fbb3c75db"));
        reviewDescriptionPatched.setFirstName("Max");

        // when
        when(reviewDescriptionRepository.findById(REVIEW_DESCRIPTION_ID)).thenReturn(Optional.ofNullable(reviewDescription));

        try (MockedStatic<PatchUtil> mockedPatchUtil = Mockito.mockStatic(PatchUtil.class)) {
            mockedPatchUtil
                    .when(() -> PatchUtil.applyPatch(any(JsonPatch.class), any(ReviewDescription.class), eq(ReviewDescription.class)))
                    .thenReturn(reviewDescriptionPatched);

            mockMvc
                    .perform(
                            patch(REVIEW_DESCRIPTION_URL_GET, REVIEW_DESCRIPTION_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(getStringFromResources("requests/review-description/patch-review-description.json")))
                    .andExpect(status().isOk());

            // then
            verify(reviewDescriptionRepository).save(reviewDescriptionPatched);
        }
    }
}
