package com.karimbkb.customerreview.repositories;

import com.karimbkb.customerreview.models.ReviewDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewDescriptionRepository extends JpaRepository<ReviewDescription, Long> {
    void deleteAllByReviewId(Long id);

    Optional<ReviewDescription> findOneByReviewIdAndCustomerId(Long id, int customerId);

    List<ReviewDescription> findAllByReviewId(Long reviewId);

    ReviewDescription findOneByReviewDescriptionId(Long id);

    Optional<ReviewDescription> findByReviewDescriptionId(long reviewDescriptionId);
}
