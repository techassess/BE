package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Department;
import com.example.sourcebase.domain.dto.reqdto.DepartmentReqDTO;
import com.example.sourcebase.domain.dto.resdto.CriteriaResDTO;
import com.example.sourcebase.domain.dto.resdto.DepartmentResDTO;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
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

    @Override
    public List<DepartmentResDTO> getAllDepartments() {
        // Lấy danh sách các department đã được xử lý thông qua repository
        List<Department> departments = departmentRepository.findAll();

        // Duyệt qua các department và xử lý dữ liệu
        return departments.stream().map(department -> {
            // Chuyển đổi department sang DTO
            DepartmentResDTO departmentResDTO = departmentMapper.toDepartmentResDTO(department);

            // Nhóm các criteria theo ID để loại bỏ trùng
            Map<Long, CriteriaResDTO> criteriaMap = department.getDepartmentCriterias().stream()
                    .filter(departmentCriteria -> !departmentCriteria.getCriteria().isDeleted()) // Lọc các criteria chưa bị xóa
                    .map(departmentCriteria -> {
                        CriteriaResDTO criteriaResDTO = criteriaMapper.toCriteriaResDTO(departmentCriteria.getCriteria());

                        // Lấy tất cả câu hỏi liên kết với Criteria thông qua DepartmentCriterias
                        List<QuestionResDTO> questionResDTOList = department.getDepartmentCriterias().stream()
                                .filter(dc -> dc.getCriteria().equals(departmentCriteria.getCriteria())) // Lọc các DepartmentCriterias có cùng Criteria
                                .map(dc -> {
                                    if (dc.getQuestion() != null && !dc.getQuestion().isDeleted()) {
                                        // Chuyển đổi câu hỏi thành DTO
                                        QuestionResDTO questionResDTO = criteriaMapper.toQuestionResDTO(dc.getQuestion());

                                        // Lọc và chuyển đổi các câu trả lời
                                        if (dc.getQuestion().getAnswers() != null) {
                                            questionResDTO.setAnswers(dc.getQuestion().getAnswers().stream()
                                                    .filter(answer -> !answer.isDeleted()) // Lọc các câu trả lời chưa bị xóa
                                                    .map(criteriaMapper::toAnswerResDTO)
                                                    .collect(Collectors.toList()));
                                        }
                                        return questionResDTO;
                                    }
                                    return null;
                                })
                                .filter(questionResDTO -> questionResDTO != null) // Loại bỏ các câu hỏi null
                                .collect(Collectors.toList());

                        // Gán danh sách câu hỏi vào DTO của Criteria
                        criteriaResDTO.setQuestions(questionResDTOList);
                        return criteriaResDTO;
                    })
                    .collect(Collectors.toMap(
                            CriteriaResDTO::getId,    // Sử dụng id của CriteriaResDTO làm khóa
                            criteriaResDTO -> criteriaResDTO,  // Giá trị là CriteriaResDTO
                            (existing, replacement) -> existing // Giữ lại giá trị đầu tiên nếu có trùng lặp
                    ));

            // Gán danh sách criteria đã nhóm vào DTO của department
            departmentResDTO.setCriteria(criteriaMap.values().stream().collect(Collectors.toList()));
            return departmentResDTO;
        }).collect(Collectors.toList()); // Trả về danh sách các DepartmentResDTO
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
}
