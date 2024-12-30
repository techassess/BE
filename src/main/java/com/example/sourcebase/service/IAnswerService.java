package com.example.sourcebase.service;

import com.example.sourcebase.domain.dto.reqdto.AnswerReqDto;
import com.example.sourcebase.domain.dto.resdto.AnswerResDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAnswerService {
    List<AnswerResDTO> getAllAnswers();

    Page<AnswerResDTO> getAllAnswers(int page, int size, String sortBy, boolean asc);

    AnswerResDTO getAnswerById(Long id);

    AnswerResDTO addAnswer(AnswerReqDto answerReqDto);

    AnswerResDTO updateAnswer(Long id, AnswerReqDto answerReqDto);

    void deleteAnswer(Long id);

    Page<AnswerResDTO> getAnswersByQuestionId(Long questionId, int page, int size);

    Page<AnswerResDTO> getAnswersByQuestionId(Long questionId, int page, int size, String sortBy, boolean asc);
}
