package com.example.sourcebase.service;

import com.example.sourcebase.domain.dto.reqdto.ProjectReqDTO;
import com.example.sourcebase.domain.dto.resdto.ProjectResDTO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface IProjectService {
    ProjectResDTO addProject(ProjectReqDTO projectRequest);

    List<ProjectResDTO> getAll();

    ProjectResDTO getProjectById(Long id);

    boolean deleteProject(Long id);

    ProjectResDTO updateProject(Long id, ProjectReqDTO projectReqDTO);

    ProjectResDTO addEmployeesToProject(Long projectId, ProjectReqDTO requestDTO);
}
