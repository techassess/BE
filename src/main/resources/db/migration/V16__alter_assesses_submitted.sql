ALTER TABLE assesses
    ADD submitted BIT(1) DEFAULT 0 NULL;

ALTER TABLE assesses
    MODIFY submitted BIT(1) NOT NULL;