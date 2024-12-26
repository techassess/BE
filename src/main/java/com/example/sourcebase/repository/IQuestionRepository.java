package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IQuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q WHERE q.criteria.id = :criteriaId AND q.isDeleted = false")
    List<Question> findAllQuestionByCriteriaId(@Param("criteriaId") Long criteriaId);

    @Query("SELECT q FROM Question q WHERE q.criteria.id = :criteriaId AND q.isDeleted = false")
    Page<Question> findAllByCriteria_Id(@Param("criteriaId") Long criteriaId, Pageable pageable);

    @Override
    @Query("SELECT q FROM Question q WHERE q.isDeleted = false")
    Page<Question> findAll(Pageable pageable);

    @Override
    @Query("SELECT q FROM Question q WHERE q.id = :id AND q.isDeleted = false")
    Optional<Question> findById(@Param("id") Long id);

    @Query("SELECT q FROM Question q WHERE q.title LIKE %:title% AND q.isDeleted = false")
    Page<Question> findAllByTitleContains(String title, Pageable pageable);

    List<Question> findByCriteriaId(Long criteriaId);

}
