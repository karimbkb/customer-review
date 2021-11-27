package com.karimbkb.customerreview.repositories;

import com.karimbkb.customerreview.models.ReviewDescription;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest()
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewDescriptionRepositoryNotInMemoryTest {

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:11.5-alpine")
            .withUsername("root")
            .withPassword("root");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", container::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", container::getPassword);
    }

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

    @BeforeAll
    static void setUp() {
        ClassicConfiguration configuration = new ClassicConfiguration();
        configuration.setDataSource(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        configuration.setLocations(new Location("classpath:db/migration"));

        Flyway flyway = new Flyway(configuration);
        flyway.migrate();
    }

    @Test
    @Sql(scripts = "/scripts/init_reviews.sql")
    void shouldLoadReviewDescriptionById() {
        assertThat(entityManager, is(notNullValue()));
        assertThat(reviewDescriptionRepository, is(notNullValue()));
        assertThat(reviewRepository, is(notNullValue()));
        assertThat(testEntityManager, is(notNullValue()));
        assertThat(dataSource, is(notNullValue()));

        ReviewDescription result = reviewDescriptionRepository.findOneByReviewDescriptionId(1L);

        assertThat(result, is(notNullValue()));
    }

    @Test
    @Sql(scripts = "/scripts/init_reviews.sql")
    void deleteAllByReviewId() {
        reviewDescriptionRepository.deleteAllByReviewId(1L);
        assertThat(reviewDescriptionRepository.findAllByReviewId(1L), hasSize(0));
    }

    @Test
    @Sql(scripts = "/scripts/init_reviews_with_same_customer_id.sql")
    void findOneByReviewIdAndCustomerId() {
        Optional<ReviewDescription> oneByReviewIdAndCustomerId =
                reviewDescriptionRepository.findOneByReviewIdAndCustomerId(1L, 5677);

        assertThat(oneByReviewIdAndCustomerId.isPresent(), is(true));
        assertThat(oneByReviewIdAndCustomerId.get().getReviewId(), is(1L));
        assertThat(oneByReviewIdAndCustomerId.get().getTitle(), is("Nice product"));
        assertThat(oneByReviewIdAndCustomerId.get().getDescription(), is("I love it!"));
    }
}