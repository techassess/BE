package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.Answer;
import com.example.sourcebase.domain.dto.reqdto.AnswerReqDto;
import com.example.sourcebase.domain.dto.resdto.AnswerResDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnswerMapper {
    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    Answer toEntity(AnswerReqDto answerReqDto);

    AnswerResDTO toAnswerResDto(Answer answer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Answer partialUpdate(AnswerReqDto answerReqDto, @MappingTarget Answer answer);
}