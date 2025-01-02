package com.example.sourcebase.domain.dto.reqdto;

import com.example.sourcebase.domain.Answer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.example.sourcebase.domain.Question}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionReqDto {
    @NotBlank(message = "Title cannot be blank")
    @Pattern(
            regexp = "[A-Za-zÀ-ỹ0-9\\s\\p{Punct}]+",
            message = "Title can contain letters, numbers, spaces, and all special characters"
    )
    String title;

    @NotNull(message = "Point cannot be null")
    @Min(value = 1, message = "Point must be 1 or greater")
    Integer point;


    @NotNull(message = "Answer cannot be null")
    @NotEmpty(message = "Answers cannot be empty")
    List<AnswerReqDto> answers;
}