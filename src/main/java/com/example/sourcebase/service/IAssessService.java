package com.example.sourcebase.service;

import com.example.sourcebase.domain.dto.reqdto.AssessReqDTO;
import com.example.sourcebase.domain.dto.resdto.AssessResDTO;

import java.util.List;

public interface IAssessService {
    AssessResDTO saveAssess(AssessReqDTO assessReqDto);

    AssessResDTO updateAssess(AssessReqDTO assessReqDto, Long assessId);

    List<AssessResDTO> getListAssessOfUserId(Long userId, Long projectId);

    boolean isSubmitForm(Long userId, Long toUserId);

    AssessResDTO getAssess(Long userId, Long projectId);

    List<AssessResDTO> getListAssessByUserId(Long userId, Long projectId);
}
