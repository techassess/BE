package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICriteriaRepository extends JpaRepository<Criteria, Long> {
    @Override
    @Query("SELECT c FROM Criteria c WHERE c.isDeleted = false")
    List<Criteria> findAll();

    @Override
    @Query("SELECT c FROM Criteria c WHERE c.isDeleted = false")
    Page<Criteria> findAll(Pageable pageable);

    @Override
    @Query("SELECT c FROM Criteria c WHERE c.id = :id AND c.isDeleted = false")
    Optional<Criteria> findById(@Param("id") Long id);
}
