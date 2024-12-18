package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.Answer;
import com.example.sourcebase.domain.Criteria;
import com.example.sourcebase.domain.Question;
import com.example.sourcebase.domain.dto.reqdto.CriteriaReqDTO;
import com.example.sourcebase.domain.dto.resdto.AnswerResDTO;
import com.example.sourcebase.domain.dto.resdto.CriteriaResDTO;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CriteriaMapper {
    CriteriaMapper INSTANCE = Mappers.getMapper(CriteriaMapper.class);
    Criteria reqDtoToItem(CriteriaReqDTO reqDTO);
    CriteriaResDTO toCriteriaResDTO(Criteria criteria);
    AnswerResDTO toAnswerResDTO(Answer answer);
}
