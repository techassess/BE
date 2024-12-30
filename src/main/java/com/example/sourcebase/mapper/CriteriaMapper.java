package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.Answer;
import com.example.sourcebase.domain.Criteria;
import com.example.sourcebase.domain.Question;
import com.example.sourcebase.domain.dto.reqdto.CriteriaReqDTO;
import com.example.sourcebase.domain.dto.resdto.AnswerResDTO;
import com.example.sourcebase.domain.dto.resdto.CriteriaResDTO;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CriteriaMapper {
    CriteriaMapper INSTANCE = Mappers.getMapper(CriteriaMapper.class);

    // Mapping từ CriteriaReqDTO sang thực thể Criteria
    Criteria toEntity(CriteriaReqDTO reqDTO);

    // Mapping từ Criteria sang DTO
    CriteriaResDTO toCriteriaResDTO(Criteria criteria);

    // Mapping từ Answer sang DTO
    AnswerResDTO toAnswerResDTO(Answer answer);

    // Mapping từ Question sang DTO
    QuestionResDTO toQuestionResDTO(Question question);

    // Cập nhật một phần dữ liệu Criteria
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Criteria partialUpdate(CriteriaReqDTO reqDTO, @MappingTarget Criteria criteria);
}
