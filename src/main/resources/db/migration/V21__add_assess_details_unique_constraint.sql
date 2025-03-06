alter table assess_details
    add constraint fk_unique_assess_details unique (assess_id, criteria_id, question_id);
