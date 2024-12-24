package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAnswerRepository extends JpaRepository<Answer, Long> {
    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId AND a.isDeleted = false")
    Page<Answer> findByQuestion_Id(@Param("questionId") Long questionId, Pageable pageable);

    @Override
    @Query("SELECT a FROM Answer a WHERE a.isDeleted = false")
    List<Answer> findAll();

    @Override
    @Query("SELECT a FROM Answer a WHERE a.id = :id AND a.isDeleted = false")
    Optional<Answer> findById(@Param("id") Long id);
}