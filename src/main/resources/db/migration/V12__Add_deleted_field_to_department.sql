ALTER TABLE department
    ADD deleted BIT(1) NULL;
UPDATE department
SET deleted = 0;

ALTER TABLE department
    MODIFY deleted BIT(1) NOT NULL;

ALTER TABLE department_criterias
    MODIFY question_id BIGINT NULL;