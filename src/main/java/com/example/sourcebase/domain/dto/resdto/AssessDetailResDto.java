package com.example.sourcebase.domain.dto.resdto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AssessDetailResDto {
    Long id;
    AssessResDTO assess;
    CriteriaResDTO criteria;
    QuestionResDTO question;
    int value;
    String description;
    boolean isComment;
}
