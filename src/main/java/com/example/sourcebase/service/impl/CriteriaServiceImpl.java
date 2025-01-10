package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.*;
import com.example.sourcebase.domain.dto.reqdto.CriteriaReqDTO;
import com.example.sourcebase.domain.dto.resdto.AnswerResDTO;
import com.example.sourcebase.domain.dto.resdto.CriteriaResDTO;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import com.example.sourcebase.exception.AppException;
import com.example.sourcebase.mapper.CriteriaMapper;
import com.example.sourcebase.mapper.QuestionMapper;
import com.example.sourcebase.repository.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CriteriaServiceImpl implements ICriteriaService {

    ICriteriaRepository criteriaRepository;
    IQuestionRepository questionRepository;
    IDepartmentCriteriasRepository dcRepository;
    IDepartmentRepository departmentRepository;
    CriteriaMapper criteriaMapper = CriteriaMapper.INSTANCE;
    QuestionMapper questionMapper = QuestionMapper.INSTANCE;
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
                                .filter(question -> !question.isDeleted())
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
                })
                .collect(Collectors.toList());

        // sort in order: criteria with questions first, criteria without questions last
        criteriaResDTOs.sort((c1, c2) -> {
            if (c1.getQuestions().isEmpty() && !c2.getQuestions().isEmpty()) {
                return 1;
            } else if (!c1.getQuestions().isEmpty() && c2.getQuestions().isEmpty()) {
                return -1;
            } else {
                return c1.getId().compareTo(c2.getId());
            }
        });
        return criteriaResDTOs;
    }

    @Override
    public Page<CriteriaResDTO> getAllCriteria(int page, int size, String sortBy, boolean asc) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return criteriaRepository.findAll(pageable).map(criteriaMapper::toCriteriaResDTO);
    }

    @Override
    public void validateUniqueTitle(CriteriaReqDTO criteriaReqDTO) {
        if (criteriaRepository.existsByTitle(criteriaReqDTO.getTitle())) {
            throw new IllegalArgumentException("Tiêu đề đã tồn tại!");
        }
    }

    @Override
    @Transactional
    public CriteriaResDTO getCriteriaById(Long id, Long departmentId) {

        // find dcs by criteria
        List<DepartmentCriterias> dcsByCId = dcRepository.findByCriteria_Id(id);
        if (dcsByCId.isEmpty()) {
            throw new AppException(ErrorCode.CRITERIA_NOT_FOUND);
        }
        List<DepartmentCriterias> fillDeletedDcs = dcsByCId.stream().filter(dc -> !dc.getCriteria().isDeleted()).toList();
        List<DepartmentCriterias> fillDeletedQuestions = fillDeletedDcs.stream()
                .filter(dc -> dc.getQuestion() != null && !dc.getQuestion().isDeleted())
                .toList();
        int totalPoints = fillDeletedQuestions.stream().map(dc -> dc.getQuestion().getPoint()).reduce(0, Integer::sum);
        Criteria currentCriteria = dcsByCId.getFirst().getCriteria();
        currentCriteria.setPoint(totalPoints);
        criteriaRepository.save(currentCriteria); // done update point of criteria

        Criteria criteria = criteriaRepository.findById(id, departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.CRITERIA_NOT_FOUND));

        CriteriaResDTO criteriaResDTO = criteriaMapper.toCriteriaResDTO(criteria);

        if (criteria.getDepartmentCriterias() != null && !criteria.getDepartmentCriterias().isEmpty()) {
            List<QuestionResDTO> questionResDTOs = criteria.getDepartmentCriterias().stream()
                    .filter(dc -> dc.getDepartment().getId().equals(departmentId))
                    .map(dc -> {
                        Question question = dc.getQuestion();
                        if (question != null && !question.isDeleted()) {
                            QuestionResDTO questionResDTO = questionMapper.toQuestionResDTO(question);

                            List<AnswerResDTO> answerResDTOs = question.getAnswers() != null
                                    ? question.getAnswers().stream()
                                    .map(criteriaMapper::toAnswerResDTO)
                                    .collect(Collectors.toList())
                                    : null;
                            questionResDTO.setAnswers(answerResDTOs);

                            return questionResDTO;
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            criteriaResDTO.setQuestions(questionResDTOs);
        } else {
            criteriaResDTO.setQuestions(null);
        }


        return criteriaResDTO;
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
                question.setDeleted(true);
                if (question.getAnswers() != null) {
                    question.getAnswers().forEach(answer -> answer.setDeleted(true));
                }
            });
        }
        criteria.setDeleted(true);
        criteriaRepository.save(criteria);
    }

    @Override
    @Transactional
    public void deleteCriterionByCriteriaIdAndDepartmentId(Long criteriaId, Long departmentId) {
        List<DepartmentCriterias> dcs = dcRepository.findByCriteria_IdAndDepartment_Id(criteriaId, departmentId);
        System.out.println(criteriaId + " " + departmentId);
        if (dcs != null && !dcs.isEmpty()) {
            dcs.forEach(dc -> {
                dc.getCriteria().getQuestions().forEach(question -> {
                    question.setDeleted(true);
                    question.getAnswers().forEach(answer -> answer.setDeleted(true));
                });
                dc.getCriteria().setDeleted(true);
            });
            dcRepository.deleteAll(dcs);
        }
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
        Criteria savedCriteria = criteriaRepository.save(newCriteria);

        Department d = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        DepartmentCriterias dc = dcRepository.save(DepartmentCriterias.builder()
                .criteria(savedCriteria)
                .department(d)
                .build());

        return criteriaMapper.toCriteriaResDTO(dc.getCriteria());
    }

    @Override
    @Transactional
    public CriteriaResDTO updateCriterionInDepartment(CriteriaReqDTO criteriaReqDTO, Long departmentId, Long criteriaId) {
        // Check if there is any criteria in department
        List<DepartmentCriterias> dcs = dcRepository.findByCriteria_IdAndDepartment_Id(criteriaId, departmentId);
        if (dcs.isEmpty()) {
            throw new AppException(ErrorCode.DEPARTMENT_CRITERIA_NOT_FOUND);
        }

        if (dcRepository.isCriteriaTitleExistsInDepartment(departmentId, criteriaReqDTO.getTitle())
                && !dcs.getFirst().getCriteria().getTitle().equalsIgnoreCase(criteriaReqDTO.getTitle())) {
            throw new AppException(ErrorCode.CRITERIA_EXISTED);
        }

        Criteria c = dcs.getFirst().getCriteria();
        Criteria toBeUpdated = criteriaMapper.partialUpdate(criteriaReqDTO, c);
        dcs.forEach(dc -> dc.setCriteria(toBeUpdated));
        List<DepartmentCriterias> savedDcs = dcRepository.saveAll(dcs);
        return criteriaMapper.toCriteriaResDTO(savedDcs.getFirst().getCriteria());
    }

    @Override
    public List<CriteriaResDTO> getCriteriaByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        Long departmentId = project.getDepartment().getId();

        List<Criteria> criterias = criteriaRepository.findAllCriteriaByDepartmentId(departmentId);

        List<CriteriaResDTO> criteriaResDTOs = criterias.stream()
                .map(criteria -> {
                    CriteriaResDTO criteriaResDTO = criteriaMapper.toCriteriaResDTO(criteria);

                    if (criteria.getDepartmentCriterias() != null && !criteria.getDepartmentCriterias().isEmpty()) {
                        List<QuestionResDTO> questionResDTOs = criteria.getDepartmentCriterias().stream()
                                .filter(dc -> dc.getDepartment().getId().equals(departmentId))
                                .map(dc -> {
                                    Question question = dc.getQuestion();
                                    if (question != null && !question.isDeleted()) {
                                        QuestionResDTO questionResDTO = questionMapper.toQuestionResDTO(question);

                                        List<AnswerResDTO> answerResDTOs = question.getAnswers() != null
                                                ? question.getAnswers().stream()
                                                .map(criteriaMapper::toAnswerResDTO)
                                                .collect(Collectors.toList())
                                                : null;
                                        questionResDTO.setAnswers(answerResDTOs);

                                        return questionResDTO;
                                    }
                                    return null;
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());

                        criteriaResDTO.setQuestions(questionResDTOs);
                    } else {
                        criteriaResDTO.setQuestions(null);
                    }

                    return criteriaResDTO;
                })
                .collect(Collectors.toList());
        return criteriaResDTOs;
    }
}
