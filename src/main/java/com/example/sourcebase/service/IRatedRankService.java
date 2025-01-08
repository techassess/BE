package com.example.sourcebase.service;

import com.example.sourcebase.domain.dto.resdto.custom.OverallRatedResDto;
import com.example.sourcebase.domain.model.AverageValueInCriteria;

import java.util.List;

public interface IRatedRankService {
    List<AverageValueInCriteria> getAverageValueOfCriteriaByTeam(Long userId);

    List<AverageValueInCriteria> getAverageValueOfCriteriaBySelf(Long userId);

    List<AverageValueInCriteria> getAverageValueOfCriteriaByManager(Long userId);

    OverallRatedResDto getOverallRatedOfAUser(Long userId);
}
