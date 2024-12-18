package com.example.sourcebase.domain.dto.resdto.user;

import com.example.sourcebase.domain.Rank;
import com.example.sourcebase.domain.dto.resdto.FileInfoResDTO;
import com.example.sourcebase.domain.dto.resdto.ProjectResDTO;
import com.example.sourcebase.domain.enumeration.EGender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserDetailResDTO {
    Long id;
    String name;
    String email;
    String phoneNumber;
    String dob;
    EGender gender;
    String username;
    String password;
    Rank rank;
    boolean isDeleted;
    LocalDateTime createdAt;
    FileInfoResDTO fileInfoResDto;
    List<UserRoleResDTO> userRoles;
    List<ProjectResDTO> projects;
    List<UserProjectResDTO> userProjects;
}
