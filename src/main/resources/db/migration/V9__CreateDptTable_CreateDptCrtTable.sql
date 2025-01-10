CREATE TABLE department
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NULL,
    CONSTRAINT pk_department PRIMARY KEY (id)
);
INSERT INTO department (name)
VALUES ('Product'),
       ('Vận Hành'),
       ('OutSource'),
       ('QC'),
       ('Bảo mật'),
       ('DevOps'),
       ('R&D');


CREATE TABLE department_criterias
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    department_id BIGINT                NOT NULL,
    criterias_id  BIGINT                NOT NULL,
    question_id   BIGINT                NULL,
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

INSERT INTO department_criterias (department_id, criterias_id, question_id) VALUES
              (1, 1, 1),
              (1, 1, 2),
              (1, 1, 3),
              (1, 2, 4),
              (1, 2, 5),
              (1, 2, 6),
              (1, 3, 7),
              (1, 3, 8),
              (1, 3, 9),
              (1, 4, 10),
              (1, 6, null),
              (1, 7, null),
              (1, 8, null),
              (2, 10, 12),
              (2, 10, 13),
              (2, 12, 14),
              (2, 12, 15),
              (2, 13, 16),
              (2, 13, 17),
              (2, 14, 18),
              (2, 14, 19),
              (2, 15, 20),
              (2, 15, 21),
              (2, 6, null),
              (2, 7, null),
              (2, 8, null);

