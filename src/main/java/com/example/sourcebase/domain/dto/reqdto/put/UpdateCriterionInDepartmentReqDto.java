package com.example.sourcebase.domain.dto.reqdto.put;

import com.example.sourcebase.domain.dto.reqdto.CriteriaReqDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCriterionInDepartmentReqDto {
    CriteriaReqDTO criteriaReqDTO;
    Long departmentId;
    Long criteriaId;
}
