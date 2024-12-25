package com.example.sourcebase.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    ResponseData error = new ResponseData();

    @ExceptionHandler({
            NoSuchElementException.class,
            RuntimeException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseData> handlerValidationException(Exception ex, WebRequest request) {
        error.setCode(ErrorCode.USER_NOT_FOUND.getCode());
        error.setTimestamp(LocalDateTime.now());
        error.setError(ex.getMessage());
        error.setPath(request.getDescription(false).replace("uri=", ""));
        String message = ex.getMessage();
        if (ex instanceof NoSuchElementException) {
            error.setError("Data Invalid");
            error.setCode(ErrorCode.USER_NOT_FOUND.getCode());
            error.setMessage(ErrorCode.USER_NOT_FOUND.getMessage());
        } else if (ex instanceof RuntimeException) {
            error.setError("PathVariable Invalid");
            error.setMessage(message);
        } else if (ex instanceof IllegalArgumentException) {
            error.setError("ILLEGAL ARGUMENT");
            error.setMessage(ErrorCode.ILLEGAL_ARGUMENT.getMessage());
            error.setCode(ErrorCode.ILLEGAL_ARGUMENT.getCode());
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });


        return ResponseEntity.badRequest().body(ResponseData.builder()
                .data(errors)
                .message("Invalid input")
                .code(4000)
                .build());
    }
}