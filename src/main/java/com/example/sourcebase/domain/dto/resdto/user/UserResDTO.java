package com.example.sourcebase.domain.dto.resdto.user;

import com.example.sourcebase.domain.Rank;
import com.example.sourcebase.domain.dto.resdto.FileInfoResDTO;
import com.example.sourcebase.domain.enumeration.EGender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResDTO {
    Long id;
    String name;
    String email;
    String phoneNumber;
    String dob;
    EGender gender;
    String username;
    String password;
    boolean isDeleted;
    Rank rank;
    Long departmentId;
    FileInfoResDTO fileInfo;
    List<UserRoleResDTO> userRoles;
    List<UserProjectResDTO> userProjects;
}
