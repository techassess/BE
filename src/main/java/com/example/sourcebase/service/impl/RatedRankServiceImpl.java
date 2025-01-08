package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Assess;
import com.example.sourcebase.domain.AssessDetail;
import com.example.sourcebase.domain.Criteria;
import com.example.sourcebase.domain.dto.resdto.AssessDetailResDto;
import com.example.sourcebase.domain.dto.resdto.AssessResDTO;
import com.example.sourcebase.domain.dto.resdto.custom.OverallRatedResDto;
import com.example.sourcebase.domain.enumeration.ERank;
import com.example.sourcebase.domain.enumeration.ERole;
import com.example.sourcebase.domain.model.AverageValueInCriteria;
import com.example.sourcebase.domain.model.OverallOfACriterion;
import com.example.sourcebase.exception.AppException;
import com.example.sourcebase.mapper.AssessMapper;
import com.example.sourcebase.repository.IAssessDetailRepository;
import com.example.sourcebase.repository.IAssessRepository;
import com.example.sourcebase.repository.ICriteriaRepository;
import com.example.sourcebase.service.IRatedRankService;
import com.example.sourcebase.util.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatedRankServiceImpl implements IRatedRankService {
    IAssessRepository assessRepository;
    AssessMapper assessMapper;
    IAssessDetailRepository assessDetailRepository;
    ICriteriaRepository criteriaRepository;

    @Override
    public List<AverageValueInCriteria> getAverageValueOfCriteriaByTeam(Long userId) {
        // 1. Lấy danh sách đánh giá TEAM của userId
        List<AssessResDTO> assessesResDto = assessRepository.getListAssessTeamOfUserId(userId).stream()
                .map(assess -> {
                    AssessResDTO assessResDTO = assessMapper.toAssessResDto(assess);
                    assessResDTO.setAssessDetails(assessResDTO.getAssessDetails().stream()
                            .peek(assessDetail -> assessDetail.setAssessId(assessResDTO.getId()))
                            .collect(Collectors.toList()));
                    return assessResDTO;
                })
                .collect(Collectors.toList());

        // 2. Tính giá trị trung bình của các tiêu chí (criteriaID giống nhau)
        Map<Long, Double> result = new HashMap<>();
        assessesResDto.forEach(assessResDTO -> {
            Map<Long, List<AssessDetailResDto>> groupedByCriteria = assessResDTO.getAssessDetails().stream()
                    .collect(Collectors.groupingBy(detail -> detail.getCriteria().getId()));

            groupedByCriteria.forEach((criteriaId, assessDetails) -> {
                double averageValue = assessDetails.stream()
                        .mapToDouble(AssessDetailResDto::getValue)
                        .average()
                        .orElse(0.0);

                if (Math.round(averageValue) != 0) {
                    result.put(criteriaId, averageValue);
                }
            });
        });

        // 3. Trả về kết quả
        return convertMapAvgToList(result);
    }

    public List<AverageValueInCriteria> getAverageValueOfCriteriaBySelf(Long userId) {
        // 1. Lấy danh sách đánh giá SELF của userId
        Assess assess = assessRepository.getAssessBySelf(userId);
        AssessResDTO assessResDTO = assessMapper.toAssessResDto(assess);
        assessResDTO.setAssessDetails(assessResDTO.getAssessDetails().stream()
                .peek(assessDetail -> assessDetail.setAssessId(assessResDTO.getId()))
                .collect(Collectors.toList()));
        // 2. Tính giá trị trung bình của các tiêu chí (criteriaID giống nhau)
        Map<Long, Double> result = new HashMap<>();
        Map<Long, List<AssessDetailResDto>> groupedByCriteria = assessResDTO.getAssessDetails().stream()
                .collect(Collectors.groupingBy(detail -> detail.getCriteria().getId()));

        groupedByCriteria.forEach((criteriaId, assessDetails) -> {
            double averageValue = assessDetails.stream()
                    .mapToDouble(AssessDetailResDto::getValue)
                    .average()
                    .orElse(0.0);

            result.put(criteriaId, averageValue);
        });
        // 3. Trả về kết quả
        return convertMapAvgToList(result);
    }


    @Override
    public List<AverageValueInCriteria> getAverageValueOfCriteriaByManager(Long userId) {
        // find all assess of user
        List<Assess> aList = assessRepository.findByToUser_Id(userId);
        if (aList.isEmpty()) {
            throw new AppException(ErrorCode.ASSESS_IS_NOT_EXIST);
        }

        // find all the assesses by manager
        List<Assess> managerAssessesList = new ArrayList<>();
        for (Assess a : aList) {
            boolean isManager = a.getUser().getUserRoles().stream()
                    .anyMatch(ur -> ur.getRole().getId() == ERole.MANAGER.getValue());
            if (isManager) {
                managerAssessesList.add(a);
            }
        }
        if (managerAssessesList.isEmpty()) {
            throw new AppException(ErrorCode.MANAGER_ASSESS_IS_NOT_EXIST);
        }

        // get the newest assess
        Assess newestAssess = managerAssessesList.getLast();
        // find all the assess detail of the newest assess. now the adList have the same assessId and assessmentDate
        List<AssessDetail> adList = assessDetailRepository
                .findByAssess_IdAndAssess_AssessmentDate(
                        newestAssess.getId(),
                        newestAssess.getAssessmentDate()
                );

        //group by assessId and criteriaId and get average point on scale 5
        Map<Long, Double> averagePointPerCriteria = adList.stream()
                .collect(Collectors.groupingBy(
                        ad -> ad.getCriteria().getId(),
                        Collectors.averagingDouble(AssessDetail::getValue)
                ));

        return convertMapAvgToList(averagePointPerCriteria);
    }

    @Override
    public OverallRatedResDto getOverallRatedOfAUser(Long userId) {
        OverallRatedResDto result = new OverallRatedResDto();
        // get average value of criteria by team
        List<AverageValueInCriteria> averageValueByTeam = getAverageValueOfCriteriaByTeam(userId);
        // get average value of criteria by self
        List<AverageValueInCriteria> averageValueBySelf = getAverageValueOfCriteriaBySelf(userId);
        // get average value of criteria by manager
        List<AverageValueInCriteria> averageValueByManager = getAverageValueOfCriteriaByManager(userId);

        // Step 1: Collect all unique criteriaIds
        Set<Long> allCriteriaIds = new HashSet<>();
        addCriteriaIds(allCriteriaIds, averageValueByManager);
        addCriteriaIds(allCriteriaIds, averageValueBySelf);
        addCriteriaIds(allCriteriaIds, averageValueByTeam);

        // Step 2: Normalize each list
        normalizeList(averageValueByManager, allCriteriaIds);
        normalizeList(averageValueBySelf, allCriteriaIds);
        normalizeList(averageValueByTeam, allCriteriaIds);

        int selfWeight, teamWeight, managerWeight;

        Criteria c;
        OverallOfACriterion overallOfACriterion;
        List<OverallOfACriterion> overallOfCriteria = new ArrayList<>();

        int totalCriteriaPoint = 0;
        double totalUserPoint = 0D;

        for (int i = 0; i < averageValueByTeam.size(); i++) {
            AverageValueInCriteria team = averageValueByTeam.get(i);
            AverageValueInCriteria self = averageValueBySelf.get(i);
            AverageValueInCriteria manager = averageValueByManager.get(i);

            selfWeight = (team.getAverageValue() == 0) ? 0 : 1;
            teamWeight = (self.getAverageValue() == 0) ? 0 : 1;
            managerWeight = (manager.getAverageValue() == 0) ? 0 : 1;

            // check if criteriaId of team, self, manager are the same
            if (!Objects.equals(team.getCriteriaId(), self.getCriteriaId())
                    || !Objects.equals(team.getCriteriaId(), manager.getCriteriaId())) {
                throw new AppException(ErrorCode.CRITERIA_ID_NOT_MATCH);
            }

            // get point of criterion
            c = criteriaRepository.findById(team.getCriteriaId()).orElseThrow(() -> new AppException(ErrorCode.CRITERIA_NOT_FOUND));
            int pointOfCriterion = c.getPoint();

            // calculate point of user of a criterion
            double userPointOfCriteria = (team.getAverageValue() * teamWeight + self.getAverageValue() * selfWeight + manager.getAverageValue() * managerWeight)
                    / (teamWeight + selfWeight + managerWeight)
                    / 5
                    * pointOfCriterion;

            // calculate total point
            totalCriteriaPoint += pointOfCriterion;
            totalUserPoint += userPointOfCriteria;

            // map to OverallOfACriterion
            overallOfACriterion = new OverallOfACriterion(
                    team.getCriteriaId(),
                    pointOfCriterion,
                    c.getTitle(),
                    round(self.getAverageValue(), 1),
                    round(team.getAverageValue(), 1),
                    round(manager.getAverageValue(), 1),
                    round(userPointOfCriteria, 2)
            );
            overallOfCriteria.add(overallOfACriterion);
        }

        // set list overall of criteria
        result.setOverallOfCriteria(overallOfCriteria);

        // calculate overall point
        double overallPoint = totalUserPoint / totalCriteriaPoint * 100;
        result.setOverallPoint(round(overallPoint, 2));

        // calculate rank
        result.setRank(calculateRank(overallPoint).getValue());

        // calculate level up recommend
        result.setLevelUpRecommend(calculateLevelUpRecommend(overallPoint));

        return result;
    }

    private List<AverageValueInCriteria> convertMapAvgToList(Map<Long, Double> map) {
        List<AverageValueInCriteria> result = new ArrayList<>();
        map.forEach((criteriaId, avgValue) -> result.add(new AverageValueInCriteria(criteriaId, avgValue)));
        result.sort(Comparator.comparing(AverageValueInCriteria::getCriteriaId));
        return result;
    }

    private ERank calculateRank(double overallPoint) {
        if (overallPoint >= 90) {
            return ERank.A_PLUS;
        } else if (overallPoint >= 80) {
            return ERank.A;
        } else if (overallPoint >= 70) {
            return ERank.B_PLUS;
        } else if (overallPoint >= 50) {
            return ERank.B;
        } else if (overallPoint > 30) {
            return ERank.C_PLUS;
        } else {
            return ERank.C;
        }
    }

    private int calculateLevelUpRecommend(double overallPoint) {
        if (overallPoint >= 90) {
            return 3;
        } else if (overallPoint >= 80) {
            return 2;
        } else if (overallPoint >= 70) {
            return 1;
        } else {
            return 0;
        }
    }

    private void addCriteriaIds(Set<Long> allCriteriaIds, List<AverageValueInCriteria> list) {
        for (AverageValueInCriteria item : list) {
            allCriteriaIds.add(item.getCriteriaId());
        }
    }

    private void normalizeList(List<AverageValueInCriteria> list, Set<Long> allCriteriaIds) {
        Map<Long, AverageValueInCriteria> map = list.stream()
                .collect(Collectors.toMap(AverageValueInCriteria::getCriteriaId, item -> item));

        for (Long criteriaId : allCriteriaIds) {
            if (!map.containsKey(criteriaId)) {
                list.add(new AverageValueInCriteria(criteriaId, 0.0));
            }
        }
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.doubleValue();
    }
}
