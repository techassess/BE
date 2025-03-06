package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.Assess;
import com.example.sourcebase.domain.AssessDetail;
import com.example.sourcebase.domain.Criteria;
import com.example.sourcebase.domain.dto.resdto.custom.OverallRatedResDto;
import com.example.sourcebase.domain.enumeration.ERank;
import com.example.sourcebase.domain.enumeration.ETypeAssess;
import com.example.sourcebase.domain.model.AverageValueInCriteria;
import com.example.sourcebase.domain.model.OverallOfACriterion;
import com.example.sourcebase.exception.AppException;
import com.example.sourcebase.repository.IAssessDetailRepository;
import com.example.sourcebase.repository.IAssessRepository;
import com.example.sourcebase.repository.ICriteriaRepository;
import com.example.sourcebase.repository.IUserProjectRepository;
import com.example.sourcebase.service.IRatedRankService;
import com.example.sourcebase.service.IUserService;
import com.example.sourcebase.util.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatedRankServiceImpl implements IRatedRankService {
    IAssessRepository assessRepository;
    IAssessDetailRepository assessDetailRepository;
    ICriteriaRepository criteriaRepository;
    IUserProjectRepository userProjectRepository;
    IUserService userService;


    /**
     * Lấy list giá trị trung bình của các tiêu chí dựa trên toUserId, projectId và assessmentType
     * <p>
     * cách tính giá trị trung bình của các tiêu chí: <br>
     * -
     *
     * @param toUserId       người được đánh giá
     * @param projectId      dự án
     * @param assessmentType loại đánh giá
     * @return danh sách giá trị trung bình của các tiêu chí
     */
    private List<AverageValueInCriteria> getAvgValueOfCriteriaByToUserIdAndProjectIdAndAssessmentType(Long toUserId, Long projectId, ETypeAssess assessmentType) {
        // 1. Lấy danh sách đánh giá dựa trên toUserId, projectId và assessmentType
        // lúc này bản ghi đánh giá sẽ có cùng projectId, toUserId và assessmentType
        List<Assess> assesses = assessRepository.findAllByToUser_IdAndProject_IdAndAssessmentType(toUserId, projectId, assessmentType);
        if (assesses.isEmpty()) {
            switch (assessmentType) {
                case SELF -> throw new AppException(ErrorCode.SELF_ASSESS_IS_NOT_EXIST);
                case TEAM -> {
                    // todo: trường hợp team chỉ có 2 người sẽ không có đánh giá của team -> không trả về lỗi
                    if (userService.getAllUserHadSameProject(toUserId, projectId).size() == 1) {
                        return new ArrayList<>();
                    }
                    throw new AppException(ErrorCode.TEAM_ASSESS_IS_NOT_EXIST);
                }
                case MANAGER -> throw new AppException(ErrorCode.MANAGER_ASSESS_IS_NOT_EXIST);
            }
        }

        // 2. Tính giá trị trung bình của các tiêu chí (criteriaID giống nhau)
        // tùy thuộc vào assessmentType sẽ có cách tính khác nhau

        Map<Long, Double> averagePointPerCriteria = new HashMap<>(); // key: criteriaId, value: avg
        switch (assessmentType) {
            case SELF, MANAGER -> { // chỉ có 1 bản ghi -> điểm trung bình của 1 criteria
                Assess assess = assesses.getFirst();
                List<AssessDetail> assessDetails = assessDetailRepository.findByAssess_Id(assess.getId());

                // group by criteriaId and get average point on scale 5
                averagePointPerCriteria = assessDetails.stream()
                        .collect(Collectors.groupingBy(
                                ad -> ad.getCriteria().getId(),
                                Collectors.collectingAndThen( // xử lý tính tổng criteria dựa trên trọng số của question
                                        Collectors.toList(),
                                        (ads -> { // cùng criteriaId
                                            // Nếu criteria không có question thì trả về 0
                                            // Nếu criteria có question thì điểm cri thang 5 = tổng điểm thực các câu hỏi / điểm của criteria * 5
                                            double criteriaAvgS5;

                                            if (ads.getFirst().getQuestion() == null) { // trường hợp criteria không có question
                                                return 0D;
                                            }

                                            double cPoint = ads.getFirst().getCriteria().getPoint();
                                            double cRealPoint = ads.stream()
                                                    .mapToDouble(ad -> (double) ad.getValue() / 5 * ad.getQuestion().getPoint())
                                                    .sum();

                                            criteriaAvgS5 = cRealPoint / cPoint * 5;
                                            return criteriaAvgS5;
                                        })
                                )
                        ));
            }
            case TEAM -> { // có nhiều assess -> cần tính trung bình của 'trung bình 1 criteria' của các assess
                // mỗi assess có nhiều assessDetail
                // ta có 1 list của list assessDetail
                List<List<AssessDetail>> groupedByCriteria = assesses.stream()
                        .map(assess -> assessDetailRepository.findByAssess_Id(assess.getId()))
                        .toList();

                // list chứa giá trị trung bình của từng criteria của từng assess
                List<Map<Long, Double>> listOfAvgPointPerCriteria = new ArrayList<>();

                // -> tính trung bình từng criteria của từng asssess, sau đó trung bình các giá trị đó
                groupedByCriteria.forEach(assessDetailsOfAnAssess -> {
                    Map<Long, Double> avgPointPerCriteria = assessDetailsOfAnAssess.stream()
                            .collect(Collectors.groupingBy(
                                    ad -> ad.getCriteria().getId(), // group by criteriaId
                                    Collectors.collectingAndThen( // xử lý tính tổng criteria dựa trên trọng số của question
                                            Collectors.toList(),
                                            (ads -> { // cùng criteriaId
                                                // Nếu criteria không có question thì trả về 0
                                                // Nếu criteria có question thì điểm cri thang 5 = tổng điểm thực các câu hỏi / điểm của criteria * 5
                                                double criteriaAvgS5;

                                                if (ads.getFirst().getQuestion() == null) { // trường hợp criteria không có question
                                                    return 0D;
                                                }

                                                double cPoint = ads.getFirst().getCriteria().getPoint();
                                                double cRealPoint = ads.stream()
                                                        .mapToDouble(ad -> (double) ad.getValue() / 5 * ad.getQuestion().getPoint())
                                                        .sum();

                                                criteriaAvgS5 = cRealPoint / cPoint * 5;
                                                return criteriaAvgS5;
                                            })
                                    )
                            ));
                    listOfAvgPointPerCriteria.add(avgPointPerCriteria); // add to list
                });

                // trung bình các giá trị của từng criteria của mọi assess
                Map<Long, Double> avgPointPerCriteriaOfAllAssess = new HashMap<>();
                avgPointPerCriteriaOfAllAssess = listOfAvgPointPerCriteria.stream()
                        .flatMap(map -> map.entrySet().stream())
                        .collect(Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.averagingDouble(Map.Entry::getValue)
                        ));
                averagePointPerCriteria = avgPointPerCriteriaOfAllAssess;
            }
        }
        return convertMapAvgToList(averagePointPerCriteria);
    }

    @Override
    public OverallRatedResDto getOverallRatedOfAUserInAllProject(Long toUserId) {
        // get all project of user
        List<Long> projectIds = userProjectRepository.findAllByUserId(toUserId).stream()
                .map(userProject -> userProject.getProject().getId())
                .toList();

        // get overall rated of a user by project
//        List<OverallRatedResDto> overallRatedResDtos = projectIds.stream()
//                .map(projectId -> getOverallRatedOfAUserByProject(toUserId, projectId))
//                .toList();

        // normalize each list
//        List<OverallOfACriterion> overallOfCriteria = new ArrayList<>();
//        for (OverallRatedResDto overallRatedResDto : overallRatedResDtos) {
//            overallOfCriteria.addAll(overallRatedResDto.getOverallOfCriteria());
//        }


        return getOverallRatedOfAUserByProject(toUserId, projectIds.getFirst());
    }

    @Override
    public OverallRatedResDto getOverallRatedOfAUserByProject(Long toUserId, Long projectId) {
        // get average value of criteria by self
        List<AverageValueInCriteria> averageValueBySelf = getAvgValueOfCriteriaByToUserIdAndProjectIdAndAssessmentType(toUserId, projectId, ETypeAssess.SELF);
        // get average value of criteria by team
        List<AverageValueInCriteria> averageValueByTeam = getAvgValueOfCriteriaByToUserIdAndProjectIdAndAssessmentType(toUserId, projectId, ETypeAssess.TEAM);
        // get average value of criteria by manager
        List<AverageValueInCriteria> averageValueByManager = getAvgValueOfCriteriaByToUserIdAndProjectIdAndAssessmentType(toUserId, projectId, ETypeAssess.MANAGER);

        // Step 1: Collect all unique criteriaIds
        Set<Long> allCriteriaIds = new HashSet<>();
        addCriteriaIds(allCriteriaIds, averageValueByManager);
        addCriteriaIds(allCriteriaIds, averageValueBySelf);
        addCriteriaIds(allCriteriaIds, averageValueByTeam);

        // Step 2: Normalize each list
        normalizeList(averageValueByManager, allCriteriaIds);
        normalizeList(averageValueBySelf, allCriteriaIds);
        normalizeList(averageValueByTeam, allCriteriaIds);

        // Step 3: sort all lists by criteria id
        averageValueByManager.sort(Comparator.comparing(AverageValueInCriteria::getCriteriaId));
        averageValueBySelf.sort(Comparator.comparing(AverageValueInCriteria::getCriteriaId));
        averageValueByTeam.sort(Comparator.comparing(AverageValueInCriteria::getCriteriaId));

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

            int totalWeight = selfWeight + teamWeight + managerWeight;

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
                    / (totalWeight == 0 ? 1 : totalWeight)
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
        OverallRatedResDto result = new OverallRatedResDto();

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

    /**
     * Convert map to list and sort by criteriaId
     *
     * @param map map to convert
     * @return list of AverageValueInCriteria
     */
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
