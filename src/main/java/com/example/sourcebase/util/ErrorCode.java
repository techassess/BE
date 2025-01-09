package com.example.sourcebase.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

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
    MAIL_PHONE_USERNAME_ALREADY_EXISTED(409, "Email, Phone or Username Already Exists", HttpStatus.CONFLICT),
    VALIDATION_ERROR(400, "", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTS(409, "Username Exists", HttpStatus.CONFLICT),

    INVALID_PASSWORD(409, "Invalid Password", HttpStatus.CONFLICT),

    PPOJECT_IS_EXIST(410, "Project Exist", HttpStatus.CONFLICT),
    INVALID_INPUT(411, "Dữ liệu đầu vào không hợp lệ", HttpStatus.CONFLICT),
    INVALID_START_DATE(412, "Ngày bắt đầu không được sau ngày hiện tại", HttpStatus.CONFLICT),
    INVALID_END_DATE(413, "Ngày kết thúc phải lớn hơn ngày hiện tại", HttpStatus.CONFLICT),
    END_DATE_BEFORE_START_DATE(414, "Ngày kết thúc phải sau ngày bắt đầu", HttpStatus.CONFLICT),
    PROJECT_NOT_FOUND(415, "Không tìm thấy project", HttpStatus.NOT_FOUND),
    ASSESS_IS_NOT_EXIST(404, "Không tìm thấy đánh giá", HttpStatus.NOT_FOUND),

    ANSWER_NOT_FOUND(40401, "Answer not found", HttpStatus.NOT_FOUND),
    ANSWER_EXISTED(40901, "Đáp án đã tồn tại", HttpStatus.CONFLICT),

    CRITERIA_EXISTED(40902, "Tên đánh giá đã tồn tại", HttpStatus.CONFLICT),

    SUM_POINT_INVALID(40903, "Số point của đánh giá phải bằng tổng số point của các câu hỏi", HttpStatus.CONFLICT),

    DEPARTMENT_CRITERIA_NOT_FOUND(40402, "Criteria not found in department", HttpStatus.NOT_FOUND),
    DEPARTMENT_NOT_FOUND(40403, "Department not found", HttpStatus.NOT_FOUND),
    DEPARTMENT_ALREADY_EXIST(40904, "Department already exist", HttpStatus.CONFLICT),

    MANAGER_ASSESS_IS_NOT_EXIST(40404, "Không có đánh giá của quản lý", HttpStatus.NOT_FOUND),

    CRITERIA_ID_NOT_MATCH(40905, "Criteria id not match", HttpStatus.CONFLICT),
    ;
    int code;
    String message;
    HttpStatus httpStatus;
}
