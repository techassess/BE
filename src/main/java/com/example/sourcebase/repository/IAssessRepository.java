package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Assess;
import com.example.sourcebase.domain.enumeration.ETypeAssess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAssessRepository extends JpaRepository<Assess, Long> {

    @Query("SELECT a FROM Assess a WHERE a.toUser.id = :userId")
    List<Assess> getListAssessOfUserId(Long userId);

    List<Assess> findByToUserIdAndAssessmentType(Long userId, ETypeAssess type);

    @Query("SELECT a FROM Assess a WHERE a.user.id = :userId")
    List<Assess> getListAssessByUserId(Long userId);

    @Query("SELECT a FROM Assess a WHERE a.toUser.id = :userId AND a.assessmentType = 'SELF'")
    Assess getAssessBySelf(Long userId);

    @Query("SELECT a FROM Assess a WHERE a.toUser.id = :userId AND a.assessmentType = 'TEAM'")
    List<Assess> getListAssessTeamOfUserId(Long userId);

    List<Assess> findByToUser_IdAndAssessmentTypeAndSubmitted(Long userId, ETypeAssess type, boolean submitted);

    /**
     * find all assess by toUser id
     * @param userId person being rated
     * @return list of assess
     */
    List<Assess> findByToUser_Id(Long userId);

    List<Assess> findByUser_IdAndToUser_Id(Long userId, Long toUserId);
}