package com.karimbkb.customerreview.repositories;

import com.karimbkb.customerreview.domain.ReviewDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewDescriptionRepository extends JpaRepository<ReviewDescription, UUID> {
    Optional<ReviewDescription> findOneByReviewIdAndCustomerId(UUID id, UUID customerId);

    List<ReviewDescription> findAllByReviewId(UUID reviewId);
}
