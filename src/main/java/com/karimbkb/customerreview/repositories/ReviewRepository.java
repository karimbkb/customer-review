package com.karimbkb.customerreview.repositories;

import com.karimbkb.customerreview.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByStoreIdAndProductId(int storeId, int productId);

    Optional<Review> findByReviewId(long reviewId);
}
