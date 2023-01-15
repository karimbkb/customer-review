CREATE UNIQUE INDEX customer_review_review_id_customer_id_unique_index
    ON customer_review.review_description (review_id, customer_id);
