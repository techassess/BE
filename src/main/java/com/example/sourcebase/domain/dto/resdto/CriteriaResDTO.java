package com.example.sourcebase.domain.dto.resdto;

import com.example.sourcebase.domain.enumeration.ETypeCriteria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CriteriaResDTO {
    Long id;
    String title;
    int point;
    ETypeCriteria visibleFor;
    List<QuestionResDTO> questions;

}
