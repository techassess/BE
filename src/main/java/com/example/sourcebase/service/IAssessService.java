package com.example.sourcebase.service;

import com.example.sourcebase.domain.dto.reqdto.AssessReqDTO;
import com.example.sourcebase.domain.dto.resdto.AssessResDTO;

import java.util.List;

public interface IAssessService {
    AssessResDTO updateAssess(AssessReqDTO assessReqDto);
    List<AssessResDTO> getListAssessOfUserId(Long userId);

    boolean isSubmitForm(Long userId, Long toUserId);

    AssessResDTO getAssess(Long userId);

    List<AssessResDTO> getListAssessByUserId(Long userId);
}
