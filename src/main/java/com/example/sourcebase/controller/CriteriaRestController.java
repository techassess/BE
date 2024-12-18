package com.example.sourcebase.controller;

import com.example.sourcebase.service.ICriteriaService;
import com.example.sourcebase.util.ResponseData;
import com.example.sourcebase.util.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/criterias")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
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
}
