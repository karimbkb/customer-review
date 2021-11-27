CREATE TABLE IF NOT EXISTS review
(
    review_id  SERIAL PRIMARY KEY,
    product_id integer NOT NULL,
    store_id   integer NOT NULL
);

CREATE TABLE IF NOT EXISTS review_description
(
    review_description_id  SERIAL PRIMARY KEY,
    review_id              integer NOT NULL REFERENCES review (review_id),
    customer_id            integer NOT NULL,
    title                  varchar(128)   NOT NULL,
    description            varchar(512)   NOT NULL,
    first_name             varchar(32)   NOT NULL,
    last_name              varchar(32)   NOT NULL,
    rating                 varchar(32)   NOT NULL,
    status                 varchar(32)   NOT NULL,
    created_at             timestamp NOT NULL,
    updated_at             timestamp NOT NULL
);

-- CREATE TABLE IF NOT EXISTS review_rating
-- (
--     review_rating_id  SERIAL PRIMARY KEY,
--     review_id  SERIAL PRIMARY KEY REFERENCES review (review_id),
--     reviews_count integer NOT NULL
--     rating_summary integer NOT NULL
-- );