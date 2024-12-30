package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.Answer;
import com.example.sourcebase.domain.Criteria;
import com.example.sourcebase.domain.dto.reqdto.CriteriaReqDTO;
import com.example.sourcebase.domain.dto.resdto.AnswerResDTO;
import com.example.sourcebase.domain.dto.resdto.CriteriaResDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CriteriaMapper {
    CriteriaMapper INSTANCE = Mappers.getMapper(CriteriaMapper.class);

    Criteria toEntity(CriteriaReqDTO reqDTO);

    CriteriaResDTO toCriteriaResDTO(Criteria criteria);

    Criteria toCriteria(CriteriaResDTO criteriaResDTO);

    AnswerResDTO toAnswerResDTO(Answer answer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Criteria partialUpdate(CriteriaReqDTO reqDTO, @MappingTarget Criteria criteria);
}
