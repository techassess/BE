package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAnswerRepository extends JpaRepository<Answer, Long> {
    Page<Answer> findByQuestion_Id(Long id, Pageable pageable);
}