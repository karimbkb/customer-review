ALTER TABLE review ADD COLUMN version integer NOT NULL default 1;
ALTER TABLE review_description ADD COLUMN version integer NOT NULL default 1;