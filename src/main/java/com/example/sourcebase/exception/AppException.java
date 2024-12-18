package com.example.sourcebase.exception;


import com.example.sourcebase.util.ErrorCode;
import lombok.*;


@Getter
@Setter
public class AppException extends RuntimeException {

    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
