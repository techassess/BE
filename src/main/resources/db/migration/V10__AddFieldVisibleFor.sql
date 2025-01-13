ALTER TABLE criterias
    ADD visible_for VARCHAR(255) NULL;
UPDATE criterias SET visible_for = "ALL_MEMBER";
UPDATE criterias SET visible_for = "SELF" WHERE id = 6 AND id = 7;
UPDATE criterias set visible_for = "MANAGER" where id = 8;
