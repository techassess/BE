package com.example.sourcebase.service;

import com.example.sourcebase.domain.dto.reqdto.DepartmentReqDTO;
import com.example.sourcebase.domain.dto.resdto.CriteriaResDTO;
import com.example.sourcebase.domain.dto.resdto.DepartmentResDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDepartmentService {
    List<DepartmentResDTO> getAllDepartments();

    DepartmentResDTO getDetailDepartment(Long id);

    Page<DepartmentResDTO> getAllDepartment(int page, int size, String sortBy, boolean asc);

    void deleteDepartment(Long id);

    DepartmentResDTO addDepartment (DepartmentReqDTO departmentReqDTO);

    DepartmentResDTO updateDepartment(Long id, DepartmentReqDTO departmentReqDTO);
}
