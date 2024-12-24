package com.example.sourcebase.domain.dto.reqdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.example.sourcebase.domain.Question}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionReqDto implements Serializable {
    @NotBlank(message = "Title cannot be blank")
    @Pattern(
            regexp = "[A-Za-zÀ-ỹ\\\\s]+",
            message = "Title can only contain letters and spaces"
    )
    private String title;

    @NotNull(message = "Point cannot be null")
    @Size(min = 0, message = "Point must be more than 0")
    private int point;

    @NotNull(message = "Criteria ID cannot be null")
    private Long criteriaId;

    @NotNull(message = "Answer IDs cannot be null")
    private List<Long> answerIds;
}