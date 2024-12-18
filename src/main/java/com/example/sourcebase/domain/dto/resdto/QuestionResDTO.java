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
public class QuestionResDTO {
    Long id;
    String title;
    int point;
    List<AnswerResDTO> answers;

    public QuestionResDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
