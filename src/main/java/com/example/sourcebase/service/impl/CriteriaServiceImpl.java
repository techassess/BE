package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Criteria;
import com.example.sourcebase.domain.dto.reqdto.CriteriaReqDTO;
import com.example.sourcebase.domain.dto.resdto.AnswerResDTO;
import com.example.sourcebase.domain.dto.resdto.CriteriaResDTO;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import com.example.sourcebase.mapper.CriteriaMapper;
import com.example.sourcebase.mapper.QuestionMapper;
import com.example.sourcebase.repository.ICriteriaRepository;
import com.example.sourcebase.service.ICriteriaService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CriteriaServiceImpl implements ICriteriaService {

    ICriteriaRepository criteriaRepository;
    CriteriaMapper criteriaMapper = CriteriaMapper.INSTANCE;
    QuestionMapper questionMapper = QuestionMapper.INSTANCE;

    @Override
    public List<CriteriaResDTO> getAllCriterias() {
        List<Criteria> listCriteria = criteriaRepository.findAll();
        List<CriteriaResDTO> criteriaResDTOs = listCriteria.stream()
                .map(criteria -> {
                    CriteriaResDTO criteriaResDTO = criteriaMapper.toCriteriaResDTO(criteria);

                    // Mapping questions nếu có câu hỏi
                    if (criteria.getQuestions() != null && !criteria.getQuestions().isEmpty()) {
                        List<QuestionResDTO> questionResDTOs = criteria.getQuestions().stream()
                                .map(question -> {
                                    QuestionResDTO questionResDTO = questionMapper.toQuestionResDTO(question);

                                    // Mapping answers nếu có câu trả lời
                                    if (question.getAnswers() != null && !question.getAnswers().isEmpty()) {
                                        List<AnswerResDTO> answerResDTOs = question.getAnswers().stream()
                                                .map(criteriaMapper::toAnswerResDTO)
                                                .collect(Collectors.toList());
                                        questionResDTO.setAnswers(answerResDTOs);
                                    } else {
                                        questionResDTO.setAnswers(null); // Không có câu trả lời
                                    }

                                    return questionResDTO;
                                })
                                .collect(Collectors.toList());
                        criteriaResDTO.setQuestions(questionResDTOs);
                    } else {
                        criteriaResDTO.setQuestions(null); // Không có câu hỏi
                    }

                    return criteriaResDTO;
                })
                .collect(Collectors.toList());
        return criteriaResDTOs;
    }


}
