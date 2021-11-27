insert into review (review_id, product_id, store_id)
VALUES (1, 8394, 2);

insert into review_description (review_description_id, review_id, customer_id, title, description, first_name,
                                last_name, rating, status, created_at, updated_at)
VALUES (1, 1, 5677, 'Perfect product', 'Awesome product, Im so impressed. really nice product', 'John', 'Doe', 7,
        'pending', '2021-09-13 13:50:25.265131', '2021-09-13 13:50:25.270304');

insert into review_description (review_description_id, review_id, customer_id, title, description, first_name,
                                last_name, rating, status, created_at, updated_at)
VALUES (2, 1, 8900, 'Nice product', 'I love it!', 'Michael', 'Jordan', 73,
        'pending', '2021-09-11 17:20:13.265131', '2021-09-12 06:50:25.270304');
