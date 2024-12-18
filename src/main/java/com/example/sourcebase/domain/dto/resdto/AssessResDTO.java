package com.example.sourcebase.domain.dto.resdto;

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
public class AssessResDTO {
    Long id;
    Long userId;
    Long toUserId;
    String assessmentDate;
    String assessmentType;
    String totalPoint;
    List<AssessDetailResDto> assessDetails;
}
