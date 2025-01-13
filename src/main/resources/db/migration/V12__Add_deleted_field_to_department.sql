ALTER TABLE department
    ADD deleted BIT(1) DEFAULT 0 NULL;

ALTER TABLE department
    MODIFY deleted BIT(1) NOT NULL;

ALTER TABLE department_criterias
    MODIFY question_id BIGINT NULL;