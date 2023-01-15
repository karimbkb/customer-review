-- postgres db schema
SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = ON;
SET check_function_bodies = FALSE;
SET client_min_messages = WARNING;

CREATE SCHEMA IF NOT EXISTS customer_review;

CREATE TABLE IF NOT EXISTS customer_review.review
(
    id         UUID NOT NULL,
    product_id UUID NOT NULL,
    store_id   INTEGER,
    version    INTEGER DEFAULT 1
);

CREATE TABLE IF NOT EXISTS customer_review.review_description
(
    id                     UUID NOT NULL,
    review_id              UUID NOT NULL,
    customer_id            UUID NOT NULL,
    title                  TEXT,
    description            TEXT,
    first_name             TEXT,
    last_name              TEXT,
    rating                 TEXT,
    status                 TEXT,
    created_at             TIMESTAMP WITH TIME ZONE,
    updated_at             TIMESTAMP WITH TIME ZONE,
    version                INTEGER DEFAULT 1
);

ALTER TABLE ONLY customer_review.review ADD CONSTRAINT customer_review_pkey PRIMARY KEY (id);
ALTER TABLE ONLY customer_review.review_description ADD CONSTRAINT customer_review_description_pkey PRIMARY KEY (id);
ALTER TABLE ONLY customer_review.review_description ADD CONSTRAINT fk_review_description_review_id FOREIGN KEY (review_id) REFERENCES customer_review.review(id);
