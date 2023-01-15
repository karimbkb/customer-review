package com.karimbkb.customerreview.repositories;

import com.karimbkb.customerreview.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Optional<Review> findByStoreIdAndProductId(Integer storeId, UUID productId);
}
