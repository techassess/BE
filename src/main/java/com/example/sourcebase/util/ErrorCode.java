package com.example.sourcebase.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import javax.tools.Diagnostic;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    USER_NOT_FOUND(404, "User Not Found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(404, "Role Not Found", HttpStatus.NOT_FOUND),
    CRITERIA_NOT_FOUND(404, "Criteria Not Found", HttpStatus.NOT_FOUND),
    QUESTION_NOT_FOUND(404, "Question Not Found", HttpStatus.NOT_FOUND),
    USER_NOT_EXISTS(409, "User Not Exists", HttpStatus.CONFLICT),
    ID_NOT_EXISTS(409, "Id Not Exists", HttpStatus.CONFLICT),
    ILLEGAL_STATE(400, "Wrong password or username", HttpStatus.BAD_REQUEST),
    ILLEGAL_ARGUMENT(409, "Email, Phone or Username Already Exists", HttpStatus.CONFLICT),
    VALIDATION_ERROR(400, "", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTS(409, "Username Exists", HttpStatus.CONFLICT),

    INVALID_PASSWORD(409, "Invalid Password", HttpStatus.CONFLICT),

    PPOJECT_IS_EXIST(410,"Project Exist" ,HttpStatus.CONFLICT ),
     INVALID_INPUT (411, "Dữ liệu đầu vào không hợp lệ",HttpStatus.CONFLICT),
    INVALID_START_DATE(412, "Ngày bắt đầu không được sau ngày hiện tại",HttpStatus.CONFLICT),
    INVALID_END_DATE(413, "Ngày kết thúc phải lớn hơn ngày hiện tại",HttpStatus.CONFLICT),
    END_DATE_BEFORE_START_DATE(414, "Ngày kết thúc phải sau ngày bắt đầu",HttpStatus.CONFLICT),
    PROJECT_NOT_FOUND(415,"Không tìm thấy project" ,HttpStatus.NOT_FOUND ),
    ASSESS_IS_NOT_EXIST(404, "Assess Not Found", HttpStatus.NOT_FOUND);
    int code;
    String message;
    HttpStatus httpStatus;
}
