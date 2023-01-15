INSERT INTO customer_review.review(id, product_id, store_id, version) VALUES
('f8fdfd78-936f-4ab9-b0b8-e6f6e5bb7b8a', 'ccbad298-1f9c-4290-9258-09dac2c4ab40', 3, 1);

INSERT INTO customer_review.review_description(id, review_id, customer_id, title, description, first_name, last_name, rating, status, created_at, updated_at, version) VALUES
('ac98b028-a523-4aa3-b8cc-2d0ef3a1223d', 'f8fdfd78-936f-4ab9-b0b8-e6f6e5bb7b8a', '02eceea3-30ce-4b9f-b5bd-7ab3a6fd0311', 'Coool product', 'Lorem ipsum si dolor amet.', 'James', 'Michael', 3, 'approved', now() - INTERVAL '1 DAY', now() - INTERVAL '1 DAY', 1)
