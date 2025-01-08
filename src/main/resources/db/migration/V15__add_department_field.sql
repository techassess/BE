ALTER TABLE users
    ADD department_id BIGINT NULL;

update users set  department_id = 1 ;