package com.example.sourcebase.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OverallOfACriterion {
    Long criteriaId;
    Integer criteriaPoint;
    String criteriaTitle;
    Double selfPoint;
    Double teamPoint;
    Double managerPoint;
    Double overallPoint;
}
