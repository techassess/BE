package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.*;
import com.example.sourcebase.domain.dto.reqdto.AssessDetailUpdateReqDTO;
import com.example.sourcebase.domain.dto.reqdto.AssessReqDTO;
import com.example.sourcebase.domain.dto.resdto.AssessResDTO;
import com.example.sourcebase.domain.dto.resdto.AssessResUpdateDTO;
import com.example.sourcebase.domain.enumeration.ETypeAssess;
import com.example.sourcebase.exception.AppException;
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
    IAssessRepository assessRepository;
    IUserRepository userRepository;
    IAssessDetailRepository assessDetailRepository;
    ICriteriaRepository criteriaRepository;
    IQuestionRepository questionRepository;
    IProjectRepository projectRepository;
    UserService userService;
    IUserProjectRepository userProjectRepository;

    @Override
    public List<AssessResDTO> getAssess(Long userId, Long toUserId, Long projectId, String assessmentType) {
        return assessRepository.getAssess(userId, toUserId, projectId, assessmentType).stream()
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
    @Transactional
    public AssessResDTO saveAssess(AssessReqDTO assessReqDto) {
        User user = userRepository.findById(Long.valueOf(assessReqDto.getUserId()))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User toUser = userRepository.findById(Long.valueOf(assessReqDto.getToUserId()))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Project project = projectRepository.findById(Long.valueOf(assessReqDto.getProjectId()))
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        ETypeAssess type = determineAssessmentType(assessReqDto, project.getLeader().getId());

        Assess assess = assessMapper.toAssess(assessReqDto);
        assess.setAssessmentType(type);
        assess.setAssessmentDate(LocalDate.now());

        assess.getAssessDetails().forEach(ad -> {
            if (ad.getQuestion().getId() != null) {
                ad.setQuestion(questionRepository.findById(ad.getQuestion().getId()).orElse(null));
            } else {
                ad.setQuestion(null);
            }
            ad.setCriteria(criteriaRepository.findById(ad.getCriteria().getId()).orElse(null));
        });

        assessRepository.save(assess);

        return assessMapper.toAssessResDto(assess);
    }

    @Override
    @Transactional
    public AssessResDTO updateAssess(AssessReqDTO assessReqDto, Long assessId) {
        Assess assessToUpdate = assessRepository.findById(assessId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSESS_IS_NOT_EXIST));

        assessToUpdate = assessMapper.updateAssess(assessReqDto, assessToUpdate);
        return assessMapper.toAssessResDto(assessRepository.save(assessToUpdate));
    }

    private ETypeAssess determineAssessmentType(AssessReqDTO assessReqDto, Long leaderId) {
        if (assessReqDto.getToUserId().equals(assessReqDto.getUserId())) {
            return ETypeAssess.SELF;
        }
        return leaderId.equals(Long.valueOf(assessReqDto.getUserId()))
                ? ETypeAssess.MANAGER
                : ETypeAssess.TEAM;
    }

    @Override
    public List<AssessResDTO> getListAssessOfUserId(Long userId, Long projectId) {
        return assessRepository.findByToUser_IdAndProject_Id(userId, projectId).stream()
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
    public AssessResDTO getAssesss(Long userId, Long projectId) {
        return assessMapper.toAssessResDto(assessRepository.findByToUserIdAndAssessmentTypeAndProjectId(userId, ETypeAssess.SELF, projectId));
    }

    @Override
    public List<AssessResDTO> getListAssessByUserId(Long userId, Long projectId) {
        return assessRepository.findByUser_IdAndProject_Id(userId, projectId).stream()
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
    @Transactional
    public AssessResUpdateDTO updateAssessDetails(Long assessId, List<AssessDetailUpdateReqDTO> assessDetails) {
        Assess assess = assessRepository.findById(assessId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSESS_IS_NOT_EXIST));

        List<AssessDetail> updatedDetails = assessDetails.stream()
                .map(detailDto -> {
                    AssessDetail detail = assessDetailRepository.findById(detailDto.getAssessDetailId())
                            .orElseThrow(() -> new AppException(ErrorCode.ASSESS_IS_NOT_EXIST));
                    Criteria criteria = criteriaRepository.findById(Long.parseLong(detailDto.getCriteriaId()))
                            .orElseThrow(() -> new AppException(ErrorCode.CRITERIA_NOT_FOUND));
                    detail.setCriteria(criteria);
                    assessDetailMapper.updateAssessDetailDto(detailDto, detail);
                    if (detailDto.getQuestionId() != null) {
                        Question question = questionRepository.findById(Long.parseLong(detailDto.getQuestionId()))
                                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
                        detail.setQuestion(question);
                    } else {
                        detail.setQuestion(null);
                    }
                    return detail;
                })
                .toList();
        assessDetailRepository.saveAll(updatedDetails);
        return assessMapper.toAssessResUpdateDto(assess);
    }


}
