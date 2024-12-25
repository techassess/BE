package com.example.sourcebase.domain.dto.resdto;

import com.example.sourcebase.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResDTO {
    Long id;
    String title;
    int value;
}