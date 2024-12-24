package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Answer;
import com.example.sourcebase.domain.Criteria;
import com.example.sourcebase.domain.Question;
import com.example.sourcebase.domain.dto.reqdto.AddQuestionReqDto;
import com.example.sourcebase.domain.dto.reqdto.QuestionReqDto;
import com.example.sourcebase.domain.dto.resdto.QuestionResDTO;
import com.example.sourcebase.exception.AppException;
import com.example.sourcebase.mapper.AnswerMapper;
import com.example.sourcebase.mapper.QuestionMapper;
import com.example.sourcebase.repository.IAnswerRepository;
import com.example.sourcebase.repository.ICriteriaRepository;
import com.example.sourcebase.repository.IQuestionRepository;
import com.example.sourcebase.service.IQuestionService;
import com.example.sourcebase.util.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionServiceImpl implements IQuestionService {

    IQuestionRepository questionRepository;
    ICriteriaRepository criteriaRepository;
    QuestionMapper questionMapper = QuestionMapper.INSTANCE;
    AnswerMapper answerMapper = AnswerMapper.INSTANCE;
    IAnswerRepository answerRepository;

    public List<QuestionResDTO> getAllQuestionByCriteriaID(Long criteriaId) {
        List<Question> questionResDTOs = questionRepository.findAllQuestionByCriteriaId(criteriaId);

        return questionResDTOs.stream()
                .map(questionMapper::toQuestionResDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<QuestionResDTO> findAllQuestions(int page, int size, String sortBy, boolean asc) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return questionRepository.findAll(pageable).map(questionMapper::toQuestionResDTO);
    }

    @Override
    public QuestionResDTO getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(questionMapper::toQuestionResDTO)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
    }

    /* @Override
    @Transactional
    public QuestionResDTO addQuestion(QuestionReqDto questionReqDto) {
        return questionMapper.toQuestionResDTO(questionRepository.save(questionMapper.toQuestion(questionReqDto)));
    }

     */

    @Override
    @Transactional
    public QuestionResDTO updateQuestion(Long id, QuestionReqDto questionReqDto) {
        return questionRepository.findById(id)
                .map(question -> {
                    question = questionMapper.partialUpdate(questionReqDto, question);
                    return questionMapper.toQuestionResDTO(questionRepository.save(question));
                })
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

        if (question.getAnswers() != null) {
            question.getAnswers().forEach(answer -> answer.setDeleted(true));
        }
        question.setDeleted(true);
        questionRepository.save(question);
    }

    @Override
    public Page<QuestionResDTO> findQuestionsByTitle(String title, int page, int size, String sortBy, boolean asc) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return questionRepository.findAllByTitleContains(title, pageable).map(questionMapper::toQuestionResDTO);
    }

    @Override
    public Page<QuestionResDTO> getQuestionsByCriteriaId(Long criteriaId, int page, int size, String sortBy, boolean asc) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return questionRepository.findAllByCriteria_Id(criteriaId, pageable).map(questionMapper::toQuestionResDTO);
    }


   @Transactional
    public QuestionResDTO addQuestionAndAnswers(AddQuestionReqDto addQuestionReqDto) {
        Question question = questionMapper.toQuestion(addQuestionReqDto);

       if (addQuestionReqDto.getCriteriaId() != null) {
           Criteria criteria = criteriaRepository.findById(addQuestionReqDto.getCriteriaId())
                   .orElseThrow(() -> new RuntimeException("Criteria not found with id: " + addQuestionReqDto.getCriteriaId()));
           question.setCriteria(criteria);
       }
       question = questionRepository.save(question);
       final Question finalQuestion = question;

        List<Answer> answers = addQuestionReqDto.getAnswers().stream()
                .map(answerReqDto -> {
                    Answer answerEntity = answerMapper.toEntity(answerReqDto);
                    answerEntity.setQuestion(finalQuestion);
                    return answerEntity;
                })
                .collect(Collectors.toList());
        question.setAnswers(answers);
        answerRepository.saveAll(answers);
        return questionMapper.toQuestionResDTO(question);
    }
}
