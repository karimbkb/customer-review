package com.karimbkb.customerreview.repositories;

import com.karimbkb.customerreview.models.Review;
import com.karimbkb.customerreview.models.ReviewDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.driver-class-name=com.p6spy.engine.spy.P6SpyDriver",
        "spring.datasource.url=jdbc:p6spy:h2:mem:testing;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false"
})
class ReviewDescriptionRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ReviewDescriptionRepository reviewDescriptionRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
    }

    @Test
    void notNull() {
        assertThat(entityManager, is(notNullValue()));
        assertThat(reviewDescriptionRepository, is(notNullValue()));
        assertThat(reviewRepository, is(notNullValue()));
        assertThat(testEntityManager, is(notNullValue()));
        assertThat(dataSource, is(notNullValue()));

        Review review = new Review();
        review.setStoreId(1);
        review.setProductId(3452);

        Review reviewResult = reviewRepository.save(review);

        ReviewDescription reviewDescription = new ReviewDescription();
        reviewDescription.setReviewId(reviewResult.getReviewId());
        reviewDescription.setTitle("Title");
        reviewDescription.setDescription("Nice book");
        reviewDescription.setCustomerId(1234);
        reviewDescription.setRating(4);
        reviewDescription.setStatus(ReviewDescription.StatusEnum.pending);

        ReviewDescription result = testEntityManager.persistFlushFind(reviewDescription);

        assertThat(result, is(notNullValue()));
    }

    @Test
    void deleteAllByReviewId() {
        Review review = new Review();
        review.setStoreId(1);
        review.setProductId(3452);
        Review reviewResult = reviewRepository.save(review);

        ReviewDescription reviewDescription = new ReviewDescription();
        reviewDescription.setReviewId(reviewResult.getReviewId());
        reviewDescription.setTitle("Title");
        reviewDescription.setDescription("Nice book");
        reviewDescription.setCustomerId(1234);
        reviewDescription.setRating(4);
        reviewDescription.setStatus(ReviewDescription.StatusEnum.pending);
        reviewDescriptionRepository.save(reviewDescription);

        ReviewDescription reviewDescription2 = new ReviewDescription();
        reviewDescription2.setReviewId(reviewResult.getReviewId());
        reviewDescription2.setTitle("Title 2");
        reviewDescription2.setDescription("Nice book 2");
        reviewDescription2.setCustomerId(4567);
        reviewDescription2.setRating(7);
        reviewDescription2.setStatus(ReviewDescription.StatusEnum.pending);
        reviewDescriptionRepository.save(reviewDescription2);

        reviewDescriptionRepository.deleteAllByReviewId(reviewResult.getReviewId());

        assertThat(reviewDescriptionRepository.findAllByReviewId(reviewResult.getReviewId()), hasSize(0));
    }

    @Test
    void findOneByReviewIdAndCustomerId() {
        Review review = new Review();
        review.setStoreId(1);
        review.setProductId(3452);
        Review reviewResult = reviewRepository.save(review);

        ReviewDescription reviewDescription = new ReviewDescription();
        reviewDescription.setReviewId(reviewResult.getReviewId());
        reviewDescription.setTitle("Title");
        reviewDescription.setDescription("Nice book");
        reviewDescription.setCustomerId(1234);
        reviewDescription.setRating(4);
        reviewDescription.setStatus(ReviewDescription.StatusEnum.pending);
        ReviewDescription reviewDescriptionResult = reviewDescriptionRepository.save(reviewDescription);

        ReviewDescription reviewDescription2 = new ReviewDescription();
        reviewDescription2.setReviewId(reviewResult.getReviewId());
        reviewDescription2.setTitle("Title 2");
        reviewDescription2.setDescription("Nice book 2");
        reviewDescription2.setCustomerId(4567);
        reviewDescription2.setRating(7);
        reviewDescription2.setStatus(ReviewDescription.StatusEnum.pending);
        reviewDescriptionRepository.save(reviewDescription2);

        Optional<ReviewDescription> oneByReviewIdAndCustomerId =
                reviewDescriptionRepository.findOneByReviewIdAndCustomerId(reviewResult.getReviewId(), reviewDescription.getCustomerId());

        assertThat(oneByReviewIdAndCustomerId.isPresent(), is(true));
        assertThat(oneByReviewIdAndCustomerId.get().getReviewId(), is(reviewDescriptionResult.getReviewId()));
        assertThat(oneByReviewIdAndCustomerId.get().getTitle(), is(reviewDescriptionResult.getTitle()));
        assertThat(oneByReviewIdAndCustomerId.get().getDescription(), is(reviewDescriptionResult.getDescription()));
    }
}