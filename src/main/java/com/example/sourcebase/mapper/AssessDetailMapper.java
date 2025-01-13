package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.AssessDetail;
import com.example.sourcebase.domain.dto.reqdto.AssessDetailReqDTO;
import com.example.sourcebase.domain.dto.resdto.AssessDetailResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AssessMapper.class})
public interface AssessDetailMapper {
//    AssessDetailMapper INSTANCE = Mappers.getMapper(AssessDetailMapper.class);

    @Mapping(source = "assess", target = "assess", ignore = true)
    @Mapping(source = "criteria.id", target ="criteria.id")
    @Mapping(source = "question.id", target = "question.id")
    AssessDetailResDto toAssessDetailResDto(AssessDetail assessDetail);

    @Mapping(source = "assessId", target = "assess.id", ignore = true)
    @Mapping(source = "criteriaId", target = "criteria.id")
    @Mapping(source = "questionId", target = "question.id")
    AssessDetail toAssessDetail(AssessDetailReqDTO assessDetailDto);

    List<AssessDetailResDto> toResDtoList(List<AssessDetail> assessDetails);

    List<AssessDetail> toEntityList(List<AssessDetailReqDTO> assessDetailDtos);
}
