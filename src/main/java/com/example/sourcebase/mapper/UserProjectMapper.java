package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.UserProject;
import com.example.sourcebase.domain.dto.resdto.user.UserProjectResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserProjectMapper {
    @Mapping(target = "project.id", source = "projectId")
    @Mapping(target = "user.id", source = "userId")
    UserProject toEntity(UserProjectResDTO userProjectResDTO);

    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "userId", source = "user.id")
    UserProjectResDTO toResponseDTO(UserProject userProject);

    List<UserProjectResDTO> toUserProjectResDTOs(List<UserProject> userProjects);
}
