package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IQuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q WHERE q.criteria.id = :criteriaId AND q.deletedAt IS NULL")
    List<Question> findAllQuestionByCriteriaId(@Param("criteriaId") Long criteriaId);

    @Query("SELECT q FROM Question q WHERE q.criteria.id = :criteriaId AND q.deletedAt IS NULL")
    Page<Question> findAllByCriteria_Id(@Param("criteriaId") Long criteriaId, Pageable pageable);

    Page<Question> findAllByTitleContains(String title, Pageable pageable);

    List<Question> findByCriteriaId(Long criteriaId);

}
