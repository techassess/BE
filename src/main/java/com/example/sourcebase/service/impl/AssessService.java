package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Assess;
import com.example.sourcebase.domain.AssessDetail;
import com.example.sourcebase.domain.User;
import com.example.sourcebase.domain.UserRole;
import com.example.sourcebase.mapper.AssessDetailMapper;
import com.example.sourcebase.mapper.CriteriaMapper;
import com.example.sourcebase.mapper.QuestionMapper;
import com.example.sourcebase.repository.*;
import com.example.sourcebase.mapper.AssessMapper;
import com.example.sourcebase.domain.dto.reqdto.AssessReqDTO;
import com.example.sourcebase.domain.dto.resdto.AssessResDTO;
import com.example.sourcebase.domain.enumeration.ETypeAssess;
import com.example.sourcebase.service.IAssessService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AssessService implements IAssessService {
    AssessMapper assessMapper = AssessMapper.INSTANCE;
    AssessDetailMapper assessDetailMapper = AssessDetailMapper.INSTANCE;
    CriteriaMapper criteriaResMapper = CriteriaMapper.INSTANCE;
    QuestionMapper questionResMapper = QuestionMapper.INSTANCE;
    IAssessRepository assessRepository;
    IUserRepository userRepository;
    IAssessDetailRepository assessDetailRepository;
    ICriteriaRepository criteriaRepository;
    IQuestionRepository questionRepository;

    @Override
    public AssessResDTO updateAssess(AssessReqDTO assessReqDto) {
        ETypeAssess type = null;
        User user = userRepository.findById(Long.valueOf(assessReqDto.getUserId())).get();
        List<UserRole> userRoles = user.getUserRoles();
        if (assessReqDto.getToUserId().equals(assessReqDto.getUserId())) {
            type = ETypeAssess.SELF;
        } else {
            boolean isManager = userRoles.stream().anyMatch(item -> item.getRole().getName().equalsIgnoreCase("Manager"));
            if (isManager) {
                type = ETypeAssess.MANAGER;
            }else {
                type = ETypeAssess.TEAM;
            }
        }
        User userReview = userRepository.findById(Long.valueOf(assessReqDto.getUserId())).get();
        User toUser = userRepository.findById(Long.valueOf(assessReqDto.getToUserId())).get();
        Assess assess = assessMapper.toAssess(assessReqDto);
        assess.setUser(userReview);
        assess.setToUser(toUser);
        assess.setAssessmentType(type);
        assess.setTotalPoint(Integer.parseInt(assessReqDto.getTotalPoint()));
        assess.setAssessmentDate(LocalDate.now());

        assessReqDto.getAssessDetails().forEach(item -> {
            AssessDetail assessDetail = assessDetailMapper.toAssessDetail(item);
            assessDetail.setAssess(assess);
            if (item.getCriteriaId().equals("6") || item.getCriteriaId().equals("7") || item.getCriteriaId().equals("8")) {
                assessDetail.setComment(true);
            }
            if (item.getQuestionId() != null) {
                assessDetail.setQuestion(questionRepository.findById(Long.valueOf(item.getQuestionId())).get());
            } else {
                assessDetail.setQuestion(null);
            }
            assessDetail.setCriteria(criteriaRepository.findById(Long.valueOf(item.getCriteriaId())).get());
        });
        assess.setAssessDetails(assessReqDto.getAssessDetails().stream()
                .map(item -> {
                    AssessDetail assessDetail = assessDetailMapper.toAssessDetail(item);
                    assessDetail.setAssess(assess);
                    if (item.getCriteriaId().equals("6") || item.getCriteriaId().equals("7") || item.getCriteriaId().equals("8")) {
                        assessDetail.setComment(true);
                    }
                    if (item.getQuestionId() != null) {
                        assessDetail.setQuestion(questionRepository.findById(Long.valueOf(item.getQuestionId())).get());
                    } else {
                        assessDetail.setQuestion(null);
                    }
                    assessDetail.setCriteria(criteriaRepository.findById(Long.valueOf(item.getCriteriaId())).get());
                    return assessDetail;
                })
                .collect(Collectors.toList()));

        assessRepository.save(assess);

        return assessMapper.toAssessResDto(assess);
    }

    @Override
    public List<AssessResDTO> getListAssessOfUserId(Long userId) {
        return assessRepository.getListAssessOfUserId(userId).stream()
                .map(assess -> {
                    AssessResDTO assessResDTO = assessMapper.toAssessResDto(assess);
                    assessResDTO.setAssessDetails(assessResDTO.getAssessDetails().stream()
                            .peek(assessDetail -> assessDetail.setAssessId(assessResDTO.getId()))
                            .collect(Collectors.toList()));
                    return assessResDTO;
                })
                .collect(Collectors.toList());
    }


    @Override
    public boolean isSubmitForm(Long userId, Long toUserId) {
        return false;
    }

    @Override
    public AssessResDTO getAssess(Long userId) {
        AssessResDTO myAssess = assessMapper.toAssessResDto(assessRepository.findByToUserIdAndAssessmentType(userId, ETypeAssess.SELF));
        myAssess.getAssessDetails().forEach(item -> item.setAssessId(myAssess.getId()));
        return myAssess;
    }

    @Override
    public List<AssessResDTO> getListAssessByUserId(Long userId) {
        return assessRepository.getListAssessByUserId(userId).stream()
                .map(assess -> {
                    AssessResDTO assessResDTO = assessMapper.toAssessResDto(assess);
                    assessResDTO.setAssessDetails(assessResDTO.getAssessDetails().stream()
                            .peek(assessDetail -> assessDetail.setAssessId(assessResDTO.getId()))
                            .collect(Collectors.toList()));
                    return assessResDTO;
                })
                .collect(Collectors.toList());
    }
}
