package com.example.sourcebase.domain.dto.reqdto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Validated
@ToString
public class AssessDetailReqDTO {
    Long assessId;
    Long criteriaId;
    Long questionId;
    Integer value;
    String description;
    boolean isComment;
}
