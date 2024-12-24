package com.example.sourcebase.domain.dto.reqdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.example.sourcebase.domain.Answer}
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerReqDto implements Serializable {
    @NotBlank(message = "Title cannot be blank")
    @Pattern(
            regexp = "[A-Za-zÀ-ỹ\\\\s]+",
            message = "Title can only contain letters and spaces"
    )
    String title;

    @NotNull(message = "Value cannot be null")
    @Positive
    int value;

    @NotNull(message = "Criteria ID cannot be null")
    Long questionId;
}