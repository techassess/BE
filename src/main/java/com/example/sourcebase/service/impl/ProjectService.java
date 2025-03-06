package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Project;
import com.example.sourcebase.domain.User;
import com.example.sourcebase.domain.UserProject;
import com.example.sourcebase.domain.dto.reqdto.ProjectReqDTO;
import com.example.sourcebase.domain.dto.resdto.DepartmentResDTO;
import com.example.sourcebase.domain.dto.resdto.ProjectResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserProjectResDTO;
import com.example.sourcebase.exception.AppException;
import com.example.sourcebase.mapper.DepartmentMapper;
import com.example.sourcebase.mapper.ProjectMapper;
import com.example.sourcebase.mapper.UserMapper;
import com.example.sourcebase.repository.IProjectRepository;
import com.example.sourcebase.repository.IUserProjectRepository;
import com.example.sourcebase.repository.IUserRepository;
import com.example.sourcebase.service.IProjectService;
import com.example.sourcebase.util.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProjectService implements IProjectService {

    IProjectRepository projectRepository;
    IUserProjectRepository userProjectRepository;
    IUserRepository userRepository;

    ProjectMapper projectMapper;
    UserMapper userMapper;
    DepartmentMapper departmentMapper;

    @Override
    @Transactional
    public ProjectResDTO addProject(ProjectReqDTO projectRequest) {
        if (projectRepository.existsByName(projectRequest.getName())) {
            throw new AppException(ErrorCode.PPOJECT_IS_EXIST);
        }
        validateProject(projectRequest);
        Project project = projectMapper.toEntity(projectRequest);
        project.setLeader(null);
        Project savedProject = projectRepository.save(project);

        return projectMapper.toResponseDTO(savedProject);
    }

    @Override
    public List<ProjectResDTO> getAll() {
        // Lấy tất cả các dự án từ repository
        List<Project> projects = projectRepository.findAll();
        List<ProjectResDTO> projectResDTOS = new ArrayList<>();

        // Duyệt qua từng dự án
        for (Project project : projects) {
            // Gọi hàm getProjectById để lấy ProjectResDTO cho từng dự án
            ProjectResDTO projectResDTO = getProjectById(project.getId());
            if (project.getLeader() != null && project.getDepartment().getId() != null) {
                projectResDTO.setLeaderId(project.getLeader().getId());
            }
            projectResDTOS.add(projectResDTO);
        }

        return projectResDTOS;
    }

    @Override
    public ProjectResDTO getProjectById(Long id) {
        System.out.println("id cuar project: " + id);
        Project project = projectRepository.findById(id).orElse(null);

        ProjectResDTO projectResDTO = projectMapper.toResponseDTO(project);
        for (var i = 0; i < project.getUserProjects().size(); i++) {
            for (var j = 0; j < project.getUserProjects().size(); j++) {
                User user = userRepository.findById(project.getUserProjects().get(i).getUser().getId()).orElse(null);
                projectResDTO.getUserProjects().get(i).setUserId(user.getId());
                projectResDTO.getUserProjects().get(i).setProjectId(project.getId());
            }
        }

        return projectResDTO;
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        }
        projectRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteEmployeeFromProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(project.getLeader().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.LEADER_CANNOT_DELETE_IN_PROJECT);
        }
        UserProject userProject = userProjectRepository.findByProject_IdAndUser_Id(projectId, userId);
        if (userProject == null) {
            throw new AppException(ErrorCode.USER_PROJECT_NOT_FOUND);
        }
        userProjectRepository.delete(userProject);
    }



    @Override
    @Transactional
    public ProjectResDTO updateProject(Long id, ProjectReqDTO projectReqDTO) {

        validateProject(projectReqDTO);
        return projectRepository.findById(id).map(existingProject -> {

            existingProject.setName(projectReqDTO.getName());
            existingProject.setStartDay(projectReqDTO.getStartDay());
            existingProject.setEndDay(projectReqDTO.getEndDay());
            Project updatedProject = projectRepository.save(existingProject);
            return projectMapper.toResponseDTO(updatedProject);
        }).orElse(null);
    }

    @Override
    @Transactional
    public ProjectResDTO updateLeader(Long id, ProjectReqDTO projectReqDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        if (projectReqDTO.getLeaderId() != null) {
            User leader = userRepository.findById(projectReqDTO.getLeaderId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            project.setLeader(leader);
        } else {
            project.setLeader(null);
        }
        Project updatedProject = projectRepository.save(project);
        DepartmentResDTO departmentResDTO = departmentMapper.toResponseDTO(updatedProject.getDepartment());

        List<UserProjectResDTO> userProjectResDTOS = project.getUserProjects().stream()
                .map(userProject -> {
                    UserProjectResDTO userProjectResDTO = new UserProjectResDTO();
                    userProjectResDTO.setProjectId(project.getId());
                    userProjectResDTO.setUserId(userProject.getUser().getId());
                    userProjectResDTO.setDepartment(departmentResDTO);
                    return userProjectResDTO;
                })
                .collect(Collectors.toList());

        ProjectResDTO responseDTO = projectMapper.toResponseDTO(updatedProject);
        responseDTO.setUserProjects(userProjectResDTOS);
        responseDTO.setLeaderId(updatedProject.getLeader() != null ? updatedProject.getLeader().getId() : null);

        return responseDTO;
    }

    @Override
    @Transactional
    public ProjectResDTO addEmployeesToProject(Long projectId, ProjectReqDTO requestDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        if (project.getLeader() == null) {
            User leader = userRepository.findById(requestDTO.getLeaderId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            project.setLeader(leader);
        }
        projectRepository.save(project);
        List<User> usersToAdd = userRepository.findAllById(requestDTO.getEmployeeIds());

        if (usersToAdd.size() != requestDTO.getEmployeeIds().size()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        List<UserProject> newUserProjects = usersToAdd.stream()
                .map(user -> {
                    UserProject userProject = new UserProject();
                    userProject.setProject(project);
                    userProject.setUser(user);
                    return userProject;
                })
                .collect(Collectors.toList());

        userProjectRepository.saveAll(newUserProjects);

        // Map users to DTOs
        List<UserProjectResDTO> userProjectResDTOS = newUserProjects.stream()
                .map(userProject -> {
                    UserProjectResDTO userProjectResDTO = new UserProjectResDTO();
                    userProjectResDTO.setProjectId(projectId); // Set the project ID();
                    userProjectResDTO.setUserId(userProject.getUser().getId());
                    return userProjectResDTO;
                })
                .collect(Collectors.toList());
        // Create and set the ProjectResDTO
        ProjectResDTO responseDTO = new ProjectResDTO();
        responseDTO.setId(project.getId());
        responseDTO.setName(project.getName());
        responseDTO.setStartDay(project.getStartDay());
        responseDTO.setEndDay(project.getEndDay());
        responseDTO.setUserProjects(userProjectResDTOS);
        responseDTO.setLeaderId(project.getLeader() != null ? project.getLeader().getId() : null);
        return responseDTO;
    }

    private void validateProject(ProjectReqDTO projectReqDTO) {

        LocalDate currentDate = LocalDate.now();

        if (projectReqDTO.getStartDay().isAfter(currentDate)) {
            throw new AppException(ErrorCode.INVALID_START_DATE);
        }
        if (projectReqDTO.getEndDay().isBefore(currentDate)) {
            throw new AppException(ErrorCode.INVALID_END_DATE);
        }
        if (projectReqDTO.getEndDay().isBefore(projectReqDTO.getStartDay())) {
            throw new AppException(ErrorCode.END_DATE_BEFORE_START_DATE);
        }
    }
}
