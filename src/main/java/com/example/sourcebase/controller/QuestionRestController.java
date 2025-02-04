package com.example.sourcebase.controller;

import com.example.sourcebase.domain.dto.reqdto.AddQuestionReqDto;
import com.example.sourcebase.domain.dto.reqdto.QuestionReqDto;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import com.example.sourcebase.service.IQuestionService;
import com.example.sourcebase.util.ErrorCode;
import com.example.sourcebase.util.ResponseData;
import com.example.sourcebase.util.SuccessCode;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/questions")
@AllArgsConstructor
@CrossOrigin(origins = {"http://192.168.0.152:5000", "http://192.168.0.152:6123", "http://localhost:5000", "http://localhost:6123"})
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class QuestionRestController {
    IQuestionService questionService;

    @GetMapping("/{criteriaId}")
    public ResponseEntity<ResponseData<?>> getAllQuestionsByCriteriaID(@PathVariable Long criteriaId) {

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
    @GetMapping("/v2/{id}")
    public ResponseEntity<ResponseData<?>> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(questionService.getQuestionById(id))
                        .build());
    }
   @PostMapping
    public ResponseEntity<ResponseData<?>> addQuestionAndAnswers(@Valid @RequestBody  AddQuestionReqDto addQuestionReqDto) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.CREATED.getCode())
                        .message(SuccessCode.CREATED.getMessage())
                        .data(questionService.addQuestionAndAnswers(addQuestionReqDto))
                        .build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @Valid @RequestBody QuestionReqDto questionReqDto) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.UPDATED.getCode())
                        .message(SuccessCode.UPDATED.getMessage())
                        .data(questionService.updateQuestion(id, questionReqDto))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<?>> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ResponseData<?>> findAllQuestions(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "id") String sortBy,
                                                            @RequestParam(defaultValue = "true") boolean asc) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .timestamp(LocalDateTime.now())
                        .data(questionService.findAllQuestions(page, size, sortBy, asc))
                        .build());
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData<?>> search(@RequestParam String title,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "true") boolean asc) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .timestamp(LocalDateTime.now())
                        .data(questionService.findQuestionsByTitle(title, page, size, sortBy, asc))
                        .build());
    }
}
