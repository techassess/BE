package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.Project;
import com.example.sourcebase.domain.User;
import com.example.sourcebase.domain.UserProject;
import com.example.sourcebase.domain.dto.reqdto.ProjectReqDTO;
import com.example.sourcebase.domain.dto.resdto.ProjectResDTO;
import com.example.sourcebase.domain.dto.reqdto.ProjectReqDTO;
import com.example.sourcebase.domain.dto.resdto.ProjectResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserProjectResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);
    @Mapping(source = "startDay", target = "startDay")
    @Mapping(source = "endDay", target = "endDay")
    Project toEntity(ProjectReqDTO dto);

    ProjectResDTO toResponseDTO(Project entity);
    List<ProjectResDTO> toProjectResDTOs(List<Project> projects);


    List<UserResDTO> toUserDTOs(List<User> users);
    List<UserProjectResDTO> toUserProjectResDTOs(List<UserProject> userProjects);

}
