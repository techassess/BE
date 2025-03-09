package com.example.sourcebase.domain.dto.resdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResDTO {
    Long id;
    String title;
    int value;
}