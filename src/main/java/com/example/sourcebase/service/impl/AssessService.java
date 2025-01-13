package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.*;
import com.example.sourcebase.domain.dto.reqdto.AssessReqDTO;
import com.example.sourcebase.domain.dto.resdto.AssessResDTO;
import com.example.sourcebase.domain.enumeration.ETypeAssess;
import com.example.sourcebase.exception.AppException;
import com.example.sourcebase.mapper.AnswerMapper;
import com.example.sourcebase.mapper.AssessDetailMapper;
import com.example.sourcebase.mapper.AssessMapper;
import com.example.sourcebase.mapper.CriteriaMapper;
import com.example.sourcebase.repository.*;
import com.example.sourcebase.service.IAssessService;
import com.example.sourcebase.util.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AssessService implements IAssessService {
    AssessMapper assessMapper;
    AssessDetailMapper assessDetailMapper;
    CriteriaMapper criteriaResMapper;
    AnswerMapper answerMapper;
    IAssessRepository assessRepository;
    IUserRepository userRepository;
    IAssessDetailRepository assessDetailRepository;
    ICriteriaRepository criteriaRepository;
    IQuestionRepository questionRepository;
    IAnswerRepository answerRepository;

    @Override
    @Transactional
    public AssessResDTO updateAssess(AssessReqDTO assessReqDto) {
        ETypeAssess type;
        User user = userRepository.findById(assessReqDto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<UserRole> userRoles = user.getUserRoles();

        if (assessReqDto.getToUserId().equals(assessReqDto.getUserId())) {
            type = ETypeAssess.SELF;
        } else {
            boolean isManager = userRoles.stream().anyMatch(item -> item.getRole().getName().equalsIgnoreCase("Manager"));
            if (isManager) {
                type = ETypeAssess.MANAGER;
            } else {
                type = ETypeAssess.TEAM;
            }
        }
        User userReview = userRepository.findById(assessReqDto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User toUser = userRepository.findById(assessReqDto.getToUserId())
                .orElseThrow(() -> new AppException(ErrorCode.TO_USER_NOT_FOUND));

        List<Assess> aList = assessRepository.findByUser_IdAndToUser_Id(assessReqDto.getUserId(), assessReqDto.getToUserId());

        Assess toBeSavedAssess;
        Assess savedAssess;
        if (!aList.isEmpty()) { // update
            Assess newestAssess = aList.getLast(); // now have assessId
            toBeSavedAssess = newestAssess; //persisted, but not list assessDetails

            toBeSavedAssess.setUser(userReview);
            toBeSavedAssess.setToUser(toUser);
            toBeSavedAssess.setAssessmentType(type);
            toBeSavedAssess.setAssessmentDate(LocalDate.now());
            toBeSavedAssess.setSubmitted(assessReqDto.isSubmitted());

            // merge assess detail
            List<AssessDetail> assessDetailsToUpdate = assessDetailMapper.toEntityList(assessReqDto.getAssessDetails());
            List<AssessDetail> assessDetails = newestAssess.getAssessDetails(); // not being persisted, now have to merge
            assessDetailsToUpdate.forEach(assessDetail -> {
                        AssessDetail ad = assessDetails.stream()
                                .filter(item -> {
                                    if (item.getQuestion() == null) {
                                        return item.getAssess().getId().equals(newestAssess.getId()) &&
                                                item.getCriteria().getId().equals(assessDetail.getCriteria().getId());
                                    }
                                    return item.getAssess().getId().equals(newestAssess.getId()) &&
                                            item.getCriteria().getId().equals(assessDetail.getCriteria().getId()) &&
                                            item.getQuestion().getId().equals(assessDetail.getQuestion().getId());
                                })
                                .findFirst()
                                .orElseThrow(() -> new AppException(ErrorCode.ASSESS_DETAIL_NOT_FOUND));
                        ad.setValue(assessDetail.getValue());
                        ad.setDescription(assessDetail.getDescription());
                        ad.setAssess(newestAssess);
                    }
            );

            toBeSavedAssess.setAssessDetails(assessDetails);
            return assessMapper.toAssessResDto(toBeSavedAssess);
        } else { // create
            toBeSavedAssess = assessMapper.toAssess(assessReqDto);

            toBeSavedAssess.setUser(userReview);
            toBeSavedAssess.setToUser(toUser);
            toBeSavedAssess.setAssessmentType(type);
            toBeSavedAssess.setAssessmentDate(LocalDate.now());
            toBeSavedAssess.setSubmitted(assessReqDto.isSubmitted());

            List<AssessDetail> assessDetails = assessDetailMapper.toEntityList(assessReqDto.getAssessDetails());
            assessDetails.forEach(assessDetail -> {
                // merge Criteria
                Criteria criteria = criteriaRepository.findById(assessDetail.getCriteria().getId())
                        .orElseThrow(() -> new AppException(ErrorCode.CRITERIA_NOT_FOUND));
                assessDetail.setCriteria(criteria);

                // merge Question
                Question toBeSavedQuestion = questionRepository.findById(assessDetail.getQuestion().getId())
                        .orElse(null);
                assessDetail.setQuestion(toBeSavedQuestion);

                assessDetail.setAssess(toBeSavedAssess);
            });

            toBeSavedAssess.setAssessDetails(assessDetails);
            savedAssess = assessRepository.saveAndFlush(toBeSavedAssess);
        }
        return assessMapper.toAssessResDto(savedAssess);
    }

    @Override
    public List<AssessResDTO> getListAssessOfUserId(Long userId) {
        return assessRepository.getListAssessOfUserId(userId).stream()
                .map(assess -> {
                    AssessResDTO assessResDTO = assessMapper.toAssessResDto(assess);
                    assessResDTO.setAssessDetails(assessResDTO.getAssessDetails().stream()
                            .peek(assessDetail -> assessDetail.setAssess(assessResDTO))
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
        List<Assess> assessList = assessRepository.findByToUserIdAndAssessmentType(userId, ETypeAssess.SELF);
        if (assessList.isEmpty()) {
            throw new AppException(ErrorCode.ASSESS_IS_NOT_EXIST);
        }
        //        System.out.println(myAssess);
        return assessMapper.toAssessResDto(assessList.getLast());
    }

    @Override
    public List<AssessResDTO> getListAssessByUserId(Long userId) {
        return assessRepository.getListAssessByUserId(userId).stream()
                .map(assess -> {
                    AssessResDTO assessResDTO = assessMapper.toAssessResDto(assess);
                    assessResDTO.setAssessDetails(assessResDTO.getAssessDetails().stream()
                            .peek(assessDetail -> assessDetail.setAssess(assessResDTO))
                            .collect(Collectors.toList()));
                    return assessResDTO;
                })
                .collect(Collectors.toList());
    }


}
