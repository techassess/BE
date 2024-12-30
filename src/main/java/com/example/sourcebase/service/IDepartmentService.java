package com.example.sourcebase.service;

import com.example.sourcebase.domain.dto.resdto.DepartmentResDTO;

import java.util.List;

public interface IDepartmentService {
    List<DepartmentResDTO> getAllDepartments();
}
