package com.example.sourcebase.controller;

import com.example.sourcebase.domain.dto.reqdto.AssessReqDTO;
import com.example.sourcebase.service.IAssessService;
import com.example.sourcebase.service.IRatedRankService;
import com.example.sourcebase.util.ErrorCode;
import com.example.sourcebase.util.ResponseData;
import com.example.sourcebase.util.SuccessCode;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assess")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssessRestController {
    IAssessService assessService;
    IRatedRankService ratedRankService;

    @PostMapping("/save-assess")
    @CrossOrigin
    public ResponseEntity<ResponseData<?>> saveAssess(@Valid @RequestBody AssessReqDTO assessReqDto) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.CREATED.getCode())
                        .message(SuccessCode.CREATED.getMessage())
                        .data(assessService.updateAssess(assessReqDto))
                        .build()
        );
    }

    @GetMapping("/list-assess-of-user/{userId}")
    public ResponseEntity<ResponseData<?>> getListAssessOfUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(assessService.getListAssessOfUserId(userId))
                        .build()
        );
    }

    @GetMapping("/list-assess-by-user/{userId}")
    public ResponseEntity<ResponseData<?>> getListAssessByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(assessService.getListAssessByUserId(userId))
                        .build()
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseData<?>> getAllUserHadSameProject(@PathVariable Long userId) {
        if (assessService.getAssess(userId) == null) {
            return ResponseEntity.status(ErrorCode.ASSESS_IS_NOT_EXIST.getHttpStatus()).body(
                    ResponseData.builder()
                            .code(ErrorCode.ASSESS_IS_NOT_EXIST.getCode())
                            .message(ErrorCode.ASSESS_IS_NOT_EXIST.getMessage())
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(assessService.getAssess(userId))
                        .build()
        );
    }

    @GetMapping("/get-map-manager-rating-point-to-user/{userId}")
    public ResponseEntity<ResponseData<?>> test(@PathVariable String userId) {

        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(ratedRankService.getAverageValueOfCriteriaByManager(Long.parseLong(userId)))
                        .build()
        );
    }
}
