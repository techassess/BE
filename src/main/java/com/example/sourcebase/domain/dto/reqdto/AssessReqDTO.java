package com.example.sourcebase.domain.dto.reqdto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Validated
@ToString
public class AssessReqDTO {
    @NotBlank(message = "Log in to assess")
    Long userId;
    @NotBlank(message = "Please select a user to assess")
    Long toUserId;
    Integer totalPoint;
    boolean submitted;
    List<AssessDetailReqDTO> assessDetails;
}
