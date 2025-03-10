package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.Answer;
import com.example.sourcebase.domain.Criteria;
import com.example.sourcebase.domain.Question;
import com.example.sourcebase.domain.dto.reqdto.CriteriaReqDTO;
import com.example.sourcebase.domain.dto.resdto.AnswerResDTO;
import com.example.sourcebase.domain.dto.resdto.CriteriaResDTO;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import com.example.sourcebase.domain.enumeration.ETypeCriteria;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CriteriaMapper {
    @Mapping(target = "department.id", source = "departmentId")
    Criteria toEntity(CriteriaReqDTO reqDTO);

    CriteriaResDTO toCriteriaResDTO(Criteria criteria);

    Criteria toCriteria(CriteriaResDTO criteriaResDTO);

    AnswerResDTO toAnswerResDTO(Answer answer);

    QuestionResDTO toQuestionResDTO(Question question);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Criteria partialUpdate(CriteriaReqDTO reqDTO, @MappingTarget Criteria criteria);

    default ETypeCriteria toETypeCriteria(String type) {
        return ETypeCriteria.valueOf(type);
    }

    default String fromETypeCriteria(ETypeCriteria type) {
        return type.name();
    }

    Set<Criteria> reqToEntitySet(Set<CriteriaReqDTO> criteriaReqDTOS);

    Set<Criteria> resToEntitySet(Set<CriteriaResDTO> criteriaResDTOS);

    List<CriteriaResDTO> toCriteriaResDTOList(List<Criteria> criteriaList);
}
