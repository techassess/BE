package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Answer;
import com.example.sourcebase.domain.dto.reqdto.AnswerReqDto;
import com.example.sourcebase.domain.dto.resdto.AnswerResDTO;
import com.example.sourcebase.exception.AppException;
import com.example.sourcebase.mapper.AnswerMapper;
import com.example.sourcebase.repository.IAnswerRepository;
import com.example.sourcebase.service.IAnswerService;
import com.example.sourcebase.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements IAnswerService {
    private final IAnswerRepository answerRepository;
    private final AnswerMapper answerMapper;

    @Override
    public List<AnswerResDTO> getAllAnswers() {
        return answerRepository.findAll().stream().map(answerMapper::toAnswerResDto).toList();
    }

    @Override
    public Page<AnswerResDTO> getAllAnswers(int page, int size, String sortBy, boolean asc) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return answerRepository.findAll(pageable).map(answerMapper::toAnswerResDto);
    }

    @Override
    public AnswerResDTO getAnswerById(Long id) {
        return answerMapper.toAnswerResDto(
                answerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND))
        );
    }

    @Override
    @Transactional
    public AnswerResDTO addAnswer(AnswerReqDto answerReqDto) {
        return answerMapper.toAnswerResDto(answerRepository.save(answerMapper.toEntity(answerReqDto)));
    }

    @Override
    @Transactional
    public AnswerResDTO updateAnswer(Long id, AnswerReqDto answerReqDto) {
        return answerRepository.findById(id)
                .map(answer -> {
                    answer = answerMapper.partialUpdate(answerReqDto, answer);
                    return answerMapper.toAnswerResDto(answerRepository.save(answer));
                })
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteAnswer(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));
        answer.setDeleted(true);
        answerRepository.save(answer);
    }

    @Override
    public Page<AnswerResDTO> getAnswersByQuestionId(Long questionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return answerRepository.findByQuestion_Id(questionId, pageable).map(answerMapper::toAnswerResDto);
    }

    @Override
    public Page<AnswerResDTO> getAnswersByQuestionId(Long questionId, int page, int size, String sortBy, boolean asc) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return answerRepository.findByQuestion_Id(questionId, pageable).map(answerMapper::toAnswerResDto);
    }
}
