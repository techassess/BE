package com.example.sourcebase.domain.dto.resdto.user;

import com.example.sourcebase.domain.dto.resdto.DepartmentResDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProjectResDTO {
    Long projectId;
    Long userId;
    DepartmentResDTO department;
}
