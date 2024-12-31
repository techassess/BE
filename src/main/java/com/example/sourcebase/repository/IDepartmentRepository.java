package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Department;
import com.example.sourcebase.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IDepartmentRepository extends JpaRepository<Department, Long> {
    @Override
    @Query("SELECT d FROM Department d WHERE d.id = :id AND d.isDeleted = false")
    Optional<Department> findById(@Param("id") Long id);
}