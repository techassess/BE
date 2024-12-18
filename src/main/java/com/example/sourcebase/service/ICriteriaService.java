package com.example.sourcebase.service;

import com.example.sourcebase.domain.dto.reqdto.CriteriaReqDTO;
import com.example.sourcebase.domain.dto.resdto.CriteriaResDTO;

import java.util.List;

public interface ICriteriaService {
    List<CriteriaResDTO> getAllCriterias();
}
