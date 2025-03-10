package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface IAnswerRepository extends JpaRepository<Answer, Long> {
    Page<Answer> findByQuestion_Id(@Param("questionId") Long questionId, Pageable pageable);
}