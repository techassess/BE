package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Question;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import com.example.sourcebase.mapper.QuestionMapper;
import com.example.sourcebase.repository.IQuestionRepository;
import com.example.sourcebase.service.IQuestionService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionServiceImpl implements IQuestionService {

    IQuestionRepository questionRepository;
    QuestionMapper questionMapper = QuestionMapper.INSTANCE;

    public List<QuestionResDTO> getAllQuestionByCriteriaID(Long criteriaId){
        List<Question> questionResDTOs = questionRepository.findAllQuestionByCriteriaId(criteriaId);

        return questionResDTOs.stream()
                .map(questionMapper::toQuestionResDTO)
                .collect(Collectors.toList());
    }
}
