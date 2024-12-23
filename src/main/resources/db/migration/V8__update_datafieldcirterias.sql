ALTER TABLE criterias
    ADD COLUMN is_deleted BIT(1) NOT NULL DEFAULT 0;

ALTER TABLE questions
    ADD COLUMN is_deleted BIT(1) NOT NULL DEFAULT 0;

ALTER TABLE assess_details
    ADD COLUMN is_deleted BIT(1) NOT NULL DEFAULT 0;

UPDATE criterias
SET is_deleted = 0;

UPDATE questions
SET is_deleted = 0;

UPDATE assess_details
SET is_deleted = 0;
