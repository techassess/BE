package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.Assess;
import com.example.sourcebase.domain.dto.reqdto.AssessReqDTO;
import com.example.sourcebase.domain.dto.resdto.AssessDetailResDto;
import com.example.sourcebase.domain.dto.resdto.AssessResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssessMapper {
    AssessMapper INSTANCE = Mappers.getMapper(AssessMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "toUser.id", target = "toUserId")
    AssessResDTO toAssessResDto(Assess assess);

    Assess toAssess(AssessReqDTO assessReqDto);
}
