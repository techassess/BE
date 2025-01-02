CREATE TABLE department
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NULL,
    CONSTRAINT pk_department PRIMARY KEY (id)
);

INSERT INTO department (name) VALUES ('Vận Hành');

CREATE TABLE department_criterias
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    department_id BIGINT                NOT NULL,
    criterias_id  BIGINT                NOT NULL,
    question_id   BIGINT                NOT NULL,
    CONSTRAINT pk_department_criterias PRIMARY KEY (id)
);

ALTER TABLE projects
    ADD department_id BIGINT NULL;

UPDATE projects SET department_id = 1;

ALTER TABLE projects
    MODIFY department_id BIGINT NOT NULL;

ALTER TABLE department_criterias
    ADD CONSTRAINT uc_57e59fabb275cacec35bf6285 UNIQUE (department_id, criterias_id, question_id);

ALTER TABLE department_criterias
    ADD CONSTRAINT FK_DEPARTMENT_CRITERIAS_ON_CRITERIAS FOREIGN KEY (criterias_id) REFERENCES criterias (id);

ALTER TABLE department_criterias
    ADD CONSTRAINT FK_DEPARTMENT_CRITERIAS_ON_DEPARTMENT FOREIGN KEY (department_id) REFERENCES department (id);

ALTER TABLE department_criterias
    ADD CONSTRAINT FK_DEPARTMENT_CRITERIAS_ON_QUESTION FOREIGN KEY (question_id) REFERENCES questions (id);

ALTER TABLE projects
    ADD CONSTRAINT FK_PROJECTS_ON_DEPARTMENT FOREIGN KEY (department_id) REFERENCES department (id);

ALTER TABLE assess_details
    MODIFY value INT NOT NULL;