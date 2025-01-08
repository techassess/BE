package com.example.sourcebase.domain.dto.reqdto.user;

import jakarta.servlet.annotation.WebInitParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RegisterReqDTO {
    String name;
    String email;
    String phoneNumber;
    String dob;
    String gender;
    String username;
    String password;
    String position;
    String level;
    Long departmentId;

}
