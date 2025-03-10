package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Criteria;
import com.example.sourcebase.domain.Department;
import com.example.sourcebase.domain.dto.reqdto.CriteriaReqDTO;
import com.example.sourcebase.domain.dto.resdto.AnswerResDTO;
import com.example.sourcebase.domain.dto.resdto.CriteriaResDTO;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import com.example.sourcebase.exception.AppException;
import com.example.sourcebase.mapper.CriteriaMapper;
import com.example.sourcebase.mapper.QuestionMapper;
import com.example.sourcebase.repository.ICriteriaRepository;
import com.example.sourcebase.repository.IDepartmentRepository;
import com.example.sourcebase.repository.IProjectRepository;
import com.example.sourcebase.repository.IQuestionRepository;
import com.example.sourcebase.service.ICriteriaService;
import com.example.sourcebase.util.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CriteriaServiceImpl implements ICriteriaService {

    ICriteriaRepository criteriaRepository;
    IQuestionRepository questionRepository;
    IDepartmentRepository departmentRepository;
    CriteriaMapper criteriaMapper;
    QuestionMapper questionMapper;
    IProjectRepository projectRepository;


    @Override
    public List<CriteriaResDTO> getAllCriterias() {
        List<Criteria> listCriteria = criteriaRepository.findAll();
        List<CriteriaResDTO> criteriaResDTOs = listCriteria.stream()
                .map(criteria -> {
                    CriteriaResDTO criteriaResDTO = criteriaMapper.toCriteriaResDTO(criteria);

                    // Mapping questions nếu có câu hỏi
                    if (criteria.getQuestions() != null && !criteria.getQuestions().isEmpty()) {
                        List<QuestionResDTO> questionResDTOs = criteria.getQuestions().stream()
                                .filter(question -> question.getDeletedAt() == null)
                                .map(question -> {
                                    QuestionResDTO questionResDTO = questionMapper.toQuestionResDTO(question);

                                    // Mapping answers nếu có câu trả lời
                                    if (question.getAnswers() != null && !question.getAnswers().isEmpty()) {
                                        List<AnswerResDTO> answerResDTOs = question.getAnswers().stream()
                                                .map(criteriaMapper::toAnswerResDTO)
                                                .collect(Collectors.toList());
                                        questionResDTO.setAnswers(answerResDTOs);
                                    } else {
                                        questionResDTO.setAnswers(new ArrayList<>()); // Không có câu trả lời
                                    }

                                    return questionResDTO;
                                })
                                .collect(Collectors.toList());
                        criteriaResDTO.setQuestions(questionResDTOs);
                    } else {
                        criteriaResDTO.setQuestions(new ArrayList<>()); // Không có câu hỏi
                    }

                    return criteriaResDTO;
                }).sorted((c1, c2) -> {
                    if (c1.getQuestions().isEmpty() && !c2.getQuestions().isEmpty()) {
                        return 1;
                    } else if (!c1.getQuestions().isEmpty() && c2.getQuestions().isEmpty()) {
                        return -1;
                    } else {
                        return c1.getId().compareTo(c2.getId());
                    }
                }).collect(Collectors.toList());

        // sort in order: criteria with questions first, criteria without questions last
        return criteriaResDTOs;
    }

    @Override
    public Page<CriteriaResDTO> getAllCriteria(int page, int size, String sortBy, boolean asc) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return criteriaRepository.findAll(pageable).map(criteriaMapper::toCriteriaResDTO);
    }

    @Override
    public CriteriaResDTO getCriteriaById(Long id) {
        Criteria criteria = criteriaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CRITERIA_NOT_FOUND));

        // lọc đi các câu hỏi đã bị xóa
        criteria.setQuestions(
                criteria.getQuestions().stream()
                        .filter(question -> question.getDeletedAt() == null)
                        .collect(Collectors.toList())
        );
        return criteriaMapper.toCriteriaResDTO(criteria);
    }

    @Override
    public void validateUniqueTitle(CriteriaReqDTO criteriaReqDTO) {
        if (criteriaRepository.existsByTitle(criteriaReqDTO.getTitle())) {
            throw new IllegalArgumentException("Tiêu đề đã tồn tại!");
        }
    }

    @Override
    @Transactional
    public CriteriaResDTO addCriterion(CriteriaReqDTO criteriaReqDTO) {
        Criteria newCriteria = criteriaMapper.toEntity(criteriaReqDTO);
        if (criteriaRepository.existsByTitleIgnoreCase(newCriteria.getTitle())) {
            throw new AppException(ErrorCode.CRITERIA_EXISTED);
        }

        return criteriaMapper.toCriteriaResDTO(criteriaRepository.save(newCriteria));
    }

    @Override
    @Transactional
    public CriteriaResDTO updateCriterion(Long id, CriteriaReqDTO criteriaReqDTO) {
        return criteriaRepository.findById(id)
                .map(criteria -> {
                    if (criteriaReqDTO.getTitle() != null
                            && !criteriaReqDTO.getTitle().equalsIgnoreCase(criteria.getTitle())
                            && criteriaRepository.existsByTitleIgnoreCase(criteriaReqDTO.getTitle())
                    ) {
                        throw new AppException(ErrorCode.CRITERIA_EXISTED);
                    }
                    criteria = criteriaMapper.partialUpdate(criteriaReqDTO, criteria);
                    if (criteriaReqDTO.getTitle() != null
                            && !criteriaReqDTO.getTitle().equals(criteria.getTitle())
                            && criteriaRepository.existsByTitleIgnoreCase(criteriaReqDTO.getTitle())
                    ) {
                        throw new AppException(ErrorCode.CRITERIA_EXISTED);
                    }
                    return criteriaMapper.toCriteriaResDTO(criteriaRepository.save(criteria));
                })
                .orElseThrow(() -> new AppException(ErrorCode.CRITERIA_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteCriterion(Long id) {
        Criteria criteria = criteriaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CRITERIA_NOT_FOUND));

        if (criteria.getQuestions() != null) {
            criteria.getQuestions().forEach(question -> {
                question.setDeletedAt(LocalDateTime.now());
                if (question.getAnswers() != null) {
                    question.getAnswers().forEach(answer -> answer.setDeletedAt(LocalDateTime.now()));
                }
            });
        }
        criteria.setDeletedAt(LocalDateTime.now());
        criteriaRepository.save(criteria);
    }

    @Override
    public Page<QuestionResDTO> findQuestionsByCriterionId(Long criteriaId, int page, int size, String sortBy, boolean asc) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return questionRepository.findAllByCriteria_Id(criteriaId, pageable).map(questionMapper::toQuestionResDTO);
    }

    @Override
    @Transactional
    public CriteriaResDTO addCriterionToDepartment(CriteriaReqDTO criteriaReqDTO, Long departmentId) {
        Criteria newCriteria = criteriaMapper.toEntity(criteriaReqDTO);
        if (criteriaRepository.existsByTitleIgnoreCase(newCriteria.getTitle())) {
            throw new AppException(ErrorCode.CRITERIA_EXISTED);
        }

        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        newCriteria.setDepartment(department);

        Criteria savedCriteria = criteriaRepository.save(newCriteria);
        return criteriaMapper.toCriteriaResDTO(savedCriteria);
    }

    @Override
    @Transactional
    public CriteriaResDTO updateCriterionInDepartment(CriteriaReqDTO criteriaReqDTO, Long departmentId, Long criteriaId) {
        // Check if there is any criteria in department
        Department rqDepartment = departmentRepository.findById(departmentId).orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        Set<Criteria> cInRqDepartment = rqDepartment.getCriterias();
        if (cInRqDepartment == null
                || cInRqDepartment.isEmpty()
                || cInRqDepartment.stream().noneMatch(c -> c.getId().equals(criteriaId))
        ) {
            throw new AppException(ErrorCode.DEPARTMENT_CRITERIA_NOT_FOUND);
        }

        // check if criteria title is existed in department
        if (cInRqDepartment.stream().anyMatch(c -> c.getTitle().equalsIgnoreCase(criteriaReqDTO.getTitle()))) {
            throw new AppException(ErrorCode.CRITERIA_EXISTED);
        }

        Criteria c = cInRqDepartment.stream().filter(criteria -> criteria.getId().equals(criteriaId)).findFirst().orElse(null);
        Criteria toBeUpdated = criteriaMapper.partialUpdate(criteriaReqDTO, c);
        Criteria savedCriteria = criteriaRepository.save(toBeUpdated);
        return criteriaMapper.toCriteriaResDTO(savedCriteria);
    }
}
