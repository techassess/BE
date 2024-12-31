package com.example.sourcebase.controller;

import com.example.sourcebase.domain.dto.reqdto.DepartmentReqDTO;
import com.example.sourcebase.domain.dto.resdto.DepartmentResDTO;
import com.example.sourcebase.domain.dto.reqdto.DepartmentReqDTO;
import com.example.sourcebase.service.IDepartmentService;
import com.example.sourcebase.util.ResponseData;
import com.example.sourcebase.util.SuccessCode;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class DepartmentRestController {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentRestController.class);

    IDepartmentService departmentService;

    @GetMapping
    public ResponseEntity<ResponseData<?>> getListDepartment() {
        var departments = departmentService.getAllDepartments();

        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(departments)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ResponseData<?>> createDepartment(@Valid @RequestBody DepartmentReqDTO departmentReqDTO) {
        DepartmentResDTO d = departmentService.addDepartment(departmentReqDTO);

        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.CREATED.getCode())
                        .message(SuccessCode.CREATED.getMessage())
                        .data(d)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<?>> updatedDepartment(@PathVariable Long id,
                                                             @Valid @RequestBody DepartmentReqDTO departmentReqDTO) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.UPDATED.getCode())
                        .message(SuccessCode.UPDATED.getMessage())
                        .data(departmentService.updateDepartment(id, departmentReqDTO))
                        .build());
    }
}