package com.example.sourcebase.domain.dto.reqdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class AssessReqDTO {
    @NotBlank(message = "Log in to assess")
    String userId;
    @NotBlank(message = "Please select a user to assess")
    String toUserId;
    String totalPoint;
    List<AssessDetailReqDTO> assessDetails;
}
