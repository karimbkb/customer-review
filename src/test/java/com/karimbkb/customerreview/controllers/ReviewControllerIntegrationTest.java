package com.karimbkb.customerreview.controllers;

import com.karimbkb.customerreview.domain.Review;
import com.karimbkb.customerreview.repositories.ReviewDescriptionRepository;
import com.karimbkb.customerreview.repositories.ReviewRepository;
import com.karimbkb.customerreview.service.ReviewService;
import com.karimbkb.customerreview.validator.ReviewControllerPatchValidator;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.karimbkb.customerreview.test.util.TestDataUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
class ReviewControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:11.5-alpine")
            .withDatabaseName("postgres")
            .withUsername("root")
            .withPassword("root");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", container::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", container::getPassword);
    }

    static {
        container.start();
    }

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewDescriptionRepository reviewDescriptionRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewControllerPatchValidator reviewControllerPatchValidator;

    @BeforeEach
    void setUpRestAssured() {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ReviewController(reviewService, reviewControllerPatchValidator)).build();
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @BeforeAll
    static void setUp() {
        ClassicConfiguration configuration = new ClassicConfiguration();
        configuration.setDataSource(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        configuration.setLocations(new Location("classpath:db/migration"));

        Flyway flyway = new Flyway(configuration);
        flyway.migrate();
    }

    @Test
    @Sql(scripts = "/sql/review/insert_review_data.sql")
    void shouldLoadReviewById() {
        RestAssuredMockMvc
            .given()
                .auth().none()
            .when()
                .get(REVIEW_URL_GET, REVIEW_ID)
                .prettyPeek()
            .then()
                .statusCode(OK.value())
                .body("id", equalTo(REVIEW_ID.toString()))
                .body("storeId", equalTo(STORE_ID))
                .body("productId", equalTo(PRODUCT_ID.toString()))
                .body("version", equalTo(1));
    }

    @Test
    @Sql(scripts = "/sql/review/insert_review_data.sql")
    void shouldDeleteReviewAndDescriptionsByReviewId() {
        RestAssuredMockMvc
            .given()
                .auth().none()
            .when()
                .delete(REVIEW_URL_DELETE, REVIEW_ID)
                .prettyPeek()
            .then()
                .statusCode(NO_CONTENT.value());

        assertThat(reviewRepository.findById(REVIEW_ID)).isEmpty();
        assertThat(reviewDescriptionRepository.findAllByReviewId(REVIEW_ID)).asList().hasSize(0);
    }

    @Test
    @Sql(scripts = "/sql/review/insert_review_data.sql")
    void shouldCreateNewReview() throws IOException {
        String reviewCreateJson = getStringFromResources("requests/review/create-review.json");

        RestAssuredMockMvc
            .given()
                .auth().none()
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(reviewCreateJson)
            .when()
                .post(REVIEW_URL_POST)
                .prettyPeek()
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("storeId", equalTo(STORE_ID))
                .body("productId", equalTo(PRODUCT_ID.toString()))
                .body("version", equalTo(1));
    }

    @Test
    @Sql(scripts = "/sql/review/insert_review_data.sql")
    void shouldPatchReview() throws IOException {
        String reviewPatchJson = getStringFromResources("requests/review/patch-review.json");

        RestAssuredMockMvc
            .given()
                .auth().none()
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(reviewPatchJson)
            .when()
                .patch(REVIEW_URL_PATCH, REVIEW_ID)
                .prettyPeek()
            .then()
                .statusCode(OK.value());

        Optional<Review> review = reviewRepository.findById(REVIEW_ID);
        assertThat(review).hasValueSatisfying(entity -> {
            assertThat(entity.getId()).isEqualTo(REVIEW_ID);
            assertThat(entity.getStoreId()).isEqualTo(4);
            assertThat(entity.getProductId()).isEqualTo(UUID.fromString("225925f1-f16e-4572-9a3a-0c76182819d4"));
            assertThat(entity.getVersion()).isEqualTo(2);
        });
    }

    @Test
    @Sql(scripts = "/sql/review/insert_review_data.sql")
    void shouldPatchReviewWithErrorMessage() throws IOException {
        String reviewPatchJson = getStringFromResources("requests/review/patch-review-with-missing-version.json");

        RestAssuredMockMvc
            .given()
                .auth().none()
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(reviewPatchJson)
            .when()
                .patch(REVIEW_URL_PATCH, REVIEW_ID)
                .prettyPeek()
            .then()
                .statusCode(INTERNAL_SERVER_ERROR.value());
    }
}