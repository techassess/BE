package com.example.sourcebase.controller;

import com.example.sourcebase.domain.dto.reqdto.CriteriaReqDTO;
import com.example.sourcebase.domain.dto.reqdto.DepartmentCriteriasReqDto;
import com.example.sourcebase.domain.dto.reqdto.post.AddCriterionToDepartmentReqDto;
import com.example.sourcebase.domain.dto.reqdto.put.UpdateCriterionInDepartmentReqDto;
import com.example.sourcebase.service.ICriteriaService;
import com.example.sourcebase.util.ResponseData;
import com.example.sourcebase.util.SuccessCode;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/criterias")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class CriteriaRestController {
    ICriteriaService criteriaService;

    @GetMapping
    public ResponseEntity<ResponseData<?>> getListCriteria() {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(criteriaService.getAllCriterias())
                        .build());
    }

    @PostMapping
    public ResponseEntity<ResponseData<?>> addCriterion(@Valid @RequestBody AddCriterionToDepartmentReqDto dto) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.CREATED.getCode())
                        .message(SuccessCode.CREATED.getMessage())
                        .data(criteriaService.addCriterionToDepartment(dto.getCriteriaReqDTO(), dto.getDepartmentId()))
                        .build());
    }

    @GetMapping("/v2")
    public ResponseEntity<ResponseData<?>> getAllCriteria(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean asc
    ) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(criteriaService.getAllCriteria(page, size, sortBy, asc))
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCriterion(@PathVariable Long id, @Valid @RequestBody CriteriaReqDTO criteriaReqDTO) {

        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.UPDATED.getCode())
                        .message(SuccessCode.UPDATED.getMessage())
                        .data(criteriaService.updateCriterion(id, criteriaReqDTO))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<?>> deleteCriterion(@PathVariable Long id) {
        criteriaService.deleteCriterion(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<ResponseData<?>> deleteCriterionByCriteriaIdAndDepartmentId(@RequestBody DepartmentCriteriasReqDto dcReqDto) {
        criteriaService.deleteCriterionByCriteriaIdAndDepartmentId(dcReqDto.getCriteriaId(), dcReqDto.getDepartmentId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/{departmentId}")
    public ResponseEntity<ResponseData<?>> getCriterionById(@PathVariable Long id, @PathVariable Long departmentId) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(criteriaService.getCriteriaById(id, departmentId))
                        .build());
    }

    @GetMapping("/{id}/questions")
    public ResponseEntity<ResponseData<?>> getQuestionsByCriterionId(@PathVariable Long id,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "id") String sortBy,
                                                                     @RequestParam(defaultValue = "true") boolean asc
    ) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(criteriaService.findQuestionsByCriterionId(id, page, size, sortBy, asc))
                        .build());
    }

    @PutMapping(value = "/update-criterion-in-department")
    public ResponseEntity<?> updateCriterionInDepartment(@RequestBody UpdateCriterionInDepartmentReqDto dcReqDto) {

        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.UPDATED.getCode())
                        .message(SuccessCode.UPDATED.getMessage())
                        .data(criteriaService
                                .updateCriterionInDepartment(dcReqDto.getCriteriaReqDTO(),
                                        dcReqDto.getDepartmentId(),
                                        dcReqDto.getCriteriaId()))
                        .build());
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ResponseData<?>> getListCriteriaByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(criteriaService.getCriteriaByProjectId(projectId))
                        .build());
    }
}
