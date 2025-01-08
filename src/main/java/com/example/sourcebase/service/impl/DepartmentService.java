package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Criteria;
import com.example.sourcebase.domain.Department;
import com.example.sourcebase.domain.dto.reqdto.DepartmentReqDTO;
import com.example.sourcebase.domain.dto.resdto.CriteriaResDTO;
import com.example.sourcebase.domain.dto.resdto.DepartmentResDTO;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import com.example.sourcebase.exception.AppException;
import com.example.sourcebase.mapper.CriteriaMapper;
import com.example.sourcebase.mapper.DepartmentMapper;
import com.example.sourcebase.repository.IDepartmentCriteriasRepository;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService implements IDepartmentService {

    IDepartmentRepository departmentRepository;
    DepartmentMapper departmentMapper;
    CriteriaMapper criteriaMapper;
    IDepartmentCriteriasRepository departmentCriteriasRepository;

    @Override
    public List<DepartmentResDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();

        return departments.stream().map(department -> {
            DepartmentResDTO departmentResDTO = departmentMapper.toDepartmentResDTO(department);
            Map<Long, CriteriaResDTO> criteriaMap = department.getDepartmentCriterias().stream()
                    .filter(departmentCriteria -> !departmentCriteria.getCriteria().isDeleted())
                    .map(departmentCriteria -> {
                        CriteriaResDTO criteriaResDTO = criteriaMapper.toCriteriaResDTO(departmentCriteria.getCriteria());
                        List<QuestionResDTO> questionResDTOList = department.getDepartmentCriterias().stream()
                                .filter(dc -> dc.getCriteria().equals(departmentCriteria.getCriteria()))
                                .map(dc -> {
                                    if (dc.getQuestion() != null && !dc.getQuestion().isDeleted()) {
                                        QuestionResDTO questionResDTO = criteriaMapper.toQuestionResDTO(dc.getQuestion());
                                        if (dc.getQuestion().getAnswers() != null) {
                                            questionResDTO.setAnswers(dc.getQuestion().getAnswers().stream()
                                                    .filter(answer -> !answer.isDeleted())
                                                    .map(criteriaMapper::toAnswerResDTO)
                                                    .collect(Collectors.toList()));
                                        }
                                        return questionResDTO;
                                    }
                                    return null;
                                })
                                .filter(questionResDTO -> questionResDTO != null)
                                .collect(Collectors.toList());

                        criteriaResDTO.setQuestions(questionResDTOList);
                        return criteriaResDTO;
                    })
                    .collect(Collectors.toMap(
                            CriteriaResDTO::getId,
                            criteriaResDTO -> criteriaResDTO,
                            (existing, replacement) -> existing
                    ));

            departmentResDTO.setCriteria(criteriaMap.values().stream().collect(Collectors.toList()));
            return departmentResDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<DepartmentResDTO> getAllDepartment(int page, int size, String sortBy, boolean asc) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return departmentRepository.findAll(pageable).map(departmentMapper::toDepartmentResDTO);
    }

    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        if (department.getDepartmentCriterias() != null) {
            department.getDepartmentCriterias().forEach(departmentCriteria -> {
                departmentCriteriasRepository.deleteByDepartmentId(departmentCriteria.getId());
            });
        }
        department.setDeleted(true);
        departmentRepository.save(department);
    }

    @Override
    @Transactional
    public DepartmentResDTO addDepartment(DepartmentReqDTO departmentReqDTO) {
        Department department = departmentMapper.toEntity(departmentReqDTO);
        if (departmentRepository.existsByNameIgnoreCaseAndDeletedIsFalse(department.getName())) {
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
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));

        // Map sang DTO
        DepartmentResDTO departmentResDTO = departmentMapper.toDepartmentResDTO(department);

        // Lấy danh sách tiêu chí và các câu hỏi của phòng ban
        Map<Long, CriteriaResDTO> criteriaMap = department.getDepartmentCriterias().stream()
                .filter(departmentCriteria -> !departmentCriteria.getCriteria().isDeleted())
                .map(departmentCriteria -> {
                    CriteriaResDTO criteriaResDTO = criteriaMapper.toCriteriaResDTO(departmentCriteria.getCriteria());

                    List<QuestionResDTO> questionResDTOList = department.getDepartmentCriterias().stream()
                            .filter(dc -> dc.getCriteria().equals(departmentCriteria.getCriteria()))
                            .map(dc -> {
                                if (dc.getQuestion() != null && !dc.getQuestion().isDeleted()) {
                                    QuestionResDTO questionResDTO = criteriaMapper.toQuestionResDTO(dc.getQuestion());
                                    if (dc.getQuestion().getAnswers() != null) {
                                        questionResDTO.setAnswers(dc.getQuestion().getAnswers().stream()
                                                .filter(answer -> !answer.isDeleted())
                                                .map(criteriaMapper::toAnswerResDTO)
                                                .collect(Collectors.toList()));
                                    }
                                    return questionResDTO;
                                }
                                return null;
                            })
                            .filter(questionResDTO -> questionResDTO != null)
                            .collect(Collectors.toList());

                    criteriaResDTO.setQuestions(questionResDTOList);
                    return criteriaResDTO;
                })
                .collect(Collectors.toMap(
                        CriteriaResDTO::getId,
                        criteriaResDTO -> criteriaResDTO,
                        (existing, replacement) -> existing
                ));

        // Set danh sách tiêu chí vào DTO của phòng ban
        departmentResDTO.setCriteria(criteriaMap.values().stream().collect(Collectors.toList()));

        return departmentResDTO;
    }

}
