package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IQuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllQuestionByCriteriaId(Long criteriaId);

    Page<Question> findAllByCriteria_Id(Long criteriaId, Pageable pageable);
}
