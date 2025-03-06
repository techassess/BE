package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.Assess;
import com.example.sourcebase.domain.dto.reqdto.AssessReqDTO;
import com.example.sourcebase.domain.dto.resdto.AssessResDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED, uses = {AssessDetailMapper.class, ProjectMapper.class})
public interface AssessMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "toUser.id", target = "toUserId")
    AssessResDTO toAssessResDto(Assess assess);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "toUserId", target = "toUser.id")
    @Mapping(source = "projectId", target = "project.id")
    Assess toAssess(AssessReqDTO assessReqDto);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "toUserId", target = "toUser.id")
    @Mapping(source = "projectId", target = "project.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Assess updateAssess(AssessReqDTO assessReqDto, @MappingTarget Assess assess);
}
