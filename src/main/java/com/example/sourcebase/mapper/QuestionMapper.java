package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.Question;
import com.example.sourcebase.domain.dto.reqdto.QuestionReqDto;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    QuestionResDTO toQuestionResDTO(Question question);

    Question toQuestion(QuestionReqDto questionReqDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Question partialUpdate(QuestionReqDto questionReqDto, @MappingTarget Question question);
}
