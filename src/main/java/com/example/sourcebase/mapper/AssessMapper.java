package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.Assess;
import com.example.sourcebase.domain.dto.reqdto.AssessReqDTO;
import com.example.sourcebase.domain.dto.resdto.AssessResDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AssessDetailMapper.class})
public interface AssessMapper {
//    AssessMapper INSTANCE = Mappers.getMapper(AssessMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "toUser.id", target = "toUserId")
    @Mapping(source = "assessDetails", target = "assessDetails")
    AssessResDTO toAssessResDto(Assess assess);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "toUserId", target = "toUser.id")
    @Mapping(target = "assessDetails", source = "assessDetails")
    Assess toAssess(AssessReqDTO assessReqDto);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "toUserId", target = "toUser.id")
    @Mapping(target = "assessDetails", source = "assessDetails")
    Assess partialUpdate(AssessReqDTO assessReqDto, @MappingTarget Assess assess);
}
