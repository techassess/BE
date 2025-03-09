package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Criteria;
import com.example.sourcebase.domain.Department;
import com.example.sourcebase.domain.dto.reqdto.DepartmentReqDTO;
import com.example.sourcebase.domain.dto.resdto.DepartmentResDTO;
import com.example.sourcebase.exception.AppException;
import com.example.sourcebase.mapper.CriteriaMapper;
import com.example.sourcebase.mapper.DepartmentMapper;
import com.example.sourcebase.repository.IDepartmentRepository;
import com.example.sourcebase.service.IDepartmentService;
import com.example.sourcebase.util.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService implements IDepartmentService {

    IDepartmentRepository departmentRepository;
    DepartmentMapper departmentMapper;
    CriteriaMapper criteriaMapper;

    @Override
    public List<DepartmentResDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();

        // Lọc đi các tiêu chí đã bị xóa của các phòng ban
        departments.forEach(department ->
                department.setCriterias(
                        department.getCriterias().stream()
                                .filter(criteria -> criteria.getDeletedAt() == null)
                                .collect(Collectors.toSet())
                )
        );
        return departments.stream().map(departmentMapper::toDepartmentResDTO).collect(Collectors.toList());
    }

    @Override
    public Page<DepartmentResDTO> getAllDepartment(int page, int size, String sortBy, boolean asc) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return departmentRepository.findAll(pageable).map(departmentMapper::toDepartmentResDTO);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        department.getCriterias().forEach(criteria -> criteria.setDeletedAt(LocalDateTime.now()));
        department.setDeletedAt(LocalDateTime.now());
        departmentRepository.save(department);
    }

    @Override
    @Transactional
    public DepartmentResDTO addDepartment(DepartmentReqDTO departmentReqDTO) {
        Department department = departmentMapper.toEntity(departmentReqDTO);
        if (departmentRepository.existsByNameIgnoreCase(department.getName())) {
            throw new AppException(ErrorCode.DEPARTMENT_ALREADY_EXIST);
        }
        return departmentMapper.toDepartmentResDTO(departmentRepository.save(department));
    }

    @Override
    @Transactional
    public DepartmentResDTO updateDepartment(Long id, DepartmentReqDTO departmentReqDTO) {
        return departmentRepository.findById(id)
                .map(department -> {
                    if (departmentReqDTO.getName() != null
                            && !departmentReqDTO.getName().equalsIgnoreCase(department.getName())
                            && departmentRepository.existsByNameIgnoreCase(departmentReqDTO.getName())
                    ) {
                        throw new AppException(ErrorCode.DEPARTMENT_NOT_FOUND);
                    }
                    department = departmentMapper.partialUpdate(departmentReqDTO, department);
                    return departmentMapper.toDepartmentResDTO(departmentRepository.save(department));
                })
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }

    @Override
    public DepartmentResDTO getDetailDepartment(Long id) {
        // Tìm phòng ban theo ID
        Department department = departmentRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        // Sort the criteria set

        List<Criteria> criteriaList = new ArrayList<>(department.getCriterias());
        criteriaList.sort(Criteria::compareTo);

        DepartmentResDTO resDTO = departmentMapper.toDepartmentResDTO(department);
        resDTO.setCriteria(criteriaMapper.toCriteriaResDTOList(criteriaList));

        return resDTO;
    }
}
