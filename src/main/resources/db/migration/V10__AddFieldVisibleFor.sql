ALTER TABLE criterias
    ADD visible_for VARCHAR(255) NULL;
UPDATE criterias SET visible_for = "ALL_MEMBER";