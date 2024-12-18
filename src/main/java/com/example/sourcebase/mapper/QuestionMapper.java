package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.Question;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);
    QuestionResDTO toQuestionResDTO(Question question);
}
