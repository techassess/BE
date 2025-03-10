ALTER TABLE answers
    ADD deleted_at datetime NULL;

ALTER TABLE assesses
    ADD deleted_at datetime NULL;

ALTER TABLE criterias
    ADD deleted_at datetime NULL;

ALTER TABLE department
    ADD deleted_at datetime NULL;

ALTER TABLE projects
    ADD deleted_at datetime NULL;

ALTER TABLE questions
    ADD deleted_at datetime NULL;

ALTER TABLE users
    ADD deleted_at datetime NULL;

ALTER TABLE users
    DROP COLUMN create_at;

ALTER TABLE users
    DROP COLUMN created_by;

ALTER TABLE users
    DROP COLUMN modified_at;

ALTER TABLE users
    DROP COLUMN modified_by;

ALTER TABLE users
    DROP COLUMN is_deleted;

ALTER TABLE department
    DROP COLUMN deleted;

ALTER TABLE answers
    DROP COLUMN is_deleted;

ALTER TABLE criterias
    DROP COLUMN is_deleted;

ALTER TABLE questions
    DROP COLUMN is_deleted;