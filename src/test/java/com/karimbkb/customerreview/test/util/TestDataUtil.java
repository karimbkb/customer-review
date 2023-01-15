package com.karimbkb.customerreview.test.util;

import com.karimbkb.customerreview.controllers.ReviewControllerTest;
import com.karimbkb.customerreview.domain.Review;
import com.karimbkb.customerreview.domain.ReviewDescription;
import com.karimbkb.customerreview.dto.ReviewCreateDTO;
import com.karimbkb.customerreview.dto.ReviewDTO;
import com.karimbkb.customerreview.dto.ReviewDescriptionCreateDTO;
import com.karimbkb.customerreview.dto.ReviewDescriptionDTO;
import lombok.experimental.UtilityClass;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@UtilityClass
public class TestDataUtil {

    public static final String REVIEW_URL_GET = "/api/v1/review/{id}";
    public static final String REVIEW_URL_GET_PAGINATION = "/api/v1/review";
    public static final String REVIEW_URL_DELETE = "/api/v1/review/{id}";
    public static final String REVIEW_URL_POST = "/api/v1/review";
    public static final String REVIEW_URL_PATCH = "/api/v1/review/{id}";

    public static final String REVIEW_DESCRIPTION_URL_GET = "/api/v1/reviewDescription/{id}";
    public static final String REVIEW_DESCRIPTION_URL_GET_PAGINATION = "/api/v1/reviewDescription";
    public static final String REVIEW_DESCRIPTION_URL_DELETE = "/api/v1/reviewDescription/{id}";
    public static final String REVIEW_DESCRIPTION_URL_POST = "/api/v1/reviewDescription";

    public static final UUID REVIEW_ID = UUID.fromString("f8fdfd78-936f-4ab9-b0b8-e6f6e5bb7b8a");
    public static final UUID PRODUCT_ID = UUID.fromString("ccbad298-1f9c-4290-9258-09dac2c4ab40");
    public static final UUID REVIEW_DESCRIPTION_ID = UUID.fromString("2e4efff5-d1dc-4d5e-bf0d-6ad8d93d8460");
    public static final UUID CUSTOMER_ID = UUID.fromString("4bc1eae2-7d98-40fd-ae31-af96e2aef967");
    public static final Integer STORE_ID = 3;
    public static final Integer RATING = 4;
    public static final String TITLE = "Title";
    public static final String DESCRIPTION = "Nice book";
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";

    public static Review buildReview() {
        Review review = new Review();
        review.setId(REVIEW_ID);
        review.setProductId(PRODUCT_ID);
        review.setStoreId(STORE_ID);
        review.setReviewDescriptions(List.of(buildReviewDescription()));
        return review;
    }

    public static ReviewDTO buildReviewDTO() {
        return ReviewDTO.builder()
                .id(REVIEW_ID)
                .productId(PRODUCT_ID)
                .storeId(STORE_ID)
                .build();
    }

    public static ReviewCreateDTO buildReviewCreateDTO() {
        return ReviewCreateDTO.builder()
                .productId(PRODUCT_ID)
                .storeId(STORE_ID)
                .build();
    }

    public static ReviewDescriptionDTO buildReviewDescriptionDTO() {
        return ReviewDescriptionDTO.builder()
                .id(REVIEW_DESCRIPTION_ID)
                .reviewId(REVIEW_ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .customerId(CUSTOMER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .rating(RATING)
                .createdAt(LocalDateTime.of(LocalDate.of(2021, 2, 6), LocalTime.of(12, 45)))
                .updatedAt(LocalDateTime.of(LocalDate.of(2021, 2, 6), LocalTime.of(12, 45)))
                .status(ReviewDescription.StatusEnum.pending)
                .build();
    }

    public static ReviewDescription buildReviewDescription() {
        return ReviewDescription.builder()
                .id(REVIEW_DESCRIPTION_ID)
                .reviewId(REVIEW_ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .customerId(CUSTOMER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .rating(RATING)
                .createdAt(LocalDateTime.of(LocalDate.of(2021, 2, 6), LocalTime.of(12, 45)))
                .updatedAt(LocalDateTime.of(LocalDate.of(2021, 2, 6), LocalTime.of(12, 45)))
                .status(ReviewDescription.StatusEnum.pending)
                .build();
    }

    public static ReviewDescriptionCreateDTO buildReviewDescriptionCreateDTO() {
        return ReviewDescriptionCreateDTO.builder()
                .reviewId(REVIEW_ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .customerId(CUSTOMER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .rating(RATING)
                .build();
    }

    public static String getStringFromResources(String filename) throws IOException {
        return IOUtils.toString(
                Objects.requireNonNull(
                        ReviewControllerTest.class.getClassLoader().getResourceAsStream(filename)),
                Charset.defaultCharset());
    }
}
