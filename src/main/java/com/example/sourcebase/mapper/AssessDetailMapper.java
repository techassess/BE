package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.AssessDetail;
import com.example.sourcebase.domain.dto.reqdto.AssessDetailReqDTO;
import com.example.sourcebase.domain.dto.reqdto.AssessDetailUpdateReqDTO;
import com.example.sourcebase.domain.dto.resdto.AssessDetailResDto;
import com.example.sourcebase.domain.dto.resdto.AssessDetailUpdateResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssessDetailMapper {
    AssessDetailResDto toAssessDetailResDto(AssessDetail assessDetail);

    @Mapping(source = "questionId", target = "question.id")
    @Mapping(source = "criteriaId", target = "criteria.id")
    void updateAssessDetailDto(AssessDetailUpdateReqDTO dto, @MappingTarget AssessDetail entity);


    @Mapping(source = "assessId", target = "assess.id")
    @Mapping(source = "criteriaId", target = "criteria.id")
    @Mapping(source = "questionId", target = "question.id")
    AssessDetail toAssessDetail(AssessDetailReqDTO assessDetailDto);

    @Mapping(source = "assessId", target = "assess.id")
    @Mapping(source = "criteria.id", target = "criteria.id")
    @Mapping(source = "question.id", target = "question.id")
    AssessDetail toAssessDetail(AssessDetailResDto assessDetailDto);

    List<AssessDetail> reqToEntityList(List<AssessDetailReqDTO> assessDetailReqDtos);

    List<AssessDetail> resToEntityList(List<AssessDetailResDto> assessDetailResDtos);

    List<AssessDetailResDto> entityToResList(List<AssessDetail> assessDetails);

    List<AssessDetailReqDTO> entityToReqList(List<AssessDetail> assessDetails);
}
