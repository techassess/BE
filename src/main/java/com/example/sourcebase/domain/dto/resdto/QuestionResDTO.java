package com.example.sourcebase.domain.dto.resdto;

import com.example.sourcebase.domain.Answer;
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
public class QuestionResDTO {
    Long id;
    String title;
    int point;
    boolean isDeleted = false;
    List<AnswerResDTO> answers;
}
