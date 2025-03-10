package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IDepartmentRepository extends JpaRepository<Department, Long> {
    @Query("SELECT COUNT(d) > 0 FROM Department d WHERE lower(d.name) = lower(:name) AND d.deletedAt IS NULL")
    boolean existsByNameIgnoreCase(String name);
}
