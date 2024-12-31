package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Criteria;
import com.example.sourcebase.domain.Department;
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
import org.springframework.stereotype.Service;

import java.util.List;
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
            List<CriteriaResDTO> criteriaResDTOS = department.getDepartmentCriterias().stream()
                    .filter(departmentCriteria -> !departmentCriteria.getCriteria().isDeleted())
                    .map(departmentCriteria -> {
                        CriteriaResDTO criteriaResDTO = criteriaMapper.toCriteriaResDTO(departmentCriteria.getCriteria());
                        if (departmentCriteria.getCriteria().getQuestions() != null) {
                            criteriaResDTO.setQuestions(departmentCriteria.getCriteria().getQuestions().stream()
                                    .filter(question -> !question.isDeleted())
                                    .map(question -> {
                                        QuestionResDTO questionResDTO = criteriaMapper.toQuestionResDTO(question);

                                        if (question.getAnswers() != null) {
                                            questionResDTO.setAnswers(question.getAnswers().stream()
                                                    .filter(answer -> !answer.isDeleted())
                                                    .map(criteriaMapper::toAnswerResDTO)
                                                    .collect(Collectors.toList()));
                                        }

                                        return questionResDTO;
                                    })
                                    .collect(Collectors.toList()));
                        }

                        return criteriaResDTO;
                    })
                    .collect(Collectors.toList());

            departmentResDTO.setCriteria(criteriaResDTOS);
            return departmentResDTO;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        departmentCriteriasRepository.deleteByDepartmentId(id);
        department.setDeleted(true);
        departmentRepository.save(department);
    }
}
