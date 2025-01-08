package com.example.sourcebase.domain.dto.resdto.custom;

import com.example.sourcebase.domain.model.OverallOfACriterion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OverallRatedResDto {
    List<OverallOfACriterion> overallOfCriteria;
    Double overallPoint;
    String rank;
    Integer levelUpRecommend;
}
