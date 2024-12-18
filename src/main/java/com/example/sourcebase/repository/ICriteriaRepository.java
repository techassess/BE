package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICriteriaRepository extends JpaRepository<Criteria, Long> {
}
