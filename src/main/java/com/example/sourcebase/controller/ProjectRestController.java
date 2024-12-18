package com.example.sourcebase.controller;

import com.example.sourcebase.domain.dto.reqdto.ProjectReqDTO;
import com.example.sourcebase.domain.dto.resdto.ProjectResDTO;
import com.example.sourcebase.service.IProjectService;
import com.example.sourcebase.util.ResponseData;
import com.example.sourcebase.util.SuccessCode;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

@RestController
@AllArgsConstructor
@RequestMapping("api/projects")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProjectRestController {
    IProjectService projectService;


    @PostMapping("/add")
    public ResponseEntity<ResponseData<?>> createProject(@Valid @RequestBody ProjectReqDTO projectRequest, BindingResult bindingResult) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.CREATED.getCode())
                        .message(SuccessCode.CREATED.getMessage())
                        .data(projectService.addProject(projectRequest))
                        .build());
    }

    @GetMapping
    public ResponseEntity<ResponseData<?>> getAllProject() {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(projectService.getAll())
                        .build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<?>> deleteProject(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.DELETE_SUCCESSFUL.getCode())
                        .message(SuccessCode.DELETE_SUCCESSFUL.getMessage())
                        .data(projectService.deleteProject(id))
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<?>> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(projectService.getProjectById(id))
                        .build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<?>> updateProject(@PathVariable Long id, @RequestBody ProjectReqDTO projectReqDTO) {
        ProjectResDTO updatedProject = projectService.updateProject(id, projectReqDTO);
            return ResponseEntity.ok(
                    ResponseData.builder()
                            .code(SuccessCode.UPDATE_SUCCESSFUL.getCode())
                            .message(SuccessCode.UPDATE_SUCCESSFUL.getMessage())
                            .data(updatedProject)
                            .build());
        }
    @PostMapping("/{projectId}/employees")
    public ResponseEntity<ResponseData<?>> addEmployeesToProject(
            @PathVariable Long projectId,
            @RequestBody ProjectReqDTO requestDTO) {
        ProjectResDTO responseDTO = projectService.addEmployeesToProject(projectId, requestDTO);
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.CREATED.getCode())
                        .message(SuccessCode.CREATED.getMessage())
                        .data(responseDTO)
                        .build());
    }
}