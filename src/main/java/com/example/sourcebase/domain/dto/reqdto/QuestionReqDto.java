package com.example.sourcebase.domain.dto.reqdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String title;
    private int point;
    private Long criteriaId;
    // private List<Long> answerIds;
    private List<AnswerReqDto> answers;
}