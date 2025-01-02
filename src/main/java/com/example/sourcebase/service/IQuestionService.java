package com.example.sourcebase.service;

import com.example.sourcebase.domain.dto.reqdto.AddQuestionReqDto;
import com.example.sourcebase.domain.dto.reqdto.QuestionReqDto;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IQuestionService {
    List<QuestionResDTO> getAllQuestionByCriteriaID(Long criteriaId);

    Page<QuestionResDTO> findAllQuestions(int page, int size, String sortBy, boolean asc);

    QuestionResDTO getQuestionById(Long id);

    //QuestionResDTO addQuestion(QuestionReqDto questionReqDto);

    QuestionResDTO updateQuestion(Long id, QuestionReqDto questionReqDto);

    void deleteQuestion(Long id);

    QuestionResDTO addQuestionAndAnswers(AddQuestionReqDto addQuestionReqDto);

    Page<QuestionResDTO> getQuestionsByCriteriaId(Long criteriaId, int page, int size, String sortBy, boolean asc);

    Page<QuestionResDTO> findQuestionsByTitle(String title, int page, int size, String sortBy, boolean asc);

}
