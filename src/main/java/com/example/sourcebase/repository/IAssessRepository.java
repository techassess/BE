package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Assess;
import com.example.sourcebase.domain.enumeration.ETypeAssess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAssessRepository extends JpaRepository<Assess, Long> {

    @Query("SELECT a FROM Assess a WHERE a.toUser.id = :userId")
    List<Assess> getListAssessOfUserId(Long userId);

    List<Assess> findByToUser_IdAndProject_Id(Long toUserId, Long projectId);

    @Query("SELECT a FROM Assess a " +
            "WHERE (:userId IS NULL OR a.user.id = :userId) " +
            "AND (:toUserId IS NULL OR a.toUser.id = :toUserId) " +
            "AND (:projectId IS NULL OR a.project.id = :projectId) " +
            "AND (:assessmentType IS NULL OR a.assessmentType = :assessmentType)")
    List<Assess> getAssess(
            @Param("userId") Long userId,
            @Param("toUserId") Long toUserId,
            @Param("projectId") Long projectId,
            @Param("assessmentType") String assessmentType
    );
//    Assess findByToUserIdAndAssessmentType(Long userId, ETypeAssess type);

    Assess findByToUserIdAndAssessmentTypeAndProjectId(Long toUserId, ETypeAssess assessmentType, Long projectId);

    @Query("SELECT a FROM Assess a WHERE a.user.id = :userId")
    List<Assess> getListAssessByUserId(Long userId);

    List<Assess> findByUser_IdAndProject_Id(Long userId, Long projectId);
    List<Assess> findByUser_IdAndToUser_IdAndProject_Id(Long userId,Long toUserId, Long projectId);
    @Query("SELECT a FROM Assess a WHERE a.toUser.id = :toUserId AND a.assessmentType = 'SELF'")
    Assess getAssessBySelf(Long toUserId);

    @Query("SELECT a FROM Assess a WHERE a.toUser.id = :toUserId AND a.assessmentType = 'TEAM'")
    List<Assess> getListAssessTeamOfUserId(Long toUserId);

    // find all assess by toUser id and project id and assessment type
    List<Assess> findAllByToUser_IdAndProject_IdAndAssessmentType(Long toUserId, Long projectId, ETypeAssess assessmentType);

    /**
     * find all assess by toUser id
     *
     * @param userId person being rated
     * @return list of assess
     */
    List<Assess> findByToUser_Id(Long userId);

    boolean existsByToUser_IdAndProject_IdAndAssessmentType(Long toUserId, Long projectId, ETypeAssess assessmentType);

    @Query("SELECT COUNT(a) FROM Assess a WHERE a.toUser.id = :toUserId AND a.project.id = :projectId AND a.assessmentType = :assessmentType")
    int countByToUser_IdAndProject_IdAndAssessmentType(Long toUserId, Long projectId, ETypeAssess assessmentType);
}