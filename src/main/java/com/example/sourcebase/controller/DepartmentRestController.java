package com.example.sourcebase.controller;

import com.example.sourcebase.exception.AppException;
import com.example.sourcebase.domain.dto.reqdto.DepartmentReqDTO;
import com.example.sourcebase.domain.dto.resdto.DepartmentResDTO;
import com.example.sourcebase.exception.AppException;
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
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(departmentService.getAllDepartments())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<?>> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESS.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(departmentService.getDetailDepartment(id))
                        .build()
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.noContent().build();
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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