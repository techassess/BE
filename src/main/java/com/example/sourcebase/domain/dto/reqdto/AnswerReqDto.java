package com.example.sourcebase.domain.dto.reqdto;

import com.example.sourcebase.domain.Question;
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
    @NotNull(message = "ID cannot be null")
    Long id;

    @NotBlank(message = "Title cannot be blank")
    @Pattern(
            regexp = "[A-Za-zÀ-ỹ0-9\\s\\p{Punct}]+",
            message = "Title can contain letters, numbers, spaces, and all special characters"
    )
    String title;

    @NotNull(message = "Value cannot be null")
    @Positive
    Integer value;
}