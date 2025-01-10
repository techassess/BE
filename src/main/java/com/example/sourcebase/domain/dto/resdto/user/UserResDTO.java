package com.example.sourcebase.domain.dto.resdto.user;

import com.example.sourcebase.domain.dto.BaseEntityDto;
import com.example.sourcebase.domain.dto.resdto.FileInfoResDTO;
import com.example.sourcebase.domain.enumeration.EGender;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ToString
public class UserResDTO extends BaseEntityDto {
    Long id;
    String name;
    String email;
    String phoneNumber;
    String dob;
    EGender gender;
    String username;
    String password;
    boolean isDeleted;
    RankResDto rank;
    Long departmentId;
    FileInfoResDTO fileInfo;
    List<UserRoleResDTO> userRoles;
    List<UserProjectResDTO> userProjects;
}
