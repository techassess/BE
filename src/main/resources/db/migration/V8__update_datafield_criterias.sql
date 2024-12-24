ALTER TABLE answers
    ADD is_deleted BIT(1) NULL;
UPDATE answers SET is_deleted = 0;

ALTER TABLE answers
    MODIFY is_deleted BIT (1) NOT NULL;

ALTER TABLE criterias
    ADD is_deleted BIT(1) NULL;
UPDATE criterias SET is_deleted = 0;

ALTER TABLE criterias
    MODIFY is_deleted BIT (1) NOT NULL;

ALTER TABLE questions
    ADD is_deleted BIT(1) NULL;
UPDATE questions SET is_deleted = 0;

ALTER TABLE questions
    MODIFY is_deleted BIT (1) NOT NULL;

ALTER TABLE assess_details
    MODIFY value INT NOT NULL;