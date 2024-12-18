package com.example.sourcebase.controller;

import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import com.example.sourcebase.service.IQuestionService;
import com.example.sourcebase.util.ErrorCode;
import com.example.sourcebase.util.ResponseData;
import com.example.sourcebase.util.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class QuestionRestController {
    IQuestionService questionService;

    @GetMapping("/{criteriaId}")
    public ResponseEntity<ResponseData<?>> getAllQuestionByCriteriaID(@PathVariable Long criteriaId) {

        List<QuestionResDTO> questions = questionService.getAllQuestionByCriteriaID(criteriaId);

        if (questions == null || questions.isEmpty()) {
            return ResponseEntity.status(ErrorCode.CRITERIA_NOT_FOUND.getHttpStatus()).body(
                    ResponseData.builder()
                            .code(ErrorCode.CRITERIA_NOT_FOUND.getCode())
                            .message(ErrorCode.CRITERIA_NOT_FOUND.getMessage())
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(questions)
                        .build());
    }
}
