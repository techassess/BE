package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.AssessDetail;
import com.example.sourcebase.domain.dto.reqdto.AssessDetailReqDTO;
import com.example.sourcebase.domain.dto.resdto.AssessDetailResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssessDetailMapper {
    AssessDetailMapper INSTANCE = Mappers.getMapper(AssessDetailMapper.class);
    AssessDetailResDto toAssessDetailResDto(AssessDetail assessDetail);
    AssessDetail toAssessDetail(AssessDetailReqDTO assessDetailDto);
}
