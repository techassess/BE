package com.example.sourcebase.domain.dto.reqdto.user;

import com.example.sourcebase.domain.dto.BaseEntityDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@SuperBuilder
@ToString
public class RegisterReqDTO extends BaseEntityDto {
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
