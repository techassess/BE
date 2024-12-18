CREATE DATABASE IF NOT EXISTS tech_assess360;

CREATE TABLE users
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_by   VARCHAR(255)          NULL,
    create_at    datetime              NULL,
    modified_at  datetime              NULL,
    modified_by  VARCHAR(255)          NULL,
    name         VARCHAR(255)          NULL,
    file_info_id VARCHAR(255)          NULL,
    email        VARCHAR(255)          NULL,
    phone_number VARCHAR(255)          NULL,
    dob          date                  NULL,
    gender       VARCHAR(255)          NULL,
    username     VARCHAR(255)          NULL,
    password     VARCHAR(255)          NULL,
    is_active    BIT(1)                NOT NULL,
    is_deleted   BIT(1)                NOT NULL,
    rank_id      BIGINT                NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE files
(
    id          VARCHAR(255) NOT NULL,
    file_name   VARCHAR(255) NULL,
    file_folder VARCHAR(255) NULL,
    file_url    VARCHAR(255) NULL,
    file_type   VARCHAR(255) NULL,
    cloud_id    VARCHAR(255) NULL,
    CONSTRAINT pk_files PRIMARY KEY (id)
);

CREATE TABLE projects
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(255)          NULL,
    start_day date                  NULL,
    end_day   date                  NULL,
    CONSTRAINT pk_projects PRIMARY KEY (id)
);

CREATE TABLE user_projects
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    project_id BIGINT                NULL,
    user_id    BIGINT                NULL,
    CONSTRAINT pk_user_projects PRIMARY KEY (id)
);

ALTER TABLE user_projects
    ADD CONSTRAINT FK_USER_PROJECTS_ON_PROJECT FOREIGN KEY (project_id) REFERENCES projects (id);

ALTER TABLE user_projects
    ADD CONSTRAINT FK_USER_PROJECTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE positions
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NULL,
    CONSTRAINT pk_positions PRIMARY KEY (id)
);

CREATE TABLE ranks
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    position_id BIGINT                NULL,
    level       VARCHAR(255)          NULL,
    CONSTRAINT pk_levels PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_FILE_INFO FOREIGN KEY (file_info_id) REFERENCES files (id);

ALTER TABLE ranks
    ADD CONSTRAINT FK_LEVELS_ON_POSITION FOREIGN KEY (position_id) REFERENCES positions (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_RANK FOREIGN KEY (rank_id) REFERENCES ranks (id);

CREATE TABLE IF NOT EXISTS roles
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_roles
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT                NULL,
    role_id BIGINT                NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (id)
);

ALTER TABLE  user_roles
    ADD CONSTRAINT FK_USER_ROLES_ON_ROLE FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles
    ADD CONSTRAINT FK_USER_ROLES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE criterias
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    title VARCHAR(255)          NULL,
    point INT                   NOT NULL,
    CONSTRAINT pk_criteria PRIMARY KEY (id)
);

CREATE TABLE questions
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    title       VARCHAR(255)          NULL,
    point       INT                   NOT NULL,
    criteria_id BIGINT                NULL,
    CONSTRAINT pk_questions PRIMARY KEY (id)
);

ALTER TABLE questions
    ADD CONSTRAINT FK_QUESTIONS_ON_CRITERIA FOREIGN KEY (criteria_id) REFERENCES criterias (id);



CREATE TABLE assesses
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    user_id         BIGINT                NULL,
    to_user_id      BIGINT                NULL,
    assessment_date date                  NULL,
    assessment_type VARCHAR(255)          NULL,
    total_point     INT                   NOT NULL,
    CONSTRAINT pk_assesses PRIMARY KEY (id)
);

ALTER TABLE assesses
    ADD CONSTRAINT FK_ASSESSES_ON_TO_USER FOREIGN KEY (to_user_id) REFERENCES users (id);

ALTER TABLE assesses
    ADD CONSTRAINT FK_ASSESSES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE assess_details
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    assess_id     BIGINT                NULL,
    criteria_id   BIGINT                NULL,
    question_id   BIGINT                NULL,
    value         INT                   NULL,
    `description` VARCHAR(255)          NULL,
    is_comment    BIT(1)                NOT NULL,
    CONSTRAINT pk_assess_details PRIMARY KEY (id)
);

ALTER TABLE assess_details
    ADD CONSTRAINT FK_ASSESS_DETAILS_ON_ASSESS FOREIGN KEY (assess_id) REFERENCES assesses (id);

ALTER TABLE assess_details
    ADD CONSTRAINT FK_ASSESS_DETAILS_ON_CRITERIA FOREIGN KEY (criteria_id) REFERENCES criterias (id);

ALTER TABLE assess_details
    ADD CONSTRAINT FK_ASSESS_DETAILS_ON_QUESTION FOREIGN KEY (question_id) REFERENCES questions (id);
