package com.example.sourcebase.service;

import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IQuestionService {
    List<QuestionResDTO> getAllQuestionByCriteriaID(Long criteriaId);
}
