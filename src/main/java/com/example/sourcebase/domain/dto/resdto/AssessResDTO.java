package com.example.sourcebase.domain.dto.resdto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AssessResDTO {
    Long id;
    Long userId;
    Long toUserId;
    String assessmentDate;
    String assessmentType;
    String totalPoint;
    boolean submitted;
    List<AssessDetailResDto> assessDetails;
}
