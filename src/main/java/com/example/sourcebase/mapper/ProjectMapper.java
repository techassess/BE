package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.Project;
import com.example.sourcebase.domain.User;
import com.example.sourcebase.domain.dto.reqdto.ProjectReqDTO;
import com.example.sourcebase.domain.dto.resdto.ProjectResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, UserMapper.class, UserProjectMapper.class})
public interface ProjectMapper {
    @Mapping(source = "startDay", target = "startDay")
    @Mapping(source = "endDay", target = "endDay")
    @Mapping(source = "departmentId", target = "department.id")
    @Mapping(source = "leaderId", target = "leader.id")
    Project toEntity(ProjectReqDTO dto);

    @Mapping(source = "startDay", target = "startDay")
    @Mapping(source = "endDay", target = "endDay")
    @Mapping(source = "leader.id", target = "leaderId")
    ProjectResDTO toResponseDTO(Project entity);

    List<ProjectResDTO> toProjectResDTOs(List<Project> projects);

    List<UserResDTO> toUserDTOs(List<User> users);


}
